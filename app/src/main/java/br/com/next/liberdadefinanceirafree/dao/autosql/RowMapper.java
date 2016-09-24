package br.com.next.liberdadefinanceirafree.dao.autosql;

import android.database.Cursor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.next.liberdadefinanceirafree.dao.MetaDadosCache;
import br.com.next.liberdadefinanceirafree.dao.MetaDadosExtractor;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Column;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.dto.DTO;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.dto.Dominio;
import br.com.next.liberdadefinanceirafree.dao.autosql.exception.TypeNotSupportedException;
import br.com.next.liberdadefinanceirafree.util.DateUtils;


/**
 * Classe para realizar rowMapper: Preenche um objeto automaticamente a partir
 * de um cursor SQL.
 * 
 * @author thiago
 * @since 24/08/2011
 */
public final class RowMapper {
	
	private RowMapper() {
		super();
	}
	
	/**
	 * Realiza o fetch do cursor preenchendo uma lista de objetos
	 * @throws NoSuchMethodException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws TypeNotSupportedException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	public static List<?> fetchCursor(Class<?> clazz, Cursor cursor, boolean simpleSearch) {
		List<Object> list = new ArrayList<Object>();

		try {
			List<MetaDadosCache> cacheList = MetaDadosExtractor.extract(clazz);
			cursor.moveToFirst();
			
			while (!cursor.isAfterLast()) {
				Object instance = clazz.newInstance();
				
				for(MetaDadosCache cache : cacheList) {
					Column coluna = cache.getColumn();
					Dominio dominio = cache.getDominio();

					if(simpleSearch) {
						if(coluna != null && !coluna.simple()) {
							continue;
						}
					}
					
					if((coluna != null && coluna.selectable()) || 
							(clazz.isAnnotationPresent(DTO.class) && dominio != null)) {
						
						String columnName = null;
						
						if(coluna == null) {
							if(dominio.alias().length() > 0) {
								columnName = dominio.alias();
							} else {
								columnName = dominio.coluna();
							}
						} else {
							columnName = coluna.name();
						}
						
						int index = cursor.getColumnIndex(columnName);
						Object value = getFromCursor(cursor, cache.getField().getType(), index);
						
						if(Modifier.isPublic(cache.getField().getModifiers())) {
							cache.getField().set(instance, value);
						} else {
							cache.getSet().invoke(instance, value);
						}
					}
				}
				
				list.add(instance);
				cursor.moveToNext();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			cursor.close();
		}
		
		return list;
	}
	
	public static Object getFromCursor(Cursor cursor, Class<?> type, int position) throws TypeNotSupportedException {
		if(type.isPrimitive()) {
			type = Wrapper.box(type);
		}
		
		if(type == String.class) {
			return cursor.getString(position);
		} else if(type == Long.class) {
			return cursor.getLong(position);
		} else if(type == Double.class) {
			return cursor.getDouble(position);
		} else if(type == Integer.class) {
			return cursor.getInt(position);
		} else if(type == Boolean.class) {
			return cursor.getInt(position) == 1;
		} else if(type == Short.class) {
			return cursor.getShort(position);
		} else if(type == Date.class) {
			return DateUtils.stringDateOrNull(cursor.getString(position));
		} else if(type == Float.class) {
			return cursor.getFloat(position);
		} else if(type == Character.class) {
			String charact = cursor.getString(position);
			return charact != null && charact.equals("") ? ' ' : (charact == null ? ' ' : charact.charAt(0));
		} else if (type == BigDecimal.class) {
			return new BigDecimal(cursor.getFloat(position)).setScale(2, RoundingMode.HALF_EVEN);
		}
		throw new TypeNotSupportedException("Tipo nao suportado");
	}
}

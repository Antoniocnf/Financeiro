package br.com.next.liberdadefinanceirafree.dao.autosql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Column;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Id;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Table;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Unique;
import br.com.next.liberdadefinanceirafree.dao.script.RepositoryScript;
import br.com.next.liberdadefinanceirafree.util.CollectionUtils;
import br.com.next.liberdadefinanceirafree.util.DateUtils;
import br.com.next.liberdadefinanceirafree.util.MethodAccess;
import br.com.next.liberdadefinanceirafree.util.ReflectionUtils;

/**
 * Repositorio generico que provê funcionalidades de select, insert, update,
 * delete e transações no SQLite.
 * 
 * @author thiago
 * @since 11/08/2011
 */
public final class GenericRepository extends RepositoryScript {

	/**
	 * Pega SQLiteDatabase para realizar o select e save
	 */
	private static Context context;
	private static Object[] noArgs = new Object[]{};
	private Class<?> parametrizedClass;
	
	private GenericRepository(Context context) {
		super(context);
	}
	
	/**
	 * Garante que haverá apenas um objeto repositório usado pela thread principal do android
	 */
	public static GenericRepository getInstanceFor(Class<?> clazz) {
		GenericRepository repositorio = new GenericRepository(context);
		repositorio.parametrizedClass = clazz;
		return repositorio;
	}
	
	public Cursor rawQuery(String query) {
		return database().rawQuery(query, null);
	}
	
	public List<?> findList(String where) {
		Cursor cursor = database().query(SQLBuilder.tableName(parametrizedClass), 
				SQLBuilder.getSelectFields(parametrizedClass, false), 
				where, null, null, null, null);
		
		return (List<?>) RowMapper.fetchCursor(parametrizedClass, cursor, false);
	}
	
	/**
	 * Realiza uma busca retornando apenas um objeto
	 */
	public Object findObject(String where) {
		Cursor cursor = database().query(false, SQLBuilder.tableName(parametrizedClass), 
				SQLBuilder.getSelectFields(parametrizedClass, false), 
				where, null, null, null, null, "1");
		
		List<?> list = (List<?>) RowMapper.fetchCursor(parametrizedClass, cursor, false);
		return CollectionUtils.getFirst(list);
	}
	
	public Object findById(long id) {
		Field f = getFieldId();
		return findObject(String.format("%s = " + id, f.getAnnotation(Column.class).name()));
	}
	
	private Field getFieldId() {
		for(Field f : parametrizedClass.getDeclaredFields()) {
			if(f.isAnnotationPresent(Id.class)) {
				return f;
			}
		}
		
		throw new RuntimeException("Nenhum campo esta anotado com @Id");
	}
	
	private long persist(Object object) {
		try {
			final Class<?> clazz = object.getClass();
			final String table = SQLBuilder.tableName(clazz);
			final ContentValues values = createContentValues(object);
			
			long id = database().insertOrThrow(table, "", values);
			
			Table aTable = clazz.getAnnotation(Table.class);
			if(aTable!= null && aTable.hasTempTable()) {
				long idTmp = database().insertOrThrow("TMP_" + table, "", values);
				database().execSQL("UPDATE TMP_" + table + " SET ID_" + table + " = " + id + " WHERE ID_" + table + " = "+ idTmp);
			}
			
			Field f = getFieldId();
			
			if(Modifier.isPublic(f.getModifiers())) {
				f.set(object, id);
			} else {
				Method method = ReflectionUtils.getMethod(object.getClass(), MethodAccess.SET, f);
				method.invoke(object, new Object[]{ id });
			}
			return id;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Atualiza um registro no banco de dados
	 */
	private long update(Object object, String where, String chave) {
		final Class<?> clazz = object.getClass();
		final ContentValues contentValue = createContentValues(object);
		final String tableName = SQLBuilder.tableName(clazz);
		
		int id = database().update(tableName, contentValue, String.format("%s = ?", where), new String[]{ chave });
		
		Table aTable = clazz.getAnnotation(Table.class);
		if(aTable!= null && aTable.hasTempTable()) {
			if(aTable != null && aTable.hasTempTable()) {
				database().update("TMP_" + tableName, contentValue, String.format("%s = ?", where), new String[]{ chave });
			}
		}
		return id;
	}
	
	public Long persistOrMerge(Object object) {
		try {
			Field f = getFieldId();
	
			String id = null;
			
			if(Modifier.isPublic(f.getModifiers())) {
				id = String.valueOf(f.get(f));
			} else {
				Method m = ReflectionUtils.getMethod(parametrizedClass, MethodAccess.GET, f);
				id = String.valueOf(m.invoke(object, noArgs));
			}
			
			if(id.equals("0") || id.equals("null")) {
				return persist(object);
			} else {
				return update(object, ((Column) f.getAnnotation(Column.class)).name(), id);
			}
		} catch(Exception e) {
			throw new RuntimeException(discoverConstraintExceptionReason(object), e);
		}
	}
	
	private String discoverConstraintExceptionReason(Object object) {
		Field[] fields = object.getClass().getDeclaredFields();
		
		for(Field f : fields) {
			if(f.isAnnotationPresent(Unique.class)) {
				return String.format("O campo %s já possui um registro no banco com esse mesmo valor. " +
						"Verifique pois esse valor deve ser único.", f.getAnnotation(Unique.class).nome());
			}
		}
		
		return "Você está cadastrando um campo que já existe salvo no banco de dados. Verifique";
	}
	
	/**
	 * Remove registros da base de dados de acordo com clausula where e 
	 * seus argumentos.
	 * 
	 * Retorna a quantidade de registros que foram removidos
	 */
	public int remove(String where, String ...whereArgs) {
		final int status = database().delete(SQLBuilder.tableName(parametrizedClass), where, whereArgs);
		return status;
	}
	
	public void executeSQL(String sql) {
		database().execSQL(sql);
	}
	
	public void beginTransaction() {
		database().beginTransaction();
	}
	
	public void endTransaction() {
		database().endTransaction();
	}
	
	public void setTransactionSuccessful() {
		database().setTransactionSuccessful();
	}
	
	/**
	 * Cria o objeto ContentValue para adicionar no banco de dados
	 */
	private ContentValues createContentValues(Object clazz) {
		ContentValues contentValues = new ContentValues();
		
		Field[] fields = clazz.getClass().getDeclaredFields();
	
		for(Field f : fields) {
			if(!f.isAnnotationPresent(Column.class)) {
				continue;
			} else {
				Column column = f.getAnnotation(Column.class);
				if(column.insertable()) {
					Method method;
					try {
						if(Modifier.isPublic(f.getModifiers())) {
							addInContentValue(contentValues, column.name(), f.get(f));
						} else {
							method = ReflectionUtils.getMethod(clazz.getClass(), MethodAccess.GET, f);
							addInContentValue(contentValues, column.name(), method.invoke(clazz, noArgs));
						}
					} catch (Exception e) {
						throw new RuntimeException("Problema ao realizar comando SQL", e);
					}
				}
			}
		}
		return contentValues;
	}
	
	/**
	 * Cria o mapa ContentValue para adicionar parametros ao insert
	 */
	private void addInContentValue(ContentValues values, String key, Object value) {
		if(value instanceof String) {
			values.put(key, String.valueOf(value));
		} else if(value instanceof Long) {
			values.put(key, (Long) value);
		} else if(value instanceof Integer) {
			values.put(key, (Integer) value);
		} else if(value instanceof Boolean) {
			values.put(key, (Integer) (((Boolean) value) ? 1 : 0));
		} else if(value instanceof Double) {
			values.put(key, (Double) value);
		} else if(value instanceof Short) {
			values.put(key, (Short) value);
		} else if(value instanceof Date) {
			values.put(key, DateUtils.convert((Date) value));
		} else if(value instanceof Character) {
			values.put(key, String.valueOf(value));
		} else if(value instanceof Float) {
			values.put(key, (Float) value);
		}  else if (value instanceof BigDecimal) {
			values.put(key, ((BigDecimal) value).floatValue());
		}
	}
	
	public static void setContext(Context context) {
		GenericRepository.context = context;
	}

	public boolean inTransaction() {
		return database().inTransaction();
	}
}

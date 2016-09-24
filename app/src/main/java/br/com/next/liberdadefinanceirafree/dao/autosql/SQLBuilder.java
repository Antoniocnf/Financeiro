package br.com.next.liberdadefinanceirafree.dao.autosql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Column;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Id;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Table;
import br.com.next.liberdadefinanceirafree.util.ArrayUtils;
import br.com.next.liberdadefinanceirafree.util.StringUtils;

/**
 * Realiza montagem de SQL nativo
 * 
 * @author thiago
 * @since 01/09/2011
 */
public class SQLBuilder {

	/**
	 * Cria um apelido para uma tabela 
	 */
	public static String aliasTo(Class<?> clazz) {
		return clazz.getAnnotation(Table.class).name().substring(0, 3);
	}
	
	/**
	 * Retorna o nome da tabela no banco de dados que o objeto E mapeia
	 */
	public static String tableName(Class<?> clazz) {
		Table table = null;
		table = (Table) clazz.getAnnotation(Table.class);
		
		if(table == null || (table != null && !StringUtils.hasLength(table.name()))) {
			throw new RuntimeException("The entity must have a table name");
		} else {
			return table.name();
		}
	}
	
	/**
	 * Retorna os campos para realizar o select. Se for busca simples, a coluna deve
	 * ter o metadado simples na anotação coluna.
	 * 
	 * O ID sempre é buscado na busca simples.
	 */
	public static String[] getSelectFields(Class<?> clazz, boolean simpleSearch) {
		List<String> list = null;
		Field[] fields = null;
		
		try {
			fields = clazz.getDeclaredFields();
			
			if(!ArrayUtils.isEmpty(fields)) {
				list = new ArrayList<String>();
				
				for(Field f : fields) {
					if(!f.isAnnotationPresent(Column.class)) {
						continue;
					} else {
						Column column = f.getAnnotation(Column.class);
						if(column.selectable()) {
							if((simpleSearch && column.simple()) || !simpleSearch || f.isAnnotationPresent(Id.class)) {
								list.add(column.name());
							}
						}
					}
				}
			}
			
			return ArrayUtils.toStringArray(list);
		} catch (Exception e) {
			return new String[0];
		}
	}
	
//	/**
//	 * Utilizado somente para pegar campos de um DTO que forem de uma classe requerida
//	 */
//	public static String getSelectFields(Class<? extends DataTransferObject> clazz) {
//		StringBuilder select = new StringBuilder();
//		
//		for(Field f : clazz.getDeclaredFields()) {
//			if(f.isAnnotationPresent(Dominio.class)) {
//				Dominio domain = f.getAnnotation(Dominio.class);
//				select.append(String.format("%s.%s, ", SQLBuilder.aliasTo(domain.tabela()), domain.coluna()));
//				
//				if(domain.alias().length() > 0) {
//					select.delete(select.length() - 2, select.length());
//					select.append(String.format(" AS %s, ", domain.alias()));
//				}
//			}
//		}
//		
//		return select.delete(select.length() - 2, select.length()).toString();
//	}
//	
//	/**
//	 * Monta a clausula join de duas tabelas
//	 */
//	public static String joinClause(Class<? extends DataTransferObject> clazz, String alias, String aliasJoinClazz) {
//		Juncao join = clazz.getAnnotation(Juncao.class);
//		return String.format("%s.%s = %s.%s", alias, join.coluna(), aliasJoinClazz, join.matchWith());
//	}
}

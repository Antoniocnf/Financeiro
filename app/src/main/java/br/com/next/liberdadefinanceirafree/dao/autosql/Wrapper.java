package br.com.next.liberdadefinanceirafree.dao.autosql;

import java.util.Date;

/**
 * Classe para realizar box dos tipos primitivos para seus
 * respectivos wrappers.
 * 
 * @author thiago
 * @since 23/08/2011
 */
public final class Wrapper {

	private Wrapper() {
		super();
	}
	
	/**
	 * Realiza o box de um tipo primitivo para seu Wrapper
	 * 
	 * @param clazz
	 * @return
	 */
	public static Class<?> box(Class<?> clazz) {
		final String className = clazz.toString();
		
		if(className.equals("long")) {
			return Long.class;
		} else if(className.equals("int")) {
			return Integer.class;
		} else if(className.equals("double")) {
			return Double.class;
		} else if(className.equals("boolean")) {
			return Boolean.class;
		} else if(className.equals("short")) {
			return Short.class;
		} else if(className.equals("char")) {
			return Character.class;
		} else if(className.equals("float")) {
			return Float.class;
		} else {
			return null;
		}
	}
	
	public static String sqliteWrapper(Class<?> type) {
		String declarationType = null;
		
		if(type == String.class) {
			declarationType = "TEXT";
		} else if(type == Integer.class) {
			declarationType = "INTEGER";
		} else if(type == Boolean.class) {
			declarationType = "INTEGER";
		} else if(type == Long.class) {
			declarationType = "INTEGER";
		} else if(type == Double.class) {
			declarationType = "REAL";
		} else if(type == Short.class) {
			declarationType = "INTEGER";
		} else if(type == Date.class) {
			declarationType = "TEXT";
		} else if(type == Character.class) {
			declarationType = "TEXT";
		} else if(type == Float.class) {
			declarationType = "REAL";
		}
		
		return declarationType;
	}
}

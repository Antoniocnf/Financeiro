package br.com.next.liberdadefinanceirafree.dao.autosql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Representa uma tabela do banco de dados
 * 
 * @author thiago
 * @since 11/08/2011
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {

	/**
	 * Retorna o nome da tabela no banco de dados
	 */
	String name();
	
	boolean hasTempTable() default false;
}

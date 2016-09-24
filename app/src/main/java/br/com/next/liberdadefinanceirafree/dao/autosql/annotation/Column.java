package br.com.next.liberdadefinanceirafree.dao.autosql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Representa uma coluna de uma tabela.
 * 
 * @author thiago
 * @since 11/08/2011
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

	/**
	 * Nome da columa mapeada na tabela
	 */
	String name();
	
	/**
	 * Retorna se o valor deve ser inserido ou não na tabela
	 */
	boolean insertable() default true;
	
	/**
	 * Retorna se o valor deve ser selecionado ou não da tabela
	 */
	boolean selectable() default true;
	
	/**
	 * Realiza uma busca retornando apenas campos marcados como simples
	 */
	boolean simple() default false;
}

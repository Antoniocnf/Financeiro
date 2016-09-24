package br.com.next.liberdadefinanceirafree.dao.autosql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Representa uma coluna que deve ser única para manter consistencia do banco de dados.
 * 
 * @author thiago
 * @since 07/11/2011
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Unique {

	/**
	 * O framework lança a exceção ConstraintViolatedException já com uma mensagem
	 * formatada. Esse campo nome possibilita ao desenvolvedor usar uma mensagem personalizada. 
	 */
	String nome() default "";
}

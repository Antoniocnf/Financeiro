package br.com.next.liberdadefinanceirafree.dao.autosql.annotation.dto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação para demonstrar uma classe como um Data Transfer Object
 * 
 * @author thiago
 * @since 01/09/2011
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DTO {

	Class<?> classe();
	
	Class<?> joinClass();
}

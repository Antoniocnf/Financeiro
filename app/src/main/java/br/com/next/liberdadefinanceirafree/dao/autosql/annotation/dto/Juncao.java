package br.com.next.liberdadefinanceirafree.dao.autosql.annotation.dto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Utilizado para mostrar como é feito uma junção entre duas classes de um DTO
 * 
 * @author thiago
 * @since 01/09/2011
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Juncao {

	String coluna();
	
	String matchWith();
}

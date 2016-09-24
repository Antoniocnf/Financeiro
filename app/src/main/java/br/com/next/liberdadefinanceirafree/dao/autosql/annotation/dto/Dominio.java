package br.com.next.liberdadefinanceirafree.dao.autosql.annotation.dto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Utilizado para marcar uma propriedade de um DTO como sendo
 * pertencente a uma tabela e um campo
 * 
 * @author thiago
 * @since 01/09/2011
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Dominio {

	Class<?> tabela();
	
	String coluna();
	
	String alias() default "";
}

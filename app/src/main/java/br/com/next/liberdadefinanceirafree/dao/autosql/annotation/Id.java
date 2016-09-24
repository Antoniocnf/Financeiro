package br.com.next.liberdadefinanceirafree.dao.autosql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação para representar um id de uma tabela mapeado em um
 * objeto
 * 
 * @author thiago
 * @since 23/08/2011
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {

	boolean autoIncrement() default true;
}

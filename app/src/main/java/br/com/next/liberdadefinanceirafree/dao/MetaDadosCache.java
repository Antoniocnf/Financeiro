package br.com.next.liberdadefinanceirafree.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Column;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.dto.Dominio;


/**
 * Faz cache dos metados de um campo de uma classe;
 * 
 * @author thiago
 * @since 17/08/2012
 */
public class MetaDadosCache {

	private Column column;
	private Dominio dominio;
	private Field field;
	private Method get;
	private Method set;
	
	public MetaDadosCache() {
		super();
	}

	public final Column getColumn() {
		return column;
	}

	public final void setColumn(Column column) {
		this.column = column;
	}
	
	public final Dominio getDominio() {
		return dominio;
	}

	public final void setDominio(Dominio dominio) {
		this.dominio = dominio;
	}

	public final Field getField() {
		return field;
	}

	public final void setField(Field field) {
		this.field = field;
	}

	public final Method getGet() {
		return get;
	}

	public final void setGet(Method get) {
		this.get = get;
	}

	public final Method getSet() {
		return set;
	}

	public final void setSet(Method set) {
		this.set = set;
	}
	
	public String toString() {
		return String.format("Coluna: %s - Campo: %s", column.name(), field.getName());
	}
}

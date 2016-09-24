package br.com.next.liberdadefinanceirafree.util;

/**
 * Define qual os metodos de acesso à uma propriedade de uma classe
 * 
 * @author thiago
 * @since 08/09/2011
 */
public enum MethodAccess {

	GET("get"), SET("set");
	
	private MethodAccess(String type) {
		this.type = type;
	}
	
	private String type;
	
	public String type() {
		return type;
	}
}

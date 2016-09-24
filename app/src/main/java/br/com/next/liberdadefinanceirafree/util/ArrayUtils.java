package br.com.next.liberdadefinanceirafree.util;

import java.util.List;

/**
 * Classe utilitaria para manipulações e verificações em arrays.
 * 
 * @author thiago
 * @since 11/08/2011
 */
public final class ArrayUtils {

	private ArrayUtils() {
		super();
	}
	
	/**
	 * Verifica se o array está vazio
	 */
	public static <T> boolean isEmpty(T[] array) {
		return !(array != null && array.length > 0);
	}
	
	/**
	 * Cria um array de strings a partir de uma lista
	 */
	public static <T> String[] toStringArray(List<T> list) {
		String[] array = new String[list.size()];
		
		for(int i=0 ; i<list.size() ; i++) {
			array[i] = list.get(i).toString();
		}
		
		return array;
	}
	
}

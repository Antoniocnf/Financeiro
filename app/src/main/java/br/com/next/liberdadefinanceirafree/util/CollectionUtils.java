package br.com.next.liberdadefinanceirafree.util;

import java.util.Collection;
import java.util.List;

/**
 * Classe utilitaria com metodos estaticos para analise de colecoes
 * 
 * @author Thiago
 * @since 02/08/2010
 * @version 1.0
 */
public final class CollectionUtils {
	
	/**
	 * O construtor de classe utilitaria não pode ser instanciado
	 */
	private CollectionUtils() {
		super();
	}
	
	public static <T> T getFirst(List<T> collection) {
		return CollectionUtils.isEmpty(collection) ? null : collection.get(0);
	}
	
	/**
	 * Passada uma colecao para esse metodo, retorna true se a colecao eh vazia
	 * ou false caso possua algum objeto.
	 * 
	 * @param {@link List<T> list
	 * @return boolean
	 */
	public static <T> boolean isEmpty(Collection<T> collection) {
		if(collection == null || (collection != null && collection.isEmpty())) {
			return true;
		}
		return false;
	}
	
}

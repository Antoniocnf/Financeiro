package br.com.next.liberdadefinanceirafree.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Column;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.dto.Dominio;
import br.com.next.liberdadefinanceirafree.util.MethodAccess;
import br.com.next.liberdadefinanceirafree.util.ReflectionUtils;

/**
 * Classe para extrair os metadados dos atributos de uma classe
 * 
 * @author thiago
 * @since 17/08/2012
 */
public class MetaDadosExtractor {

	/**
	 * Extrai os metadados dos atributos de uma classe.
	 * 
	 * @param clazz
	 * @return
	 * @throws NoSuchMethodException
	 */
	public static List<MetaDadosCache> extract(Class<?> clazz) throws NoSuchMethodException {
		List<MetaDadosCache> list = new ArrayList<MetaDadosCache>();
		Field[] fields = clazz.getDeclaredFields();
		
		for(Field f : fields) {
			MetaDadosCache cache = new MetaDadosCache();
			Column column = f.getAnnotation(Column.class);
			Dominio dominio = f.getAnnotation(Dominio.class);
			
			if(column != null || dominio != null) {
				cache.setColumn(column);
				cache.setDominio(dominio);
				cache.setField(f);
				cache.setGet(ReflectionUtils.getMethod(clazz, MethodAccess.GET, f));
				cache.setSet(ReflectionUtils.getMethod(clazz, MethodAccess.SET, f));
				
				list.add(cache);
			}
		}
		
		return list;
	}
}

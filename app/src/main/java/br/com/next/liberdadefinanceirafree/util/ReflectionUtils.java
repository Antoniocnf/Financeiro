package br.com.next.liberdadefinanceirafree.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * Classe para ajudar nas operações com reflection
 * 
 * @author thiago
 * @since 08/09/2011
 */
public final class ReflectionUtils {

	private ReflectionUtils() {
		super();
	}
	
	public static String methodName(MethodAccess methodAccess, Field field) {
		return String.format("%s%c%s", methodAccess.type(), StringUtils.upper(field.getName()).charAt(0), field.getName().substring(1));
	}
	
	public static Method getMethod(Class<?> clazz, MethodAccess methodAccess, Field field) throws NoSuchMethodException {
		String methodName = ReflectionUtils.methodName(methodAccess, field);
		Method method = null;
		
		Class<?>[] classes = MethodAccess.GET == methodAccess ?  new Class[]{} : new Class[] { field.getType() };
		
		try {
			method = clazz.getMethod(methodName, classes);
		} catch (Exception e) {
			methodName = "is" + methodName.substring(3, methodName.length());
			try {
				method = clazz.getMethod(methodName, classes);
			} catch (Exception e1) {
				throw new NoSuchMethodException(String.format("Metodo (get/is/set) nao encontrado em %s, campo %s", clazz.getSimpleName(), field.getName()));
			}
		}
		
		return method;
	}
}

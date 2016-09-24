package br.com.next.liberdadefinanceirafree.util;

/**
 * Classe utilitaria com metodos para manipulacao e verificacao
 * de objetos do tipo String
 *
 * @author thiago
 * @version 1.0
 * @since 02/08/2010
 */
public final class StringUtils {

    /**
     * Faz o toUpperCase de uma string fazendo null-check
     */
    public static String upper(String value) {
        if (value == null) {
            return "";
        } else {
            return value.toUpperCase();
        }
    }

    /**
     * Passado uma String como parametro, verifica se possui conteudo.
     *
     * @param {@link String} value
     * @return {@link boolean} true or false
     */
    public static boolean hasLength(String value) {
        return (value == null || value.trim().length() == 0) ? false : true;
    }

}

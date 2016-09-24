package br.com.next.liberdadefinanceirafree.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author Alysson Myller
 * @since  10/06/2013
 */
public class PrecoUtils {
	
	private static final String DEFAULT_DECIMAL_FORMAT_PATTERN = "###,##0.00";
	
	/**
	 * @param valor
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 23/03/2013</p>
	 */
	public static BigDecimal convertToBigDecimal(long valor){
		return new BigDecimal(valor).setScale(2, RoundingMode.HALF_EVEN);
	}
	
	/**
	 * @param valor
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 25/03/2013</p>
	 */
	public static String formatoPadrao(BigDecimal valor){
		valor = valor.setScale(2, RoundingMode.HALF_EVEN);
		return new DecimalFormat(DEFAULT_DECIMAL_FORMAT_PATTERN).format(valor);
	}
	
	/**
	 * @param valor
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 25/03/2013</p>
	 */
	public static String formatoPadrao(Long valor){
		BigDecimal val = convertToBigDecimal(valor);
		return formatoPadrao(val);
	}

	/**
	 * @param valorTotal
	 * @param percentualMes
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 14/06/2013</p>
	 */
	public static BigDecimal getValorTotalContaMes(BigDecimal valorTotal, Short percentualMes){
		return valorTotal.multiply(new BigDecimal(percentualMes).divide(new BigDecimal(100)));
	}
	
	/**
	 * @param valor1
	 * @param valor2
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 16/07/2013</p>
	 */
	public static Integer calculaPorcentagemDeValorUtilizado(BigDecimal valor1, BigDecimal valor2){
		int porcentagem = ((Double) ((valor1.doubleValue() / valor2.doubleValue()) * 100)).intValue();
		return porcentagem;
	}
}

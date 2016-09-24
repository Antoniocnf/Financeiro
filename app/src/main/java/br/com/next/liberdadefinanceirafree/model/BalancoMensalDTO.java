package br.com.next.liberdadefinanceirafree.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Alysson Myller
 * @since 13/07/2013
 */
public class BalancoMensalDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer mes;
	private Integer ano;
	private BigDecimal valorCredito;
	private BigDecimal valorDebito;

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public BigDecimal getValorCredito() {
		return valorCredito;
	}

	public void setValorCredito(BigDecimal valorCredito) {
		this.valorCredito = valorCredito;
	}

	public BigDecimal getValorDebito() {
		return valorDebito;
	}

	public void setValorDebito(BigDecimal valorDebito) {
		this.valorDebito = valorDebito;
	}

}

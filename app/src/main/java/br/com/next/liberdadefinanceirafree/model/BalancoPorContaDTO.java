package br.com.next.liberdadefinanceirafree.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Alysson Myller
 * @since  19/08/2013
 */
public class BalancoPorContaDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer tipoConta;
	private String descricaoConta;
	private BigDecimal valorTotalDebito;

	public Integer getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(Integer tipoConta) {
		this.tipoConta = tipoConta;
	}

	public String getDescricaoConta() {
		return descricaoConta;
	}

	public void setDescricaoConta(String descricaoConta) {
		this.descricaoConta = descricaoConta;
	}

	public BigDecimal getValorTotalDebito() {
		return valorTotalDebito;
	}

	public void setValorTotalDebito(BigDecimal valorTotalDebito) {
		this.valorTotalDebito = valorTotalDebito;
	}

}

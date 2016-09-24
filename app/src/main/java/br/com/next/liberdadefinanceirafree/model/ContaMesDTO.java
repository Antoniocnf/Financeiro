package br.com.next.liberdadefinanceirafree.model;

import java.math.BigDecimal;

/**
 * @author Alysson Myller
 * @since 10/06/2013
 */
public class ContaMesDTO {

	private Long idConta;
	private String descricao;
	private BigDecimal valorRestante;
	private Integer tipoConta;

	public Long getIdConta() {
		return idConta;
	}

	public void setIdConta(Long idConta) {
		this.idConta = idConta;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValorRestante() {
		return valorRestante;
	}

	public void setValorRestante(BigDecimal valor) {
		this.valorRestante = valor;
	}

	public Integer getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(Integer tipoConta) {
		this.tipoConta = tipoConta;
	}

}

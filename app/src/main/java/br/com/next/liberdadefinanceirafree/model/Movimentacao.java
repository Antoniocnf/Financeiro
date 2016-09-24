package br.com.next.liberdadefinanceirafree.model;

import java.math.BigDecimal;

/**
 * @author Alysson Myller
 * @since  15/07/2013
 */
public class Movimentacao implements Comparable<Movimentacao> {

	private String descricao;
	private String data;
	private Integer tipoMovimentacao;
	private Integer idConta;
	private Integer tipoConta;
	private BigDecimal valor;
	private Boolean ehDebitoRecorrente;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Integer getTipoMovimentacao() {
		return tipoMovimentacao;
	}

	public void setTipoMovimentacao(Integer tipoMovimentacao) {
		this.tipoMovimentacao = tipoMovimentacao;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Integer getIdConta() {
		return idConta;
	}

	public void setIdConta(Integer idConta) {
		this.idConta = idConta;
	}

	public Boolean getEhDebitoRecorrente() {
		return ehDebitoRecorrente;
	}

	public void setEhDebitoRecorrente(Boolean ehDebitoRecorrente) {
		this.ehDebitoRecorrente = ehDebitoRecorrente;
	}

	public Integer getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(Integer tipoConta) {
		this.tipoConta = tipoConta;
	}

	@Override
	public int compareTo(Movimentacao another) {
		return getData().compareTo(another.getData());
	}
}

package br.com.next.liberdadefinanceirafree.model;

import java.math.BigDecimal;

import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Column;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Id;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Table;


/**
 * @author Alysson Myller
 * @since  10/06/2013
 */
@Table(name = "CREDITO")
public class Credito {

	@Id
	@Column(name = "ID_CREDITO")
	private Long id;
	
	@Column(name = "DESCRICAO")
	private String descricao;
	
	@Column(name = "DATA_MOVIMENTO")
	private String dataMovimento;
	
	@Column(name = "VALOR")
	private BigDecimal valor;
	
	@Column(name = "TIPO_CREDITO")
	private Long tipoCredito;

	@Column(name = "ID_CREDITO_RECORRENTE")
	private Long idCreditoRecorrente;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDataMovimento() {
		return dataMovimento;
	}

	public void setDataMovimento(String dataMovimento) {
		this.dataMovimento = dataMovimento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Long getTipoCredito() {
		return tipoCredito;
	}

	public void setTipoCredito(Long tipoCredito) {
		this.tipoCredito = tipoCredito;
	}

	public Long getIdCreditoRecorrente() {
		return idCreditoRecorrente;
	}

	public void setIdCreditoRecorrente(Long idCreditoRecorrente) {
		this.idCreditoRecorrente = idCreditoRecorrente;
	}
}

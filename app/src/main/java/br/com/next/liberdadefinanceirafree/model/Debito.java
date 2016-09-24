package br.com.next.liberdadefinanceirafree.model;

import java.math.BigDecimal;

import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Column;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Id;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Table;

/**
 * @author Alysson Myller
 * @since  10/06/2013
 */
@Table(name = "DEBITO")
public class Debito {

	@Id
	@Column(name = "ID_DEBITO")
	private Long id;
	
	@Column(name = "ID_CONTA")
	private Long idConta;
	
	@Column(name = "DESCRICAO")
	private String descricao;
	
	@Column(name = "DATA_MOVIMENTO")
	private String dataMovimento;
	
	@Column(name = "VALOR")
	private BigDecimal valor;
	
	@Column(name = "ID_DEBITO_RECORRENTE")
	private Long idDebitoRecorrente;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Long getIdDebitoRecorrente() {
		return idDebitoRecorrente;
	}

	public void setIdDebitoRecorrente(Long idDebitoRecorrente) {
		this.idDebitoRecorrente = idDebitoRecorrente;
	}

}

package br.com.next.liberdadefinanceirafree.model;

import java.math.BigDecimal;

import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Column;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Id;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Table;


/**
 * @author Alysson Myller
 * @since  27/07/2013
 */
@Table(name = "DEBITO_RECORRENTE")
public class DebitoRecorrente {

	@Id
	@Column(name = "ID_DEBITO_RECORRENTE")
	private Long id;
	
	@Column(name = "ID_CONTA")
	private Long idConta;
	
	@Column(name = "DESCRICAO")
	private String descricao;
	
	@Column(name = "VALOR")
	private BigDecimal valor;
	
	@Column(name = "DATA_LIMITE")
	private String dataLimite;

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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getDataLimite() {
		return dataLimite;
	}

	public void setDataLimite(String dataLimite) {
		this.dataLimite = dataLimite;
	}

}

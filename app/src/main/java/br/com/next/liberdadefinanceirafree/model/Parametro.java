package br.com.next.liberdadefinanceirafree.model;

import java.math.BigDecimal;

import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Column;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Id;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Table;

/**
 * @author Alysson Myller
 * @since 15/06/2013
 */
@Table(name = "PARAMETRO")
public class Parametro {

	@Id
	@Column(name = "ID_PARAMETRO")
	private Long id;

	@Column(name = "VALOR_RENDIMENTO_MENSAL")
	private BigDecimal valorRendimentoMensal;
	
	@Column(name = "DATA_ULTIMA_VERIFICACAO_MES")
	private String dataUltimaVerificacaoMes;
	
	@Column(name = "DATA_IMPORT_DADOS")
	private String dataImportDados;
	
	@Column(name = "DIA_RECEBIMENTO")
	private Integer diaRecebimento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValorRendimentoMensal() {
		return valorRendimentoMensal;
	}

	public void setValorRendimentoMensal(BigDecimal valorRendimentoMensal) {
		this.valorRendimentoMensal = valorRendimentoMensal;
	}

	public String getDataUltimaVerificacaoMes() {
		return dataUltimaVerificacaoMes;
	}

	public void setDataUltimaVerificacaoMes(String dataUltimaVerificacaoMes) {
		this.dataUltimaVerificacaoMes = dataUltimaVerificacaoMes;
	}

	public String getDataImportDados() {
		return dataImportDados;
	}

	public void setDataImportDados(String dataImportDados) {
		this.dataImportDados = dataImportDados;
	}

	public Integer getDiaRecebimento() {
		return diaRecebimento;
	}

	public void setDiaRecebimento(Integer diaRecebimento) {
		this.diaRecebimento = diaRecebimento;
	}
	
}

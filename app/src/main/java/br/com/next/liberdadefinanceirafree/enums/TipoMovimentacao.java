package br.com.next.liberdadefinanceirafree.enums;

/**
 * @author Alysson Myller
 * @since  15/07/2013
 */
public enum TipoMovimentacao {
	
	CREDITO ("Crédito", 1),
	DEBITO ("Débito", 2);
	
	private String descricao;
	private Integer valor;
	
	private TipoMovimentacao(String descricao, Integer valor){
		this.descricao = descricao;
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public Integer getValor() {
		return valor;
	}
	
	/**
	 * @param valor
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 15/07/2013</p>
	 */
	public static TipoMovimentacao getInstance(Integer valor){
		for (TipoMovimentacao t : TipoMovimentacao.values()){
			if (t.getValor().equals(valor)){
				return t;
			}
		}
		return null;
	}
}

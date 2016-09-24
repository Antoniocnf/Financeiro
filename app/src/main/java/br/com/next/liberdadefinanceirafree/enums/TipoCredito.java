package br.com.next.liberdadefinanceirafree.enums;

/**
 * @author Alysson Myller
 * @since  10/06/2013
 */
public enum TipoCredito {
	
	RENDIMENTO_MENSAL ("Rendimento Mensal", 1L),
	RESTANTE_MES_PASSADO ("Restante Mês Passado", 2L), 
	NOVO_CREDITO ("Novo Crédito", 3L);

	private String descricao;
	private Long valor;
	
	private TipoCredito(String descricao, Long valor){
		this.descricao = descricao;
		this.valor = valor;
	}
	
	public Long getValor() {
		return valor;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	/**
	 * @param valor
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 10/06/2013</p>
	 */
	public static TipoCredito getInstance(Long valor){
		for (TipoCredito e : TipoCredito.values()){
			if (e.getValor().equals(valor)){
				return e;
			}
		}
		return null;
	}

}

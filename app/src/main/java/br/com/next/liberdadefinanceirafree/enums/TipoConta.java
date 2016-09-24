package br.com.next.liberdadefinanceirafree.enums;

/**
 * @author Alysson Myller
 * @since  16/07/2013
 */
public enum TipoConta {

	LIBERDADE_FINANCEIRA ("Liberdade Financeira", 1),
	DIVERSAO ("Diversão", 2),
	POUPANCA ("Poupança", 3),
	INSTRUCAO_FINANCEIRA ("Instrução Financeira", 4),
	NECESSIDADES_BASICAS ("Necessidades Básicas", 5),
	DOACOES ("Doações", 6);
	
	private String descricao;
	private Integer valor;
	
	private TipoConta(String descricao, Integer valor){
		this.descricao = descricao;
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public Integer getValor() {
		return valor;
	}
	
	public static TipoConta getInstance(Integer valor){
		for (TipoConta e : TipoConta.values()){
			if (e.getValor().equals(valor)){
				return e;
			}
		}
		return null;
	}

	public static TipoConta getInstance(String descricao){
		for (TipoConta e : TipoConta.values()){
			if (e.getDescricao().equals(descricao)){
				return e;
			}
		}
		return null;
	}
}

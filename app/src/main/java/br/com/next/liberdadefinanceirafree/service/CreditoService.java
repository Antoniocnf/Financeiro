package br.com.next.liberdadefinanceirafree.service;

import android.content.Context;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.next.liberdadefinanceirafree.R;
import br.com.next.liberdadefinanceirafree.dao.autosql.GenericRepository;
import br.com.next.liberdadefinanceirafree.enums.TipoCredito;
import br.com.next.liberdadefinanceirafree.model.Credito;
import br.com.next.liberdadefinanceirafree.model.CreditoRecorrente;
import br.com.next.liberdadefinanceirafree.model.Movimentacao;
import br.com.next.liberdadefinanceirafree.util.DateUtils;

/**
 * @author Alysson Myller
 * @since  10/06/2013
 */
public class CreditoService {

	/**
	 * @param valor
	 * @param tipoCredito
	 * <p><b>Autoria: </b>Alysson Myller - 10/06/2013</p>
	 */
	public static void criarCredito(Context context, BigDecimal valor, TipoCredito tipoCredito) {
		criarCredito(context, null, valor, tipoCredito);
	}
	
	/**
	 * @param descricao
	 * @param valor
	 * @param tipoCredito
	 * <p><b>Autoria: </b>Alysson Myller - 21/06/2013</p>
	 */
	public static void criarCredito(Context context, String descricao, BigDecimal valor, TipoCredito tipoCredito) {

//		Não sei pq tinha essa trava - vou deixar liberado por enquanto (22/09/2016)
//		// Não deixa cadastrar o mesmo crédito de rendimento mensal no mesmo mês.
//		GenericRepository rep = GenericRepository.getInstanceFor(Credito.class);
//		if (TipoCredito.RENDIMENTO_MENSAL.equals(tipoCredito)){
//			Credito credito = (Credito) rep.findObject(" strftime('%Y-%m', data_movimento) = strftime('%Y-%m', 'now') ; ");
//			if (credito != null){
//				return;
//			}
//		}
		
		Credito c = new Credito();
		c.setDataMovimento(DateUtils.now("yyyy-MM-dd"));
		c.setDescricao(getDescricaoCredito(context, descricao, tipoCredito));
		c.setTipoCredito(tipoCredito.getValor());
		c.setValor(valor);

		GenericRepository rep = GenericRepository.getInstanceFor(Credito.class);
		rep.persistOrMerge(c);
	}

	public static void criarCreditoRecorrente(String descricao, BigDecimal valor, String dataLimiteRecorrencia) throws ParseException {

		// Grava o crédito recorrente e depois repassa o id do crédito recorrente para os créditos
		CreditoRecorrente creditoRecorrente = CreditoRecorrenteService.criarCreditoRecorrente(descricao, valor, dataLimiteRecorrencia);

		Date hoje = new Date();
		Date dataLimite = DateUtils.convertFromDefaultDatabaseFormat(dataLimiteRecorrencia);
		int diffMeses = DateUtils.getDiffMeses(hoje, dataLimite);

		Calendar dataMov = Calendar.getInstance();
		dataMov.setTime(new Date());

		// Cadastro o débito para o mês atual
		Credito credito = new Credito();
		credito.setValor(valor);
		credito.setDescricao(descricao);
		credito.setDataMovimento(DateUtils.convertToDefaultDatabaseFormat(dataMov.getTime()));
		credito.setIdCreditoRecorrente(creditoRecorrente.getId());
		credito.setTipoCredito(TipoCredito.NOVO_CREDITO.getValor());
		
		GenericRepository.getInstanceFor(Credito.class).persistOrMerge(credito);

		// Cadastro os créditos recorrentes
		for (int i = 0; i < (diffMeses); i ++){
			dataMov.add(Calendar.MONTH, 1);
			String dataMovimento = DateUtils.convertToDefaultDatabaseFormat(dataMov.getTime());

			Credito credito2 = new Credito();
			credito2.setValor(valor);
			credito2.setDescricao(descricao);
			credito2.setDataMovimento(dataMovimento);
			credito2.setIdCreditoRecorrente(creditoRecorrente.getId());
			credito2.setTipoCredito(TipoCredito.NOVO_CREDITO.getValor());

			GenericRepository.getInstanceFor(Credito.class).persistOrMerge(credito2);
		}
	}

	/**
	 * @param descricao
	 * @param tipoCredito
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 05/08/2013</p>
	 */
	private static String getDescricaoCredito(Context context, String descricao, TipoCredito tipoCredito) {
		if (descricao != null && !descricao.isEmpty()){
			return descricao;
		}
		
		switch (tipoCredito) {
		case NOVO_CREDITO:
			return context.getString(R.string.descricao_tipo_credito_novo_credito);
			
		case RENDIMENTO_MENSAL:
			return context.getString(R.string.descricao_tipo_credito_rendimento_mensal);
			
		case RESTANTE_MES_PASSADO:
			return context.getString(R.string.descricao_tipo_credito_restante_mes_passado);

		default:
			return "";
		}
	}

	/**
	 * @param valor
	 * <p><b>Autoria: </b>Alysson Myller - 14/06/2013</p>
	 */
	public static void alterarRendimentoMensal(Context context, BigDecimal valor) {
		// Não vejo a necessidade de criar um crédito quando o usuário solicita pra trocar o rendimento. 22/09/2016.
		// Agora que estou desenvolvendo créditos recorrentes (alem do salario), o usuário pode excluir e cadastrar créditos
		// a qualquer momento. Por enquanto vou deixar sem esse código.
//		GenericRepository rep = GenericRepository.getInstanceFor(Credito.class);
//		Credito credito = (Credito) rep.findObject(" STRFTIME('%m', DATA_MOVIMENTO) = STRFTIME('%m', 'now') AND TIPO_CREDITO = " + TipoCredito.RENDIMENTO_MENSAL.getValor());
//
//		if (credito == null){
//			criarCredito(context, valor, TipoCredito.RENDIMENTO_MENSAL);
//		} else {
//			credito.setValor(valor);
//			rep.persistOrMerge(credito);
//		}
		
		ParametroService.alterarRendimentoMensal(valor);
	}

	/**
	 * @param credito
	 * <p><b>Autoria: </b>Alysson Myller - 21/06/2013</p>
	 */
	public static void remover(Credito credito) {
		GenericRepository rep = GenericRepository.getInstanceFor(Credito.class);
		rep.executeSQL(" DELETE FROM CREDITO WHERE ID_CREDITO = " + credito.getId());
	}

	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 21/06/2013</p>
	 */
	@SuppressWarnings("unchecked")
	public static List<Credito> buscarCreditosMesAtual() {
		GenericRepository rep = GenericRepository.getInstanceFor(Credito.class);
		return (List<Credito>) rep.findList(" STRFTIME('%Y-%m', DATA_MOVIMENTO) = STRFTIME('%Y-%m', 'now') ");
	}
	
	/**
	 * <p><b>Autoria: </b>Alysson Myller - 17/09/2016</p>
	 */
	public static void criarCreditoDeMovimentacao(Movimentacao movimentacao) {
		Credito c = new Credito();
		c.setDataMovimento(movimentacao.getData());
		c.setDescricao(movimentacao.getDescricao());
		c.setTipoCredito(TipoCredito.NOVO_CREDITO.getValor());
		c.setValor(movimentacao.getValor());

		GenericRepository rep = GenericRepository.getInstanceFor(Credito.class);
		rep.persistOrMerge(c);
	}

}
package br.com.next.liberdadefinanceirafree.service;

import android.content.Context;
import android.database.Cursor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.next.liberdadefinanceirafree.R;
import br.com.next.liberdadefinanceirafree.dao.autosql.GenericRepository;
import br.com.next.liberdadefinanceirafree.enums.TipoConta;
import br.com.next.liberdadefinanceirafree.model.Conta;
import br.com.next.liberdadefinanceirafree.model.ContaMesDTO;
import br.com.next.liberdadefinanceirafree.model.Credito;
import br.com.next.liberdadefinanceirafree.model.Debito;

/**
 * @author Alysson Myller
 * @since  10/06/2013
 */
public class ContaService {

	/**
	 * <p><b>Autoria: </b>Alysson Myller - 10/06/2013</p>
	 */
	@SuppressWarnings("unchecked")
	public static void criarDadosContas(Context context) {
		GenericRepository rep = GenericRepository.getInstanceFor(Conta.class);
		List<Conta> contaSalvas = (List<Conta>) rep.findList(null);
		
		if (contaSalvas == null || contaSalvas.isEmpty()){
			List<Conta> contas = new ArrayList<Conta>();

			Conta liberdadeFinanceira = new Conta();
			liberdadeFinanceira.setDescricao(context.getString(R.string.nome_conta_liberdade_financeira));
			liberdadeFinanceira.setTipoConta(TipoConta.LIBERDADE_FINANCEIRA.getValor());
			liberdadeFinanceira.setPercentualMes(new Short("10"));
			contas.add(liberdadeFinanceira);

			Conta diversao = new Conta();
			diversao.setDescricao(context.getString(R.string.nome_conta_diversao));
			diversao.setTipoConta(TipoConta.DIVERSAO.getValor());
			diversao.setPercentualMes(new Short("10"));
			contas.add(diversao);

			Conta poupanca = new Conta();
			poupanca.setDescricao(context.getString(R.string.nome_conta_poupanca));
			poupanca.setTipoConta(TipoConta.POUPANCA.getValor());
			poupanca.setPercentualMes(new Short("10"));
			contas.add(poupanca);

			Conta instrucaoFinanceira = new Conta();
			instrucaoFinanceira.setDescricao(context.getString(R.string.nome_conta_instrucao_financeira));
			instrucaoFinanceira.setTipoConta(TipoConta.INSTRUCAO_FINANCEIRA.getValor());
			instrucaoFinanceira.setPercentualMes(new Short("10"));
			contas.add(instrucaoFinanceira);

			Conta necessidadeBasica = new Conta();
			necessidadeBasica.setDescricao(context.getString(R.string.nome_conta_necessidade_basica));
			necessidadeBasica.setTipoConta(TipoConta.NECESSIDADES_BASICAS.getValor());
			necessidadeBasica.setPercentualMes(new Short("50"));
			contas.add(necessidadeBasica);

			Conta doacoes = new Conta();
			doacoes.setDescricao(context.getString(R.string.nome_conta_doacoes));
			doacoes.setTipoConta(TipoConta.DOACOES.getValor());
			doacoes.setPercentualMes(new Short("10"));
			contas.add(doacoes);
			
			for (Conta c : contas){
				rep.persistOrMerge(c);
			}
		}
	}
	
	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 14/06/2013</p>
	 */
	public static BigDecimal buscarValorTotalCreditoMesAtual(){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT SUM(VALOR) ");
		sql.append("   FROM CREDITO ");
		sql.append("  WHERE strftime('%Y-%m', data_movimento) = strftime('%Y-%m', 'now') ");
		
		GenericRepository rep = GenericRepository.getInstanceFor(Credito.class);
		Cursor cursor = rep.rawQuery(sql.toString());
		
		if (cursor == null || !cursor.moveToFirst()){
			return BigDecimal.ZERO;
		}

		return new BigDecimal(cursor.getDouble(0));
	}
	
	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 14/06/2013</p>
	 */
	public static BigDecimal buscarValorTotalCreditoMesPassado(){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT SUM(VALOR) ");
		sql.append("   FROM CREDITO ");
		sql.append("  WHERE strftime('%Y-%m', data_movimento) = strftime('%Y-%m', 'now', '-1 month') ");
		
		GenericRepository rep = GenericRepository.getInstanceFor(Credito.class);
		Cursor cursor = rep.rawQuery(sql.toString());
		
		if (cursor == null || !cursor.moveToFirst()){
			return BigDecimal.ZERO;
		}

		return new BigDecimal(cursor.getDouble(0));
	}
	
	/**
	 * @param idConta
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 14/06/2013</p>
	 */
	public static BigDecimal buscarValorTotalDebitoContaMesAtual(Long idConta){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT SUM(VALOR) ");
		sql.append("   FROM DEBITO ");
		sql.append("  WHERE strftime('%Y-%m', data_movimento) = strftime('%Y-%m', 'now') ");
		sql.append("    AND ID_CONTA = ").append(idConta);
		
		GenericRepository rep = GenericRepository.getInstanceFor(Debito.class);
		Cursor cursor = rep.rawQuery(sql.toString());
		
		if (cursor == null || !cursor.moveToFirst()){
			return BigDecimal.ZERO;
		}

		return new BigDecimal(cursor.getDouble(0));
	}

	/**
	 * @param idConta
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 14/06/2013</p>
	 */
	public static Conta buscarPorId(Long idConta) {
		if (idConta == null){
			throw new RuntimeException("É obrigatório informar o idConta");
		}
		
		GenericRepository rep = GenericRepository.getInstanceFor(Conta.class);
		return (Conta) rep.findById(idConta);
	}

	/**
	 * @param context
	 * @param tipoConta
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 16/07/2013</p>
	 */
	public static String getDescricaoDetalheConta(Context context, Integer tipoConta) {
		switch (TipoConta.getInstance(tipoConta)) {
		case DIVERSAO:
			return context.getString(R.string.conta_reservada_para_sua_divers_o_cinema_bares_boates_etc_);
			
		case INSTRUCAO_FINANCEIRA:
			return context.getString(R.string.conta_reservada_para_seu_conhecimento_financeiro_assim_como_livros_e_revistas_sobre_neg_cios_e_investimentos_);
			
		case DOACOES:
			return context.getString(R.string.conta_reservada_para_doa_es_o_que_muito_importante_);
			
		case LIBERDADE_FINANCEIRA:
			return context.getString(R.string.conta_reservada_para_seus_investimentos_assim_como_a_es_im_veis_fundos_de_investimentos_etc_);
			
		case NECESSIDADES_BASICAS:
			return context.getString(R.string.conta_reservada_para_suas_necessidades_b_sicas_como_gua_luz_telefone_etc_);
			
		case POUPANCA:
			return context.getString(R.string.conta_reservada_para_sua_poupan_a_este_dinheiro_n_o_deve_ser_gasto_deve_se_somente_guard_lo_);

		default:
			return "";
		}
	}

	/**
	 * @param id
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 26/07/2013</p>
	 */
	public static BigDecimal buscarValorTotalDebitoContaMesPassado(Long id) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT SUM(VALOR) ");
		sql.append("   FROM DEBITO ");
		sql.append("  WHERE strftime('%Y-%m', data_movimento) = strftime('%Y-%m', 'now', '-1 month') ");
		sql.append("    AND ID_CONTA = ").append(id);
		
		GenericRepository rep = GenericRepository.getInstanceFor(Debito.class);
		Cursor cursor = rep.rawQuery(sql.toString());
		
		if (cursor == null || !cursor.moveToFirst()){
			return BigDecimal.ZERO;
		}

		return new BigDecimal(cursor.getDouble(0));
	}
	
	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 27/07/2013</p>
	 */
	public static BigDecimal getValorRestanteContasMesPassado() {
		BigDecimal valorRestanteTodasContas = BigDecimal.ZERO;
		List<ContaMesDTO> contas = ContaMesService.getContasMesPassado();
		for (ContaMesDTO conta : contas){
			valorRestanteTodasContas = valorRestanteTodasContas.add(conta.getValorRestante());
		}
		return valorRestanteTodasContas;
	}

	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 01/08/2013</p>
	 */
	@SuppressWarnings("unchecked")
	public static List<Conta> buscarTodas() {
		GenericRepository rep = GenericRepository.getInstanceFor(Conta.class);
		return (List<Conta>) rep.findList(null);
	}

	/**
	 * @param tipoConta
	 * <p><b>Autoria: </b>Alysson Myller - 01/08/2013</p>
	 */
	public static void alterarPorcentagemConta(Integer tipoConta, Integer percentual) {
		GenericRepository rep = GenericRepository.getInstanceFor(Conta.class);
		Conta conta = (Conta) rep.findObject(" TIPO_CONTA = " + tipoConta);
		
		conta.setPercentualMes(percentual.shortValue());
		rep.persistOrMerge(conta);
	}

	/**
	 * @param context
	 * @param tipoConta
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 12/08/2013</p>
	 */
	public static String getDescricaoNomeConta(Context context, Integer tipoConta) {
		switch (TipoConta.getInstance(tipoConta)) {
		case INSTRUCAO_FINANCEIRA:
			return context.getString(R.string.nome_conta_instrucao_financeira);
			
		case DIVERSAO:
			return context.getString(R.string.nome_conta_diversao);
			
		case DOACOES:
			return context.getString(R.string.nome_conta_doacoes);
			
		case LIBERDADE_FINANCEIRA:
			return context.getString(R.string.nome_conta_liberdade_financeira);
			
		case NECESSIDADES_BASICAS:
			return context.getString(R.string.nome_conta_necessidade_basica);
			
		case POUPANCA:
			return context.getString(R.string.nome_conta_poupanca);

		default:
			return "";
		}
	}

}

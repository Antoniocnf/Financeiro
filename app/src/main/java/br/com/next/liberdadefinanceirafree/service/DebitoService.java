package br.com.next.liberdadefinanceirafree.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.next.liberdadefinanceirafree.dao.autosql.GenericRepository;
import br.com.next.liberdadefinanceirafree.model.Debito;
import br.com.next.liberdadefinanceirafree.model.DebitoRecorrente;
import br.com.next.liberdadefinanceirafree.model.Movimentacao;
import br.com.next.liberdadefinanceirafree.util.DateUtils;

/**
 * @author Alysson Myller
 * @since  14/06/2013
 */
public class DebitoService {

	/**
	 * @param idConta
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 14/06/2013</p>
	 */
	@SuppressWarnings("unchecked")
	public static List<Debito> getDebitosDoMesPorIdConta(Long idConta) {
		String where = " ID_CONTA = " + idConta + " AND STRFTIME('%Y-%m', DATA_MOVIMENTO) = STRFTIME('%Y-%m', 'now') ";
		
		GenericRepository rep = GenericRepository.getInstanceFor(Debito.class);
		return (List<Debito>) rep.findList(where);
	}

	/**
	 * @param idConta
	 * @param descricao
	 * @param valor
	 * <p><b>Autoria: </b>Alysson Myller - 14/06/2013</p>
	 * @throws Exception
	 */
	public static void criarNovoDebitoRecorrente(Long idConta, String descricao, BigDecimal valor, String dataLimiteRecorrencia) throws ParseException {
		
		// Grava o débito recorrente e depois repassa o id do débito recorrente para o débito 
		DebitoRecorrente debitoRecorrente = DebitoRecorrenteService.criarDebitoRecorrente(idConta, descricao, valor, dataLimiteRecorrencia);

		Date hoje = new Date();
		Date dataLimite = DateUtils.convertFromDefaultDatabaseFormat(dataLimiteRecorrencia);
		int diffMeses = DateUtils.getDiffMeses(hoje, dataLimite);

		// diffMeses + 1 porque tem que gerar um debito do mes atual e gerar um débito no último mês de recorrencia
		Calendar dataMov = Calendar.getInstance();
		dataMov.setTime(new Date());

		// Cadastro o débito para o mês atual
		Debito debito = new Debito();
		debito.setDataMovimento(DateUtils.convertToDefaultDatabaseFormat(dataMov.getTime()));
		debito.setDescricao(descricao);
		debito.setIdConta(idConta);
		debito.setValor(valor);
		debito.setIdDebitoRecorrente(debitoRecorrente.getId());
		GenericRepository.getInstanceFor(Debito.class).persistOrMerge(debito);

		// Cadastro os débitos recorrentes
		for (int i = 0; i < (diffMeses); i ++){
			dataMov.add(Calendar.MONTH, 1);
			String dataMovimento = DateUtils.convertToDefaultDatabaseFormat(dataMov.getTime());

			Debito debito2 = new Debito();
			debito2.setDataMovimento(dataMovimento);
			debito2.setDescricao(descricao);
			debito2.setIdConta(idConta);
			debito2.setValor(valor);
			debito2.setIdDebitoRecorrente(debitoRecorrente.getId());
			GenericRepository.getInstanceFor(Debito.class).persistOrMerge(debito2);
		}
	}
	
	/**
	 * @param idConta
	 * @param descricao
	 * @param valor
	 * <p><b>Autoria: </b>Alysson Myller - 05/08/2013</p>
	 */
	public static void criarNovoDebito(Long idConta, String descricao, BigDecimal valor) {
		Debito debito = new Debito();
		debito.setDataMovimento(DateUtils.now("yyyy-MM-dd"));
		debito.setDescricao(descricao);
		debito.setIdConta(idConta);
		debito.setValor(valor);
		GenericRepository rep = GenericRepository.getInstanceFor(Debito.class);
		rep.persistOrMerge(debito);
	}

	/**
	 * @param id
	 * <p><b>Autoria: </b>Alysson Myller - 14/06/2013</p>
	 */
	public static void removerPorId(Long id) {
		GenericRepository rep = GenericRepository.getInstanceFor(Debito.class);
		rep.remove(" ID_DEBITO = " + id);
	}

	/**
	 * <p><b>Autoria: </b>Alysson Myller - 17/09/2016</p>
	 */
	public static void criarDebitoDeMovimentacao(Movimentacao m) {
		Debito debito = new Debito();
		debito.setDataMovimento(m.getData());
		debito.setDescricao(m.getDescricao());
		debito.setIdConta(m.getTipoConta().longValue()); //Estou usando o tipo conta pq é o mesmo do id da tabela (eu espero !!!)
		debito.setValor(m.getValor());

		GenericRepository rep = GenericRepository.getInstanceFor(Debito.class);
		rep.persistOrMerge(debito);
	}

	/**
	 *
	 * @param idDebitoRecorrente
     */
    public static void removerTodosRecorrentes(Long idDebitoRecorrente) {
		GenericRepository rep = GenericRepository.getInstanceFor(Debito.class);
		rep.remove(" id_debito_recorrente = " + idDebitoRecorrente);
	}
}

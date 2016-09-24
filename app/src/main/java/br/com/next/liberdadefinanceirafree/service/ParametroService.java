package br.com.next.liberdadefinanceirafree.service;

import java.math.BigDecimal;
import java.util.Date;

import br.com.next.liberdadefinanceirafree.dao.autosql.GenericRepository;
import br.com.next.liberdadefinanceirafree.model.Parametro;
import br.com.next.liberdadefinanceirafree.util.DatePatternUtils;
import br.com.next.liberdadefinanceirafree.util.DateUtils;

/**
 * @author Alysson Myller
 * @since  15/06/2013
 */
public class ParametroService {

	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 15/06/2013</p>
	 */
	public static BigDecimal buscarUltimoValorRendimentoMensal() {
		GenericRepository rep = GenericRepository.getInstanceFor(Parametro.class);
		Parametro param = (Parametro) rep.findObject(null);
		return param.getValorRendimentoMensal();
	}

	/**
	 * @param valor
	 * <p><b>Autoria: </b>Alysson Myller - 15/06/2013</p>
	 */
	public static void alterarRendimentoMensal(BigDecimal valor) {
		GenericRepository rep = GenericRepository.getInstanceFor(Parametro.class);
		Parametro param = (Parametro) rep.findObject(null);
		param.setValorRendimentoMensal(valor);
		rep.persistOrMerge(param);
	}
	
	/**
	 * @param valorRendimentoMensal
	 * <p><b>Autoria: </b>Alysson Myller - 15/06/2013</p>
	 */
	public static void criaParametro(BigDecimal valorRendimentoMensal, Integer diaRecebimento, String dataUltimaVerificacaoMes){
		Parametro parametro = new Parametro();
		parametro.setValorRendimentoMensal(valorRendimentoMensal);
		parametro.setDiaRecebimento(diaRecebimento);
		parametro.setDataUltimaVerificacaoMes(dataUltimaVerificacaoMes);
		
		GenericRepository.getInstanceFor(Parametro.class).persistOrMerge(parametro);
	}

	/**
	 * @param data
	 * <p><b>Autoria: </b>Alysson Myller - 26/07/2013</p>
	 */
	public static void atualizaDataUltimaVerificacaoMes(String data) {
		GenericRepository rep = GenericRepository.getInstanceFor(Parametro.class);
		Parametro param = (Parametro) rep.findObject(null);
		
		if (param == null){
			param = new Parametro();
		}
		param.setDataUltimaVerificacaoMes(data);
		rep.persistOrMerge(param);
	}
	
	/**
	 * @return Data de última verificação do mês (Nunca NULO)
	 * @throws Exception
	 * <p><b>Autoria: </b>Alysson Myller - 26/07/2013</p>
	 */
	public static Date getDataUltimaVerificacaoMes() throws Exception{
		GenericRepository rep = GenericRepository.getInstanceFor(Parametro.class);
		Parametro param = (Parametro) rep.findObject(null);
		
		if (param == null){
			throw new Exception("Desculpe, Ocorreu um erro ao buscar a data de última verificação do mês! =/ \nVá ao menu Configurações e seleciona a opção de Restaurar Valores.");
		}
		
		// Caso a data esteja nula ou vazia, é atualizado para a data de hoje.
		if (param.getDataUltimaVerificacaoMes() == null || param.getDataUltimaVerificacaoMes().isEmpty()){
			String data = DateUtils.now(DatePatternUtils.YYYYMMDD);
			param.setDataUltimaVerificacaoMes(data);
			rep.persistOrMerge(param);
		}
		
		return DateUtils.convertFromDefaultDatabaseFormat(param.getDataUltimaVerificacaoMes());
	}

	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 01/08/2013</p>
	 */
	public static Parametro getParametro() {
		return (Parametro) GenericRepository.getInstanceFor(Parametro.class).findObject(null);
	}

	/**
	 * @param dia
	 * <p><b>Autoria: </b>Alysson Myller - 01/08/2013</p>
	 */
	public static void alteraDiaRecebimento(Integer dia) {
		GenericRepository rep = GenericRepository.getInstanceFor(Parametro.class);
		Parametro param = (Parametro) rep.findObject(null);
		
		param.setDiaRecebimento(dia);
		rep.persistOrMerge(param);
	}

	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 01/08/2013</p>
	 */
	public static Integer getDiaRecebimento() {
		Parametro param = getParametro();
		if (param == null){
			throw new RuntimeException("Desculpe, Ocorreu um erro ao buscar a data de última verificação do mês! =/ \nVá ao menu Configurações e seleciona a opção de Restaurar Valores.");
		}
		
		if (param.getDiaRecebimento() == null || param.getDiaRecebimento().equals(0)){
			param.setDiaRecebimento(1);
			GenericRepository.getInstanceFor(Parametro.class).persistOrMerge(param);
		}
		
		return param.getDiaRecebimento();
	}

}

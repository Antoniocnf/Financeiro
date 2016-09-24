package br.com.next.liberdadefinanceirafree.service;

import java.math.BigDecimal;

import br.com.next.liberdadefinanceirafree.dao.autosql.GenericRepository;
import br.com.next.liberdadefinanceirafree.model.DebitoRecorrente;

/**
 * @author Alysson Myller
 * @since  27/07/2013
 */
public class DebitoRecorrenteService {

	/**
	 * @param idConta
	 * @param descricao
	 * @param valor
	 * <p><b>Autoria: </b>Alysson Myller - 27/07/2013</p>
	 * @param dataLimiteRecorrencia 
	 * @return 
	 */
	public static DebitoRecorrente criarDebitoRecorrente(Long idConta, String descricao, BigDecimal valor, String dataLimiteRecorrencia) {
		if (idConta == null || descricao == null || descricao.isEmpty() || valor == null){
			throw new RuntimeException("É obrigatório informar os dados para cadastro de débito recorrente");
		}
		
		DebitoRecorrente debitoRecorrente = new DebitoRecorrente();
		debitoRecorrente.setDescricao(descricao);
		debitoRecorrente.setIdConta(idConta);
		debitoRecorrente.setValor(valor);
		debitoRecorrente.setDataLimite(dataLimiteRecorrencia);
		
		GenericRepository rep = GenericRepository.getInstanceFor(DebitoRecorrente.class);
		rep.persistOrMerge(debitoRecorrente);
		return debitoRecorrente;
	}

	/**
	 * @param id
	 * <p><b>Autoria: </b>Alysson Myller - 27/07/2013</p>
	 */
	public static void removerPorId(Long id) {
		GenericRepository rep = GenericRepository.getInstanceFor(DebitoRecorrente.class);
		rep.remove(" ID_DEBITO_RECORRENTE = " + id);
	}

}

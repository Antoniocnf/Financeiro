package br.com.next.liberdadefinanceirafree.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.next.liberdadefinanceirafree.dao.autosql.GenericRepository;
import br.com.next.liberdadefinanceirafree.model.Conta;
import br.com.next.liberdadefinanceirafree.model.ContaMesDTO;
import br.com.next.liberdadefinanceirafree.util.PrecoUtils;

/**
 * @author Alysson Myller
 * @since 12/06/2013
 */
public class ContaMesService {

	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 12/06/2013</p>
	 */
	@SuppressWarnings("unchecked")
	public static List<ContaMesDTO> getContasMesAtual() {
		BigDecimal valorTotalCreditoMes = ContaService.buscarValorTotalCreditoMesAtual();
		List<ContaMesDTO> contaMesDTOs = new ArrayList<ContaMesDTO>();

		List<Conta> contas = (List<Conta>) GenericRepository.getInstanceFor(Conta.class).findList(null);
		for (Conta conta : contas){

			// Obtém o valor que é destinado à conta de acordo com a porcentagem da conta
			BigDecimal valorTotalCreditoConta = PrecoUtils.getValorTotalContaMes(valorTotalCreditoMes, conta.getPercentualMes());
			
			// Obtém o valor total de débito da conta
			BigDecimal valorTotalDebito = ContaService.buscarValorTotalDebitoContaMesAtual(conta.getId());
			
			// Subtrai o valor que era destinado à conta do valor total do débito
			BigDecimal valorRestanteConta = valorTotalCreditoConta.subtract(valorTotalDebito);
			
			// Adiciona na lista de DTO
			contaMesDTOs.add(createContaMesDTO(conta, valorRestanteConta));
		}
		
		return contaMesDTOs;
	}
	
	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 26/07/2013</p>
	 */
	@SuppressWarnings("unchecked")
	public static List<ContaMesDTO> getContasMesPassado() {
		BigDecimal valorTotalCreditoMes = ContaService.buscarValorTotalCreditoMesPassado();
		List<ContaMesDTO> contaMesDTOs = new ArrayList<ContaMesDTO>();

		List<Conta> contas = (List<Conta>) GenericRepository.getInstanceFor(Conta.class).findList(null);
		for (Conta conta : contas){

			// Obtém o valor que é destinado à conta de acordo com a porcentagem da conta
			BigDecimal valorTotalCreditoConta = PrecoUtils.getValorTotalContaMes(valorTotalCreditoMes, conta.getPercentualMes());
			
			// Obtém o valor total de débito da conta
			BigDecimal valorTotalDebito = ContaService.buscarValorTotalDebitoContaMesPassado(conta.getId());
			
			// Subtrai o valor que era destinado à conta do valor total do débito
			BigDecimal valorRestanteConta = valorTotalCreditoConta.subtract(valorTotalDebito);
			
			// Adiciona na lista de DTO
			contaMesDTOs.add(createContaMesDTO(conta, valorRestanteConta));
		}
		
		return contaMesDTOs;
	}

	
	/**
	 * @param conta
	 * @param valorRestanteConta
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 14/06/2013</p>
	 */
	private static ContaMesDTO createContaMesDTO(Conta conta, BigDecimal valorRestanteConta) {
		ContaMesDTO contaMesDTO = new ContaMesDTO();
		contaMesDTO.setIdConta(conta.getId());
		contaMesDTO.setDescricao(conta.getDescricao());
		contaMesDTO.setValorRestante(valorRestanteConta);
		contaMesDTO.setTipoConta(conta.getTipoConta());
		return contaMesDTO;
	}

}

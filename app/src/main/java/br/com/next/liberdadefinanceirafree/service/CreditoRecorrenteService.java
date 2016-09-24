package br.com.next.liberdadefinanceirafree.service;

import java.math.BigDecimal;

import br.com.next.liberdadefinanceirafree.dao.autosql.GenericRepository;
import br.com.next.liberdadefinanceirafree.model.CreditoRecorrente;

/**
 * Created by aramtorian on 22/09/16.
 */
public class CreditoRecorrenteService {

    public static CreditoRecorrente criarCreditoRecorrente(String descricao, BigDecimal valor, String dataLimiteRecorrencia) {
        if (descricao == null || descricao.isEmpty() || valor == null || dataLimiteRecorrencia == null){
            throw new RuntimeException("É obrigatório informar os dados para cadastro de débito recorrente");
        }

        CreditoRecorrente creditoRecorrente = new CreditoRecorrente();
        creditoRecorrente.setDataLimite(dataLimiteRecorrencia);
        creditoRecorrente.setDescricao(descricao);
        creditoRecorrente.setValor(valor);

        GenericRepository rep = GenericRepository.getInstanceFor(CreditoRecorrente.class);
        rep.persistOrMerge(creditoRecorrente);
        return creditoRecorrente;
    }

}

package br.com.next.liberdadefinanceirafree.service;

import java.util.Date;

import br.com.next.liberdadefinanceirafree.dao.autosql.GenericRepository;
import br.com.next.liberdadefinanceirafree.model.ParametroChaveValor;
import br.com.next.liberdadefinanceirafree.system.Constantes;
import br.com.next.liberdadefinanceirafree.util.DateUtils;

/**
 * Created by aramtorian on 16/09/16.
 */
public class ParametroChaveValorService {

    public static void criarParametroInicioSistema() {
        ParametroChaveValor p = new ParametroChaveValor();
        p.setChave(Constantes.CHAVE_DATA_INICIO_SISTEMA);
        p.setValor(DateUtils.convertToDefaultDatabaseFormat(new Date()));

        GenericRepository param = GenericRepository.getInstanceFor(ParametroChaveValor.class);
        param.persistOrMerge(p);
    }

    public static int getDiasDesdeOInicioDoSistema() throws Exception {
        ParametroChaveValor param = (ParametroChaveValor) GenericRepository.getInstanceFor(ParametroChaveValor.class).findObject(" chave = '" + Constantes.CHAVE_DATA_INICIO_SISTEMA + "'");
        Date dataInicio = DateUtils.convertFromDefaultDatabaseFormat(param.getValor());
        Date dataHoje = new Date();
        return DateUtils.getDiffDays(dataInicio, dataHoje);
    }
}

package br.com.next.liberdadefinanceirafree.service;

import android.database.Cursor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.next.liberdadefinanceirafree.dao.autosql.GenericRepository;
import br.com.next.liberdadefinanceirafree.model.BalancoMensalDTO;
import br.com.next.liberdadefinanceirafree.model.BalancoPorContaDTO;

/**
 * @author Alysson Myller
 * @since  13/07/2013
 */
public class BalancoService {

	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 13/07/2013</p>
	 */
	public static List<BalancoMensalDTO> buscarTodos() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select ");
		sql.append(" 	cast(strftime('%Y', c.data_movimento) as integer) as ano, "); 
	    sql.append(" 	cast(strftime('%m', c.data_movimento) as integer) as mes, "); 
	    sql.append(" 	sum(c.valor) as valor_credito, ");
	    sql.append(" 	w.valor_debito as valor_debito ");
	    
	    sql.append(" from credito c ");
	    sql.append(" left join 		(	select ");
        sql.append(" 						strftime('%Y', d.data_movimento) as ano, "); 
        sql.append(" 						strftime('%m', d.data_movimento) as mes, "); 
        sql.append(" 						sum(d.valor) as valor_debito ");
        sql.append(" 				  	from debito d ");
        sql.append(" 					group by strftime('%Y', d.data_movimento), strftime('%m', d.data_movimento)		) w ");
        
        sql.append(" on ifnull(strftime('%Y', c.data_movimento),0) = w.ano ");
        sql.append(" and ifnull(strftime('%m', c.data_movimento),0) = w.mes ");

		sql.append(" where strftime('%Y-%m', c.data_movimento) <= strftime('%Y-%m', 'now') ");

		sql.append(" group by strftime('%Y', c.data_movimento), strftime('%m', c.data_movimento) ");
        sql.append(" order by strftime('%Y', c.data_movimento) desc , strftime('%m', c.data_movimento) desc; ");
        
        GenericRepository rep = GenericRepository.getInstanceFor(Void.class);
        Cursor cursor = rep.rawQuery(sql.toString());
        
        if (cursor == null || !cursor.moveToFirst()){
        	return new ArrayList<BalancoMensalDTO>();
        }
        
        List<BalancoMensalDTO> balancoMensalDTOs = new ArrayList<BalancoMensalDTO>();
        while (!cursor.isAfterLast()){
        	BalancoMensalDTO balancoMensalDTO = new BalancoMensalDTO();
        	balancoMensalDTO.setAno(cursor.getInt(0));
        	balancoMensalDTO.setMes(cursor.getInt(1));
        	balancoMensalDTO.setValorCredito(new BigDecimal(cursor.getFloat(2)));
        	balancoMensalDTO.setValorDebito(new BigDecimal(cursor.getFloat(3)));
        	
        	balancoMensalDTOs.add(balancoMensalDTO);
        	cursor.moveToNext();
        }
        
        return balancoMensalDTOs;
	}

	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 13/07/2013</p>
	 */
	public static List<BalancoMensalDTO> buscarTodosOrdemCrescente() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select ");
		sql.append(" 	cast(strftime('%Y', c.data_movimento) as integer) as ano, ");
		sql.append(" 	cast(strftime('%m', c.data_movimento) as integer) as mes, ");
		sql.append(" 	sum(c.valor) as valor_credito, ");
		sql.append(" 	w.valor_debito as valor_debito ");

		sql.append(" from credito c ");
		sql.append(" left join 		(	select ");
		sql.append(" 						strftime('%Y', d.data_movimento) as ano, ");
		sql.append(" 						strftime('%m', d.data_movimento) as mes, ");
		sql.append(" 						sum(d.valor) as valor_debito ");
		sql.append(" 				  	from debito d ");
		sql.append(" 					group by strftime('%Y', d.data_movimento), strftime('%m', d.data_movimento)		) w ");

		sql.append(" on ifnull(strftime('%Y', c.data_movimento),0) = w.ano ");
		sql.append(" and ifnull(strftime('%m', c.data_movimento),0) = w.mes ");

		sql.append(" where strftime('%Y-%m', c.data_movimento) <= strftime('%Y-%m', 'now') ");

		sql.append(" group by strftime('%Y', c.data_movimento), strftime('%m', c.data_movimento) ");
		sql.append(" order by strftime('%Y', c.data_movimento) asc , strftime('%m', c.data_movimento) asc; ");

		GenericRepository rep = GenericRepository.getInstanceFor(Void.class);
		Cursor cursor = rep.rawQuery(sql.toString());

		if (cursor == null || !cursor.moveToFirst()){
			return new ArrayList<BalancoMensalDTO>();
		}

		List<BalancoMensalDTO> balancoMensalDTOs = new ArrayList<BalancoMensalDTO>();
		while (!cursor.isAfterLast()){
			BalancoMensalDTO balancoMensalDTO = new BalancoMensalDTO();
			balancoMensalDTO.setAno(cursor.getInt(0));
			balancoMensalDTO.setMes(cursor.getInt(1));
			balancoMensalDTO.setValorCredito(new BigDecimal(cursor.getFloat(2)));
			balancoMensalDTO.setValorDebito(new BigDecimal(cursor.getFloat(3)));

			balancoMensalDTOs.add(balancoMensalDTO);
			cursor.moveToNext();
		}

		return balancoMensalDTOs;
	}

	/**
	 * @param ano
	 * @param mes
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 19/08/2013</p>
	 */
	public static List<BalancoPorContaDTO> buscarResumoMensalPorContaPorAnoEMes(int ano, int mes){
		// Obtém a data informada com o dia primeiro
		String data = String.format("%04d-%02d-%02d", ano, mes, 1);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select c.tipo_conta, c.descricao, sum(dr.valor) ");
		sql.append("   from conta c, debito dr ");
		sql.append("  where c.id_conta = dr.id_conta ");
		sql.append("    and strftime('%Y-%m', dr.DATA_MOVIMENTO) = strftime('%Y-%m', '").append(data).append("') ");
		sql.append("  group by dr.id_conta ; ");
		
		GenericRepository rep = GenericRepository.getInstanceFor(Void.class);
		Cursor cursor = rep.rawQuery(sql.toString());
		
		if (cursor == null || !cursor.moveToFirst()){
			return new ArrayList<BalancoPorContaDTO>();
		} 
		
		List<BalancoPorContaDTO> resumos = new ArrayList<BalancoPorContaDTO>();
		while(! cursor.isAfterLast()){
			BalancoPorContaDTO resumo = new BalancoPorContaDTO();
			resumo.setTipoConta(cursor.getInt(0));
			resumo.setDescricaoConta(cursor.getString(1));
			resumo.setValorTotalDebito(new BigDecimal(cursor.getFloat(2)));
			
			resumos.add(resumo);
			cursor.moveToNext();
		}
		
		return resumos;
	}
}

package br.com.next.liberdadefinanceirafree.service;

import android.database.Cursor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.next.liberdadefinanceirafree.dao.autosql.GenericRepository;
import br.com.next.liberdadefinanceirafree.model.Movimentacao;

/**
 * @author Alysson Myller
 * @since  15/07/2013
 */
public class MovimentacaoService {

	/**
	 * @param ano
	 * @param mes
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 15/07/2013</p>
	 */
	public static List<Movimentacao> buscarPorAnoEMes(Integer ano, Integer mes) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select c.descricao as descricao, c.valor as valor, 1 as tipo_movimentacao, c.data_movimento as data ");
		sql.append(" from credito c ");
		sql.append(" where strftime('%Y', c.data_movimento) = '").append(ano).append("' ");
		sql.append(" and strftime('%m', c.data_movimento) = '").append(String.format("%02d", mes)).append("' ");

		sql.append(" union all ");
		
		sql.append(" select d.descricao as descricao, d.valor as valor, 2 as tipo_movimentacao, d.data_movimento as data ");
		sql.append(" from debito d ");
		sql.append(" where strftime('%Y', d.data_movimento) = '").append(ano).append("' ");
		sql.append(" and strftime('%m', d.data_movimento) = '").append(String.format("%02d", mes)).append("' ");
		sql.append(" order by data desc ");
		
		GenericRepository rep = GenericRepository.getInstanceFor(Void.class);
		Cursor cursor = rep.rawQuery(sql.toString());
		
		if (cursor == null || !cursor.moveToFirst()){
			return new ArrayList<Movimentacao>();
		}
		
		List<Movimentacao> movimentacoes = new ArrayList<Movimentacao>();
		while (!cursor.isAfterLast()){
			Movimentacao movimentacao = new Movimentacao();
			movimentacao.setDescricao(cursor.getString(0));
			movimentacao.setValor(new BigDecimal(cursor.getFloat(1)));
			movimentacao.setTipoMovimentacao(cursor.getInt(2));
			movimentacao.setData(cursor.getString(3));
			
			movimentacoes.add(movimentacao);			
			cursor.moveToNext();
		}
		
		// Ordena por data de movimentação (criação)
//		Collections.sort(movimentacoes);
		return movimentacoes;
	}
	
	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 23/08/2013</p>
	 */
	public static List<Movimentacao> buscarTodas() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select c.descricao as descricao, c.valor as valor, 1 as tipo_movimentacao, c.data_movimento ");
		sql.append(" , 'false' as debitoRecorrente ");
		sql.append(" , 0 as tipoConta ");
		sql.append(" from credito c ");
		
		sql.append(" union all ");
		
		sql.append(" select d.descricao as descricao, d.valor as valor, 2 as tipo_movimentacao, d.data_movimento ");
		sql.append(" , (case when d.id_debito_recorrente is null then 'false' else 'true' end) as debitoRecorrente ");
		sql.append(" , c.tipo_conta ");
		sql.append(" from debito d ");
		sql.append(" ,    conta c ");
		sql.append(" where d.id_conta = c.id_conta ; ");
		
		GenericRepository rep = GenericRepository.getInstanceFor(Void.class);
		Cursor cursor = rep.rawQuery(sql.toString());
		
		if (cursor == null || !cursor.moveToFirst()){
			return new ArrayList<Movimentacao>();
		}
		
		List<Movimentacao> movimentacoes = new ArrayList<Movimentacao>();
		while (!cursor.isAfterLast()){
			Movimentacao movimentacao = new Movimentacao();
			movimentacao.setDescricao(cursor.getString(0));
			movimentacao.setValor(new BigDecimal(cursor.getFloat(1)));
			movimentacao.setTipoMovimentacao(cursor.getInt(2));
			movimentacao.setData(cursor.getString(3));
			movimentacao.setEhDebitoRecorrente("true".equals(cursor.getString(4)));
			movimentacao.setTipoConta(cursor.getInt(5));
			
			movimentacoes.add(movimentacao);			
			cursor.moveToNext();
		}
		
		return movimentacoes;
	}

	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 23/08/2013</p>
	 */
	public static Map<String, List<Movimentacao>> getMapDataXMovimentacoes(){
		List<Movimentacao> movimentacoes = buscarTodas();
		Collections.sort(movimentacoes);
		
		Map<String, List<Movimentacao>> mapDataXMovimentacoes = new HashMap<String, List<Movimentacao>>();
		for (Movimentacao e : movimentacoes){
		    String key = getSomenteAnoEMes(e.getData());
		    List<Movimentacao> values = mapDataXMovimentacoes.get(key);
		    if (values == null){
		        values = new ArrayList<Movimentacao>();
		    }
		    values.add(e);
		    mapDataXMovimentacoes.put(key, values);
		}
		
		return mapDataXMovimentacoes;
	}
	
	/**
	 * @param data
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 26/08/2013</p>
	 */
	private static String getSomenteAnoEMes(String data){
		return data != null && !data.isEmpty() ? data.substring(0, data.length() - 3) : "";
	}
	
}

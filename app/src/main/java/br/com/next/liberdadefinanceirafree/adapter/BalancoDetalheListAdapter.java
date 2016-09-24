package br.com.next.liberdadefinanceirafree.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.next.liberdadefinanceirafree.R;
import br.com.next.liberdadefinanceirafree.enums.TipoMovimentacao;
import br.com.next.liberdadefinanceirafree.model.Movimentacao;
import br.com.next.liberdadefinanceirafree.util.DateUtils;
import br.com.next.liberdadefinanceirafree.util.PrecoUtils;

/**
 * @author Alysson Myller
 * @since  15/07/2013
 */
public class BalancoDetalheListAdapter extends GenericListAdapter<Movimentacao>{

	private Context context;
	
	public BalancoDetalheListAdapter(Context context, List<Movimentacao> list) {
		super(list);
		this.context = context;
	}
	
	private class ViewHolder {
		TextView txtViewDescricao;
		TextView txtViewValor;
		TextView txtViewData;
	}

	@Override
	protected View getView(List<Movimentacao> list, int position, View convertView, ViewGroup parent) throws Exception {
		ViewHolder viewHolder;
		
		if (convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.balanco_detalhe_item, null);
			
			viewHolder = new ViewHolder();
			viewHolder.txtViewDescricao = (TextView) convertView.findViewById(R.id.txtViewBalancoDetalheItemDescricao);
			viewHolder.txtViewValor = (TextView) convertView.findViewById(R.id.txtViewBalancoDetalheItemValor);
			viewHolder.txtViewData = (TextView) convertView.findViewById(R.id.txtViewBalancoDetalheItemData);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		Movimentacao movimentacao = list.get(position);
		viewHolder.txtViewDescricao.setText(movimentacao.getDescricao());
		viewHolder.txtViewValor.setText(PrecoUtils.formatoPadrao(movimentacao.getValor()));
		
		if (TipoMovimentacao.DEBITO.getValor().equals(movimentacao.getTipoMovimentacao())){
			viewHolder.txtViewValor.setTextColor(ContextCompat.getColor(context, R.color.red));
		} else {
			viewHolder.txtViewValor.setTextColor(ContextCompat.getColor(context, R.color.green));
		}

		viewHolder.txtViewData.setText(DateUtils.convertFromDataBase(movimentacao.getData()));
		
		return convertView;
	}

}

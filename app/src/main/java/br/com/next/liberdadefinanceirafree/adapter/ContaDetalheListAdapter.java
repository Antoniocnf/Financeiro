package br.com.next.liberdadefinanceirafree.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import br.com.next.liberdadefinanceirafree.R;
import br.com.next.liberdadefinanceirafree.model.Debito;
import br.com.next.liberdadefinanceirafree.util.DateUtils;
import br.com.next.liberdadefinanceirafree.util.PrecoUtils;

/**
 * @author Alysson Myller
 * @since  10/06/2013
 */
public class ContaDetalheListAdapter extends GenericListAdapter<Debito> {

	private Context context;
	private Typeface normalTypeface;
	
	public ContaDetalheListAdapter(Context context, List<Debito> list) {
		super(list);
		this.context = context;
		this.normalTypeface = getRegularTypeface(context);
	}

	private static class ViewHolder {
		ImageView imageViewIcon;
		TextView txtViewDescricao;
		TextView txtViewData;
		TextView txtViewValor;
	}
	
	@Override
	protected View getView(List<Debito> list, int position, View convertView, ViewGroup parent) throws Exception {
		ViewHolder viewHolder;
		
		if (convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.conta_detalhe_item, null);
			
			viewHolder = new ViewHolder();
			viewHolder.imageViewIcon = (ImageView) convertView.findViewById(R.id.imageViewDetalheItemClockIcon);
			viewHolder.txtViewDescricao = (TextView) convertView.findViewById(R.id.txtViewContaDetalheItemDescricao);
			viewHolder.txtViewData = (TextView) convertView.findViewById(R.id.txtViewContaDetalheItemData);
			viewHolder.txtViewValor = (TextView) convertView.findViewById(R.id.txtViewContaDetalheItemValor);
					
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		Debito debito = list.get(position);
		viewHolder.txtViewDescricao.setText(debito.getDescricao());
		viewHolder.txtViewDescricao.setTypeface(normalTypeface);

		Date dataMovimentacao = DateUtils.convertFromDefaultDatabaseFormat(debito.getDataMovimento());
		viewHolder.txtViewData.setText(DateUtils.getDescricaoReduzidaDiaMes(dataMovimentacao));
		viewHolder.txtViewData.setTypeface(normalTypeface);

		viewHolder.txtViewValor.setText(PrecoUtils.formatoPadrao(debito.getValor()));

		// Caso a data de movimentação seja antes de hoje, exibe o ícone de relógio
		if (DateUtils.ehData1AntesData2(new Date(), dataMovimentacao)){
			viewHolder.imageViewIcon.setVisibility(View.VISIBLE);
			viewHolder.txtViewValor.setTextColor(ContextCompat.getColor(context, R.color.blue_navy));
		} else {
			viewHolder.imageViewIcon.setVisibility(View.GONE);
			viewHolder.txtViewValor.setTextColor(ContextCompat.getColor(context, R.color.red));
		}
		
		return convertView;
	}

}

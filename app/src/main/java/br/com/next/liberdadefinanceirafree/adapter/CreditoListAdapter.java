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
import br.com.next.liberdadefinanceirafree.enums.TipoCredito;
import br.com.next.liberdadefinanceirafree.model.Credito;
import br.com.next.liberdadefinanceirafree.util.DateUtils;
import br.com.next.liberdadefinanceirafree.util.PrecoUtils;

/**
 * @author Alysson Myller
 * @since  21/06/2013
 */
public class CreditoListAdapter extends GenericListAdapter<Credito> {

	private Context context;
	private Typeface normalTypeface;
	
	public CreditoListAdapter(Context context, List<Credito> list) {
		super(list);
		this.context = context;
		this.normalTypeface = getRegularTypeface(context);
	}
	
	private class ViewHolder {
		ImageView imageViewClock;
		TextView txtViewDescricao;
		TextView txtViewData;
		TextView txtViewValor;
	}

	@Override
	protected View getView(List<Credito> list, int position, View convertView, ViewGroup parent) throws Exception {
		ViewHolder viewHolder;
		
		if (convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.creditos_item, null);
			
			viewHolder = new ViewHolder();
			viewHolder.imageViewClock = (ImageView) convertView.findViewById(R.id.imageViewCreditosItemClockIcon);
			viewHolder.txtViewDescricao = (TextView) convertView.findViewById(R.id.txtViewCreditoItemDescricao);
			viewHolder.txtViewData = (TextView) convertView.findViewById(R.id.txtViewCreditoItemData);
			viewHolder.txtViewValor = (TextView) convertView.findViewById(R.id.txtViewCreditoItemValor);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		Credito credito = list.get(position);
		viewHolder.txtViewDescricao.setText(getDescricaoCredito(credito));
		viewHolder.txtViewDescricao.setTypeface(normalTypeface);

		Date dataMovimentacao = DateUtils.convertFromDefaultDatabaseFormat(credito.getDataMovimento());
		viewHolder.txtViewData.setText(DateUtils.getDescricaoReduzidaDiaMes(dataMovimentacao));
		viewHolder.txtViewData.setTypeface(normalTypeface);

		viewHolder.txtViewValor.setText(PrecoUtils.formatoPadrao(credito.getValor()));

		// Caso a data de movimentação seja antes de hoje, exibe o ícone de relógio
		if (DateUtils.ehData1AntesData2(new Date(), dataMovimentacao)){
			viewHolder.imageViewClock.setVisibility(View.VISIBLE);
			viewHolder.txtViewValor.setTextColor(ContextCompat.getColor(context, R.color.blue_navy));
		} else {
			viewHolder.imageViewClock.setVisibility(View.GONE);
			viewHolder.txtViewValor.setTextColor(ContextCompat.getColor(context, R.color.green));
		}

		return convertView;
	}

	/**
	 * @param credito
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 12/08/2013</p>
	 */
	private String getDescricaoCredito(Credito credito) {
		switch (TipoCredito.getInstance(credito.getTipoCredito())) {
		case NOVO_CREDITO:
			// Novo crédito são créditos que o usuário cria, ou seja, deve vir com 
			// a descrição que o usuário escreveu.
			return credito.getDescricao();
			
		case RENDIMENTO_MENSAL:
			return context.getString(R.string.descricao_tipo_credito_rendimento_mensal);
			
		case RESTANTE_MES_PASSADO:
			return context.getString(R.string.descricao_tipo_credito_restante_mes_passado);

		default:
			return credito.getDescricao();
		}
	}

}

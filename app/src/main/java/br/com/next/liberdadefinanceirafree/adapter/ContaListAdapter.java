package br.com.next.liberdadefinanceirafree.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;

import br.com.next.liberdadefinanceirafree.R;
import br.com.next.liberdadefinanceirafree.enums.TipoConta;
import br.com.next.liberdadefinanceirafree.model.ContaMesDTO;
import br.com.next.liberdadefinanceirafree.service.ContaService;
import br.com.next.liberdadefinanceirafree.util.PrecoUtils;

/**
 * @author Alysson Myller
 * @since  10/06/2013
 */
public class ContaListAdapter extends GenericListAdapter<ContaMesDTO> {

	private Context context;
	private Typeface normalTypeface;
	private Typeface titleTypeface;
	
	public ContaListAdapter(Context context, List<ContaMesDTO> list) {
		super(list);
		this.context = context;
		this.titleTypeface = getRegularTypeface(context);
		this.normalTypeface = getLightTypeface(context);
	}

	private static class ViewHolder {
		TextView txtViewDescricao;
		TextView txtViewValor;
		ImageView imgViewIcone;
	}
	
	@Override
	protected View getView(List<ContaMesDTO> list, int position, View convertView, ViewGroup parent) throws Exception {
		ViewHolder viewHolder;
		
		if (convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.contas_item, null);
			
			viewHolder = new ViewHolder();
			viewHolder.imgViewIcone = (ImageView) convertView.findViewById(R.id.imageViewContasItemIcone);
			viewHolder.txtViewDescricao = (TextView) convertView.findViewById(R.id.txtViewContasItemNome);
			viewHolder.txtViewValor = (TextView) convertView.findViewById(R.id.txtViewContasItemValor);
					
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		ContaMesDTO conta = list.get(position);
		viewHolder.txtViewDescricao.setText(ContaService.getDescricaoNomeConta(context, conta.getTipoConta()));
		viewHolder.txtViewDescricao.setTypeface(normalTypeface);
		
		viewHolder.txtViewValor.setText(PrecoUtils.formatoPadrao(conta.getValorRestante()));
		viewHolder.txtViewValor.setTypeface(titleTypeface);
		
		if (conta.getValorRestante().compareTo(BigDecimal.ZERO) < 0){
			viewHolder.txtViewValor.setTextColor(Color.RED);
		}

		switch (TipoConta.getInstance(conta.getTipoConta())){
			case LIBERDADE_FINANCEIRA:
				viewHolder.imgViewIcone.setImageResource(R.drawable.ic_heart);
				break;

			case DIVERSAO:
				viewHolder.imgViewIcone.setImageResource(R.drawable.ic_balloons);
				break;

			case POUPANCA:
				viewHolder.imgViewIcone.setImageResource(R.drawable.ic_piggy);
				break;

			case INSTRUCAO_FINANCEIRA:
				viewHolder.imgViewIcone.setImageResource(R.drawable.ic_open_book);
				break;

			case NECESSIDADES_BASICAS:
				viewHolder.imgViewIcone.setImageResource(R.drawable.ic_knife_fork);
				break;

			case DOACOES:
				viewHolder.imgViewIcone.setImageResource(R.drawable.ic_donation);
				break;
		}

		return convertView;
	}

}

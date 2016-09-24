package br.com.next.liberdadefinanceirafree.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import br.com.next.liberdadefinanceirafree.R;
import br.com.next.liberdadefinanceirafree.model.BalancoMensalDTO;
import br.com.next.liberdadefinanceirafree.util.DateUtils;
import br.com.next.liberdadefinanceirafree.util.PrecoUtils;

/**
 * @author Alysson Myller
 * @since  13/07/2013
 */
public class BalancoListAdapter extends GenericListAdapter<BalancoMensalDTO> {

	private Context context;
	private Typeface normalTypeface;
	private Typeface titleTypeface;
	
	public BalancoListAdapter(Context context, List<BalancoMensalDTO> list) {
		super(list);
		this.context = context;
		this.normalTypeface = getLightTypeface(context);
		this.titleTypeface = getRegularTypeface(context);
	}

	private class ViewHolder {
		TextView txtViewMes;
		TextView txtViewValor;
	}
	
	@Override
	protected View getView(List<BalancoMensalDTO> list, int position, View convertView, ViewGroup parent) throws Exception {
		ViewHolder viewHolder;
		
		if (convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.balanco_item, null);
			
			viewHolder = new ViewHolder();
			viewHolder.txtViewMes = (TextView) convertView.findViewById(R.id.txtViewResumoItemMes);
			viewHolder.txtViewValor = (TextView) convertView.findViewById(R.id.txtViewResumoItemValor);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		BalancoMensalDTO resumo = list.get(position);
		
		Calendar cal = Calendar.getInstance();
		cal.set(resumo.getAno(), resumo.getMes() - 1, 1);
		viewHolder.txtViewMes.setText(DateUtils.getDescricaoMesEAno(cal.getTime()));
		viewHolder.txtViewMes.setTypeface(normalTypeface);
		
		BigDecimal resultado = resumo.getValorCredito().subtract(resumo.getValorDebito());
		String valorFinal = PrecoUtils.formatoPadrao(resultado);
		viewHolder.txtViewValor.setText(valorFinal);
		viewHolder.txtViewValor.setTypeface(titleTypeface);
		
		if (resultado.compareTo(BigDecimal.ZERO) < 0){
			viewHolder.txtViewValor.setTextColor(Color.RED);
		}
		
		return convertView;
	}

}

package br.com.next.liberdadefinanceirafree.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.next.liberdadefinanceirafree.R;
import br.com.next.liberdadefinanceirafree.model.DebitoRecorrente;
import br.com.next.liberdadefinanceirafree.util.PrecoUtils;

/**
 * @author Alysson Myller
 * @since  27/07/2013
 */
public class DebitoRecorrenteListAdapter extends GenericListAdapter<DebitoRecorrente> {

	private Context context;
	private Typeface titleTypeface;
	private Typeface normalTypeface;
	
	public DebitoRecorrenteListAdapter(Context context, List<DebitoRecorrente> list) {
		super(list);
		this.context = context;
		this.titleTypeface = getRegularTypeface(context);
		this.normalTypeface = getLightTypeface(context);
	}

	private class ViewHolder {
		TextView txtViewDescricao;
		TextView txtViewValor;
	}
	
	@Override
	protected View getView(List<DebitoRecorrente> list, int position, View convertView, ViewGroup parent) throws Exception {
		ViewHolder viewHolder;
		
		if (convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.debito_recorrente_item, null);
			
			viewHolder = new ViewHolder();
			viewHolder.txtViewDescricao = (TextView) convertView.findViewById(R.id.txtViewDebitoRecorrenteItemDescricao);
			viewHolder.txtViewValor = (TextView) convertView.findViewById(R.id.txtViewDebitoRecorrenteItemValor);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		DebitoRecorrente debito = list.get(position);
		viewHolder.txtViewDescricao.setText(debito.getDescricao());
		viewHolder.txtViewDescricao.setTypeface(normalTypeface);
		
		viewHolder.txtViewValor.setText(PrecoUtils.formatoPadrao(debito.getValor()));
		viewHolder.txtViewValor.setTypeface(titleTypeface);
		viewHolder.txtViewValor.setTextColor(Color.RED);
		return convertView;
	}

}

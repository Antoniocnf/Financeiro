package br.com.next.liberdadefinanceirafree.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @author Alysson Myller
 * 01/07/2012
 */
public abstract class GenericListAdapter<T> extends BaseAdapter {

	private List<T> list;
	
	public GenericListAdapter (List<T> list){
		this.list = list;
	}
	
	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return list != null ? list.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	@Override
	public final View getView(int position, View convertView, ViewGroup parent) {
		try {
			return this.getView(list, position, convertView, parent);
		} catch (Throwable e) {
			// TODO Alysson - quando chega aqui, o sistema não consegue capturar a exceção 
			// e de alguma forma relança para o android.
// 			MensagemAlertaUtils.mostra(parent.getContext(), e);
		}
		return null;
	}
	
	protected abstract View getView(List<T> list, int position, View convertView, ViewGroup parent) throws Exception;

	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 10/06/2013</p>
	 */
	public Typeface getLightTypeface(Context context){
		return Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
	}
	
	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 18/06/2013</p>
	 */
	public Typeface getRegularTypeface(Context context){
		return Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
	}
	
	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 18/06/2013</p>
	 */
	public Typeface getCondensedBigTypeface(Context context){
		return Typeface.createFromAsset(context.getAssets(), "Roboto-Condensed.ttf");
	}

}

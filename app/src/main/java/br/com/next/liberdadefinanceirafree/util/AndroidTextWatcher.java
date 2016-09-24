package br.com.next.liberdadefinanceirafree.util;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import br.com.next.liberdadefinanceirafree.system.LibFinanceMessage;

/**
 * @author Alysson Myller
 * @since  07/11/2012
 */
public abstract class AndroidTextWatcher implements TextWatcher {
	
	private final Context context;
	
	public AndroidTextWatcher(Context ctx){
		this.context = ctx;
	}

	@Override
	public final void beforeTextChanged(CharSequence s, int start, int count, int after) {
		try {
			execBeforeTextChanged(s, start, count, after);
		} catch (Throwable e) {
			LibFinanceMessage.show(context, e);
		}
	}
	
	/**
	 * @param s
	 * @param start
	 * @param count
	 * @param after
	 * <p><b>Autoria: </b>Alysson Myller - 07/11/2012</p>
	 */
	protected void execBeforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public final void onTextChanged(CharSequence s, int start, int before, int count) {
		try {
			execOnTextChanged(s, start, before, count);
		} catch (Throwable e) {
			LibFinanceMessage.show(context, e);
		}
	}

	/**
	 * @param s
	 * @param start
	 * @param before
	 * @param count
	 * <p><b>Autoria: </b>Alysson Myller - 07/11/2012</p>
	 */
	protected void execOnTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public final void afterTextChanged(Editable s) {
		try {
			execAfterTextChanged(s);
		} catch (Throwable e) {
			LibFinanceMessage.show(context, e);
		}
	}

	/**
	 * @param s
	 * <p><b>Autoria: </b>Alysson Myller - 07/11/2012</p>
	 */
	protected void execAfterTextChanged(Editable s) {
	}

}

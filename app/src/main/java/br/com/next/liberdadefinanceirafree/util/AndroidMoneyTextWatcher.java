package br.com.next.liberdadefinanceirafree.util;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.widget.EditText;

/**
 * @author Alysson Myller
 * @since  08/11/2012
 */
public class AndroidMoneyTextWatcher extends AndroidTextWatcher {

	private EditText editText;
	
	public AndroidMoneyTextWatcher(Context ctx, EditText editText) {
		super(ctx);
		this.editText = editText;
	}
	
	@Override
	protected void execOnTextChanged(CharSequence s, int start, int before, int count) {
		if(!s.toString().matches("^\\$(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$")) {
            String userInput= ""+s.toString().replaceAll("[^\\d]", "");
            StringBuilder cashAmountBuilder = new StringBuilder(userInput);

            while (cashAmountBuilder.length() > 3 && cashAmountBuilder.charAt(0) == '0') {
                cashAmountBuilder.deleteCharAt(0);
            }
            while (cashAmountBuilder.length() < 3) {
                cashAmountBuilder.insert(0, '0');
            }
            cashAmountBuilder.insert(cashAmountBuilder.length()-2, '.');
            cashAmountBuilder.insert(0, '$');

            editText.setTextKeepState(cashAmountBuilder.toString());
            Selection.setSelection(editText.getText(), cashAmountBuilder.toString().length());
        }
	}

	@Override
	protected void execAfterTextChanged(Editable s) {
	}
	
	@Override
	protected void execBeforeTextChanged(CharSequence s, int start, int count, int after) {
	}
}

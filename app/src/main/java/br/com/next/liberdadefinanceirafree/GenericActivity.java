package br.com.next.liberdadefinanceirafree;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.math.BigDecimal;
import java.math.RoundingMode;

import br.com.next.liberdadefinanceirafree.dao.autosql.GenericRepository;
import br.com.next.liberdadefinanceirafree.system.LibFinanceMessage;

/**
 * Essa classe é quase a mesma classe CommonsActivity, porém, ela já trata
 * exceções lançadas das outras Activities. Não alterei a CommonsActivity pois a alteração
 * seria muito grande, então criei esta para ir "transferindo" a subimplementação aos poucos
 * 
 * @author Alysson Myller
 * @since  15/04/2013
 */
public class GenericActivity extends AppCompatActivity {

	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			GenericRepository.setContext(getApplicationContext());
			this.create(savedInstanceState);
		} catch (Throwable e) {
			LibFinanceMessage.show(this, e);
		}
	}
	
	/**
	 * @throws Exception 
	 * @see {@link #onCreate(Bundle)} 
	 * <p><b>Autoria: </b>Alysson Myller - 25/10/2012</p>
	 */
	protected void create(Bundle savedInstaceState) throws Exception{
	}
	
	@Override
	protected final void onResume() {
		try{
			super.onResume();
			this.resume();
		}catch (Throwable e) {
			LibFinanceMessage.show(this, e);
		}
	}
	
	/**
	 * @throws Exception 
	 * @see {@link #onResume()} 
	 * <p><b>Autoria: </b>Alysson Myller - 03/10/2012</p>
	 */
	protected void resume() throws Exception{
	}
	
	@Override
	protected final void onPause() {
		try {
			super.onPause();
			this.pause();
		} catch (Throwable e) {
			LibFinanceMessage.show(this, e);
		}
	}
	
	/**
	 * <p><b>Autoria: </b>Alysson Myller - 06/11/2012</p>
	 */
	protected void pause() {
	}

	@Override
	public final boolean onCreateOptionsMenu(Menu menu) {
		try {
			this.createOptionsMenu(menu);
		} catch (Exception e) {
			LibFinanceMessage.show(this, e);
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * @param menu
	 * <p><b>Autoria: </b>Alysson Myller - 23/01/2013</p>
	 */
	protected void createOptionsMenu(Menu menu) {
	}

	@Override
	public final boolean onOptionsItemSelected(MenuItem item) {
		try {
			
			switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
			default:
				break;
			}
			
			optionsItemSelected(item);
		} catch (Throwable e) {
			LibFinanceMessage.show(this, e);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public final void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		try {
			createContextMenu(menu, v, menuInfo);
			super.onCreateContextMenu(menu, v, menuInfo);
		} catch (Exception e) {
			LibFinanceMessage.show(this, e);
		}
	}
	
	/**
	 * @param menu
	 * @param v
	 * @param menuInfo
	 * <p><b>Autoria: </b>Alysson Myller - 27/07/2013</p>
	 */
	protected void createContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	}
	
	@Override
	public final boolean onContextItemSelected(MenuItem item) {
		try {
			contextItemSelected(item);
		} catch (Exception e) {
			LibFinanceMessage.show(this, e);
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * @param item
	 * <p><b>Autoria: </b>Alysson Myller - 27/07/2013</p>
	 */
	protected void contextItemSelected(MenuItem item) throws Exception {
	}

	/**
	 * @param item
	 * @throws Exception
	 * <p><b>Autoria: </b>Alysson Myller - 03/10/2012</p>
	 */
	protected void optionsItemSelected(MenuItem item) throws Exception {
	}

	@Override
	protected final void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			super.onActivityResult(requestCode, resultCode, data);
			this.activityResult(requestCode, resultCode, data);
		} catch (Exception e) {
			LibFinanceMessage.show(this, e);
		}
	}
	
	/**
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 * <p><b>Autoria: </b>Alysson Myller - 28/01/2013</p>
	 */
	protected void activityResult(int requestCode, int resultCode, Intent data){
	}
	
	@Override
	public void onBackPressed() {
		try {
			this.backPressed();
			super.onBackPressed();
		} catch (Exception e) {
			LibFinanceMessage.show(this, e);
		}
	}
	
	/**
	 * <p><b>Autoria: </b>Alysson Myller - 31/01/2013</p>
	 */
	protected void backPressed(){
	}

	/**
	 * @param field
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 06/11/2012</p>
	 */
	public BigDecimal getBigDecimalValueFromEditText(EditText field){
		if (field != null && field.getText() != null && field.getText().length() > 0){
			String value = getOnlyDigits(field.getText().toString());
			return new BigDecimal(value).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_EVEN);
		} 
		return new BigDecimal(0);
	}
	
	/**
	 * Obtém somente os números do parâmetro recebido
	 * @param value
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 08/11/2012</p>
	 */
	private String getOnlyDigits(String value){
		String res = "";
		for (Character s : value.toCharArray()){
			if (Character.isDigit(s)){
				res = res + s;
			} 
		}
		return res;
	}
	
	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 10/06/2013</p>
	 */
	public Typeface getLightTypeface(){
		return Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
	}
	
	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 18/06/2013</p>
	 */
	public Typeface getRegularTypeface(){
		return Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
	}
	
	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 18/06/2013</p>
	 */
	public Typeface getCondensedTypeface(){
		return Typeface.createFromAsset(getAssets(), "Roboto-Condensed.ttf");
	}
	
	/**
	 * <p> <b>Autoria: </b>Alysson Myller - 30/07/2013</p>
	 */
	public void openKeyboard() {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.SHOW_IMPLICIT, 0);
	}

	/**
	 * <p> <b>Autoria: </b>Alysson Myller - 14/09/2016</p>
	 */
	public void openKeyboardForce(View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}

	/**
	 * <p> <b>Autoria: </b>Alysson Myller - 30/07/2013 </p>
	 */
	public void hideKeyboard() {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromInputMethod(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 02/08/2013</p>
	 */
	public int getScreenWidth(){
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	
	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 02/08/2013</p>
	 */
	public int getScreenHeight(){
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}	
}

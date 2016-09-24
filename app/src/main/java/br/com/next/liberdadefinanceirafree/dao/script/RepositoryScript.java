package br.com.next.liberdadefinanceirafree.dao.script;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import br.com.next.liberdadefinanceirafree.dao.DataBaseAccess;
import br.com.next.liberdadefinanceirafree.dao.helper.DataBaseRepositoryHelper;


/**
 * Script para gerar a base de dados do sistema no android.
 * Esse script é rodado toda vez que o aplicativo é criado ou atualizado (alterando versão da base).
 * 
 * @author thiago
 * @since 08/08/2011
 */
public class RepositoryScript {

	public RepositoryScript(Context context) {
		databaseHelper(context);
		database();
	}
	
	public synchronized DataBaseRepositoryHelper databaseHelper(Context context) {
		if(DataBaseAccess.getDataBaseHelper() == null) {
//			Log.d(Constantes.TAG_LOG_IVENDAS, "Restartando conexao do helper");
			DataBaseAccess.restartConnection(context);
		}
		return DataBaseAccess.getDataBaseHelper();
	}
	
	public synchronized SQLiteDatabase database() {
		if(DataBaseAccess.getDatabase() == null) {
//			Log.d(Constantes.TAG_LOG_IVENDAS, "Abrindo Writable Database");
			DataBaseAccess.setDatabase(DataBaseAccess.getDataBaseHelper().getWritableDatabase());	
		}
		return DataBaseAccess.getDatabase();
	}
	
	public synchronized void close() {
		if(DataBaseAccess.getDatabase() != null) {
			DataBaseAccess.getDatabase().close();
		}
		if(DataBaseAccess.getDataBaseHelper() != null) {
			DataBaseAccess.getDataBaseHelper().close();
		}
	}
}

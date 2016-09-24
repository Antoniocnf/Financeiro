package br.com.next.liberdadefinanceirafree.dao;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import br.com.next.liberdadefinanceirafree.dao.helper.DataBaseRepositoryHelper;
import br.com.next.liberdadefinanceirafree.system.Constantes;

/**
 * Classe para compartilhar a base de dados aberta com o SQLite.
 * Se tentar abrir uma conexão com a base sem fechar a primeira,
 * ocorre erros
 * 
 * @author thiago
 * @since 19/08/2011
 */
public final class DataBaseAccess extends Application {

	private static DataBaseRepositoryHelper dataBaseHelper;
	private static SQLiteDatabase database;
	
	public DataBaseAccess() {
		super();
	}

	public static DataBaseRepositoryHelper getDataBaseHelper() {
		return dataBaseHelper;
	}

	public static void setDataBaseHelper(DataBaseRepositoryHelper dataBaseHelper) {
		DataBaseAccess.dataBaseHelper = dataBaseHelper;
	}

	public static SQLiteDatabase getDatabase() {
		return database;
	}

	public static void setDatabase(SQLiteDatabase database) {
		DataBaseAccess.database = database;
	}
	
	public static void restartConnection(Context context) {
		if(getDatabase() != null && getDatabase().isOpen()) {
			getDatabase().close();
		}
		if(getDataBaseHelper() != null) {
			getDataBaseHelper().close();
		}
		
		DataBaseAccess.setDataBaseHelper(new DataBaseRepositoryHelper(context, 
				Constantes.DATABASE_NAME, Constantes.DATABASE_VERSION));
		DataBaseAccess.setDatabase(getDataBaseHelper().getWritableDatabase());
	}
}

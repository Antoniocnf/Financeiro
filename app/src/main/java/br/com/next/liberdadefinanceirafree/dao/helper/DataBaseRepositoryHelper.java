package br.com.next.liberdadefinanceirafree.dao.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.next.liberdadefinanceirafree.dao.DatabaseUpgrade;
import br.com.next.liberdadefinanceirafree.dao.script.ScriptGenerator;


/**
 * Classe para execução dos scripts de criacao da base de dados
 * 
 * @author thiago
 * @since 08/08/2011
 */
public class DataBaseRepositoryHelper extends SQLiteOpenHelper {

	public DataBaseRepositoryHelper(Context context, String dbName, int dbVersion) {
		super(context, dbName, null, dbVersion);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		ScriptGenerator generator = new ScriptGenerator();
		String[] scriptSQLCreate = generator.generateCreateScript();
		
		for (String script : scriptSQLCreate) {
			db.execSQL(script);
		}
	}

	/**
	 * NUNCA esquecer de alterar também a versão na classe Constantes.
	 * Sempre quando há alteração do banco de dados, devem ser alterados a versão 
	 * e feito a alteração na classe VersionControl.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		VersionControl svn = new VersionControl();
		if(svn.existsNewUpgrade(oldVersion)) {
			for(DatabaseUpgrade sqlCommand : svn.getUpgradesToExecute(oldVersion, newVersion)) {
				db.execSQL(sqlCommand.getSqlCommand());
			}
		}
	}
}

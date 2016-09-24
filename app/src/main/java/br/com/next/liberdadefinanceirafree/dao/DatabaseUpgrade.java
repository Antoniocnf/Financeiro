package br.com.next.liberdadefinanceirafree.dao;

/**
 * Objeto para representar uma alteração na base de dados
 * 
 * @author thiago
 * @since 02/04/2012
 */
public final class DatabaseUpgrade {

	private String sqlCommand;
	private int databaseVersion;
	private String date;
	private String systemVersion;
	
	public DatabaseUpgrade() {
		super();
	}

	public final String getSqlCommand() {
		return sqlCommand;
	}

	public final void setSqlCommand(String sqlCommand) {
		this.sqlCommand = sqlCommand;
	}

	public final int getDatabaseVersion() {
		return databaseVersion;
	}

	public final void setDatabaseVersion(int dbVersion) {
		this.databaseVersion = dbVersion;
	}

	public final String getDate() {
		return date;
	}

	public final void setDate(String date) {
		this.date = date;
	}
	
	public final void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	public final String getSystemVersion() {
		return systemVersion;
	}

	public String toString() {
		return String.format("databaseVersion: %d, sql: %s", databaseVersion, sqlCommand);
	}
}

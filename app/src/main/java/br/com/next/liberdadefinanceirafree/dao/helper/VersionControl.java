package br.com.next.liberdadefinanceirafree.dao.helper;

import java.util.Arrays;
import java.util.List;

import br.com.next.liberdadefinanceirafree.dao.DatabaseUpgrade;
import br.com.next.liberdadefinanceirafree.dao.script.ScriptGenerator;
import br.com.next.liberdadefinanceirafree.model.CreditoRecorrente;
import br.com.next.liberdadefinanceirafree.util.CollectionUtils;


/**
 * Classe para cuidar do controle de upgrades realizados na base de dados
 * 
 * @author thiago
 * @since 02/04/2012
 */
public class VersionControl {

	private List<DatabaseUpgrade> list;

	public VersionControl() {
		
		DatabaseUpgrade upgrade1 = new DatabaseUpgrade();
		upgrade1.setDate("26/07/2013");
		upgrade1.setDatabaseVersion(2);
		upgrade1.setSqlCommand("ALTER TABLE PARAMETRO ADD COLUMN DATA_ULTIMA_VERIFICACAO_MES TEXT;");
		upgrade1.setSystemVersion("1.1.0");
		
		DatabaseUpgrade upgrade2 = new DatabaseUpgrade();
		upgrade2.setDate("02/09/2013");
		upgrade2.setDatabaseVersion(3);
		upgrade2.setSqlCommand("ALTER TABLE DEBITO_RECORRENTE ADD COLUMN DATA_LIMITE TEXT;");
		upgrade2.setSystemVersion("1.2.0");

		DatabaseUpgrade upgrade3 = new DatabaseUpgrade();
		upgrade3.setDate("22/09/2016");
		upgrade3.setDatabaseVersion(4);
		upgrade3.setSqlCommand("ALTER TABLE CREDITO ADD COLUMN ID_CREDITO_RECORRENTE INTEGER;");
		upgrade3.setSystemVersion("1.2.0");

		ScriptGenerator scriptGenerator = new ScriptGenerator();

		DatabaseUpgrade upgrade4 = new DatabaseUpgrade();
		upgrade4.setDate("22/09/2016");
		upgrade4.setDatabaseVersion(4);
		upgrade4.setSqlCommand(scriptGenerator.generateCreateScript(CreditoRecorrente.class));
		upgrade4.setSystemVersion("1.2.0");
		
		list = Arrays.asList(upgrade1, upgrade2, upgrade3, upgrade4);
	}

	public boolean existsNewUpgrade(int oldVersion) {
		if (!CollectionUtils.isEmpty(list)) {
			for (DatabaseUpgrade upgrade : list) {
				if (upgrade.getDatabaseVersion() > oldVersion) {
					return true;
				}
			}
		}

		return false;
	}

	public List<DatabaseUpgrade> getUpgradesToExecute(int oldVersion, int newVersion) {
		if (!CollectionUtils.isEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				DatabaseUpgrade upgrade = list.get(i);

				if (upgrade.getDatabaseVersion() > oldVersion) {
					return list.subList(i, indexOfNewVersionOperation(newVersion));
				}
			}
		}

		return null;
	}

	private int indexOfNewVersionOperation(int newVersion) {
		if (!CollectionUtils.isEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getDatabaseVersion() > newVersion) {
					return i;
				}
			}
		}

		return list.size();
	}

	public List<DatabaseUpgrade> list() {
		return list;
	}
}
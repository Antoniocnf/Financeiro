package br.com.next.greendao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class Main {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "br.com.next.liberdadefinanceira.db");

        Entity contas = schema.addEntity("Contas"); //name of class
        contas.addIdProperty();
        contas.addStringProperty("teste");

        DaoGenerator dg = new DaoGenerator();
        dg.generateAll(schema, "./app/src/main/java");
    }
}

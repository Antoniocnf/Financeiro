package br.com.next.liberdadefinanceirafree.dao.script;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import br.com.next.liberdadefinanceirafree.dao.autosql.Wrapper;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Column;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Id;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Table;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Unique;
import br.com.next.liberdadefinanceirafree.model.Conta;
import br.com.next.liberdadefinanceirafree.model.Credito;
import br.com.next.liberdadefinanceirafree.model.CreditoRecorrente;
import br.com.next.liberdadefinanceirafree.model.Debito;
import br.com.next.liberdadefinanceirafree.model.DebitoRecorrente;
import br.com.next.liberdadefinanceirafree.model.Parametro;
import br.com.next.liberdadefinanceirafree.model.ParametroChaveValor;
import br.com.next.liberdadefinanceirafree.util.ArrayUtils;

/**
 * Classe de geração automatica de script para gerar a base de dados
 *
 * @author thiago
 * @since 12/08/2011
 */
public class ScriptGenerator {

    private static Class<?> mappedClasses[] = {Conta.class, Credito.class, Debito.class, Parametro.class, DebitoRecorrente.class, ParametroChaveValor.class, CreditoRecorrente.class};

    /**
     * Gera array de Strings para deletar as tabelas
     */
    public String generateDeleteScript() {
        StringBuilder deleteScript = new StringBuilder();

        for (Class<?> clazz : mappedClasses()) {
            if (clazz.isAnnotationPresent(Table.class)) {
                Table tabela = clazz.getAnnotation(Table.class);
                deleteScript.append(String.format("DROP TABLE IF EXISTS %s; ", tabela.name()));
            } else {
                throw new RuntimeException("A entidade nao esta anotada com anotacao Tabela");
            }
        }

        return deleteScript.toString();
    }

    /**
     * Gera script para criacao das tabelas do banco de dados
     */
    public String[] generateCreateScript() {
        List<String> list = new ArrayList<String>();

        for (Class<?> clazz : mappedClasses()) {
            Table tabela = clazz.getAnnotation(Table.class);

            StringBuilder createTable = new StringBuilder(createTable(tabela));
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column coluna = field.getAnnotation(Column.class);

                    if (field.isAnnotationPresent(Id.class)) {
                        Id id = field.getAnnotation(Id.class);
                        createTable.append(String.format("%s INTEGER PRIMARY KEY %s, ", coluna.name(), id.autoIncrement() ? "AUTOINCREMENT" : ""));
                    } else if (field.isAnnotationPresent(Unique.class)) {
                        createTable.append(addColumnDeclaration(field, coluna).replace(",", " UNIQUE,"));
                    } else {
                        createTable.append(addColumnDeclaration(field, coluna));
                    }
                } else {
                    continue;
                }
            }

            createTable.deleteCharAt(createTable.length() - 2);
            createTable.append(");");
            list.add(createTable.toString());

            if (tabela.hasTempTable()) {
                final String tempTable = createTable.toString();
                list.add(tempTable.replace("TABLE " + tabela.name(), "TABLE TMP_" + tabela.name()));
            }
        }

        return ArrayUtils.toStringArray(list);
    }

    private String createTable(Table tabela) {
        return String.format("CREATE TABLE %s(", tabela.name());
    }

    private String addColumnDeclaration(Field field, Column coluna) {
        Class<?> type = field.getType();

        if (type.isPrimitive()) {
            type = Wrapper.box(type);
        }

        String declarationType = Wrapper.sqliteWrapper(type);

        return String.format("%s %s, ", coluna.name(), declarationType);
    }

    public static Class<?>[] mappedClasses() {
        return mappedClasses;
    }

    /**
     * @param clazz <p><b>Autoria: </b>Alysson Myller - 08/11/2012</p>
     */
    public String generateCreateScript(Class<?> clazz) {
        Table tabela = clazz.getAnnotation(Table.class);

        StringBuilder createTable = new StringBuilder(createTable(tabela));
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column coluna = field.getAnnotation(Column.class);

                if (field.isAnnotationPresent(Id.class)) {
                    Id id = field.getAnnotation(Id.class);
                    createTable.append(String.format("%s INTEGER PRIMARY KEY %s, ", coluna.name(), id.autoIncrement() ? "AUTOINCREMENT" : ""));
                } else if (field.isAnnotationPresent(Unique.class)) {
                    createTable.append(addColumnDeclaration(field, coluna).replace(",", " UNIQUE,"));
                } else {
                    createTable.append(addColumnDeclaration(field, coluna));
                }
            } else {
                continue;
            }
        }

        createTable.deleteCharAt(createTable.length() - 2);
        createTable.append(");");
        return createTable.toString();
    }

}

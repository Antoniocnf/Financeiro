package br.com.next.liberdadefinanceirafree.model;

import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Column;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Id;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Table;

/**
 * Created by aramtorian on 16/09/16.
 */
@Table(name = "PARAMETRO_CHAVE_VALOR")
public class ParametroChaveValor {

    @Id
    @Column(name = "ID_PARAMETRO_CHAVE_VALOR")
    private Long id;

    @Column(name = "CHAVE")
    private String chave;

    @Column(name = "VALOR")
    private String valor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}

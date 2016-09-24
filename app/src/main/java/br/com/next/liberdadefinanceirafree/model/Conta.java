package br.com.next.liberdadefinanceirafree.model;


import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Column;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Id;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Table;

/**
 * @author Alysson Myller
 * @since 10/06/2013
 */
@Table(name = "CONTA")
public class Conta {

    @Id
    @Column(name = "ID_CONTA")
    private Long id;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "PERCENTUAL_MES")
    private Short percentualMes;

    @Column(name = "TIPO_CONTA")
    private Integer tipoConta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Short getPercentualMes() {
        return percentualMes;
    }

    public void setPercentualMes(Short percentualMes) {
        this.percentualMes = percentualMes;
    }

    public Integer getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(Integer tipoConta) {
        this.tipoConta = tipoConta;
    }

}

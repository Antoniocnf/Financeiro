package br.com.next.liberdadefinanceirafree;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import br.com.next.liberdadefinanceirafree.system.Constantes;
import br.com.next.liberdadefinanceirafree.system.LibFinanceMessage;

public class InformacoesActivity extends GenericActivity {

    private int cliques;

    private TextView txtViewOQueE;
    private TextView txtViewComo;
    private TextView txtViewPorque;
    private TextView txtViewBottomLine;

    @Override
    protected void create(Bundle savedInstanceState) throws Exception {
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.init();
    }

    @Override
    protected void resume() throws Exception {
        this.cliques = 0;
    }

    private void init() throws Exception {
        String msgOQueE = getString(R.string.o_software_foi_feito_para_te_auxiliar);
        txtViewOQueE = (TextView) findViewById(R.id.txtViewInformacoesOQueE);
        txtViewOQueE.setText(msgOQueE);
        txtViewOQueE.setTypeface(getLightTypeface());

        String msgComo = getString(R.string.para_realizar_o_cadastro_dos_d_bitos_selecione_a_conta_referente_ao_d_bito);
        txtViewComo = (TextView) findViewById(R.id.txtViewInformacoesComo);
        txtViewComo.setText(msgComo);
        txtViewComo.setTypeface(getLightTypeface());

        String msgPorque = getString(R.string.este_software_foi_inspirado_no_livro_o_segredo_da_mente_milion_ria);
        txtViewPorque = (TextView) findViewById(R.id.txtViewInformacoesPorque);
        txtViewPorque.setText(msgPorque);
        txtViewPorque.setTypeface(getLightTypeface());

        String versao = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        txtViewBottomLine = (TextView) findViewById(R.id.txtViewInformacoesBottomLine);
        txtViewBottomLine.setText(getResources().getString(R.string.version) + " " + versao);

        txtViewBottomLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cliques ++;
                if (cliques >= 10){
                    exibirDialogoEntrarConfiguracoesAvancadas();
                }
            }
        });
    }

    private void exibirDialogoEntrarConfiguracoesAvancadas() {
        View view = LayoutInflater.from(this).inflate(R.layout.insere_senha, null);
        final EditText editText = (EditText) view.findViewById(R.id.editTextInsereSenha);

        MaterialStyledDialog alert = LibFinanceMessage.criaAlerta(this);
        alert.setTitle(R.string.informe_senha);
        alert.setCustomView(view, 20, 20, 20, 0);
        alert.setNegative(getResources().getString(R.string.cancelar), null);
        alert.setPositive(getResources().getString(R.string.ok), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
                String senha = editText.getText().toString();
                if (senhaEstaCorreta(senha)){
                    startActivity(new Intent(InformacoesActivity.this, ConfiguracoesAvancadasActivity.class));
                }
            }
        });
        alert.show();
    }

    private boolean senhaEstaCorreta(String senha) {
        return senha != null && !senha.isEmpty() && senha.equals(Constantes.ADVANCED_SETTINGS_PASSWORD);
    }
}

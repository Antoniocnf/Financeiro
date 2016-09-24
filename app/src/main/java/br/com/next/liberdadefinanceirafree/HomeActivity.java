package br.com.next.liberdadefinanceirafree;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;

import br.com.next.liberdadefinanceirafree.service.ContaService;
import br.com.next.liberdadefinanceirafree.service.SistemaService;

public class HomeActivity extends GenericActivity {

    private LinearLayout linearLayoutContas;
    private LinearLayout linearLayoutBalanco;
    private LinearLayout linearLayoutConfiguracoes;
    private LinearLayout linearLayoutInformacoes;

    @Override
    protected void create(Bundle savedInstanceState) throws Exception {
        setContentView(R.layout.activity_home);

        SistemaService.criaPastasNecessarias();
        if (!SistemaService.existeDadosJaCadastrados()){
            ContaService.criarDadosContas(this);
        }

        SistemaService.verificaNovoMes(this);

        this.linearLayoutContas = (LinearLayout) findViewById(R.id.linearLayoutHomeContas);
        this.linearLayoutContas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ContasActivity.class));
            }
        });

        this.linearLayoutBalanco = (LinearLayout) findViewById(R.id.linearLayoutHomeBalanco);
        this.linearLayoutBalanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, BalancoActivity.class));
            }
        });

        this.linearLayoutConfiguracoes = (LinearLayout) findViewById(R.id.linearLayoutHomeConfiguracoes);
        this.linearLayoutConfiguracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ConfiguracoesActivity.class));
            }
        });

        this.linearLayoutInformacoes = (LinearLayout) findViewById(R.id.linearLayoutHomeInformacoes);
        this.linearLayoutInformacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, InformacoesActivity.class));
            }
        });

        Snackbar snackbar = Snackbar.make(linearLayoutContas, R.string.juizo_com_dinheiro, Snackbar.LENGTH_LONG).setAction("Action", null);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.blue_navy));
        snackbar.show();

//        AdView mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
    }
}

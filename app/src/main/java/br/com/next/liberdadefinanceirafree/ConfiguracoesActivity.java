package br.com.next.liberdadefinanceirafree;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import java.io.File;

import br.com.next.liberdadefinanceirafree.service.LibFinancePlanilha;
import br.com.next.liberdadefinanceirafree.system.Constantes;
import br.com.next.liberdadefinanceirafree.system.LibFinanceMessage;
import br.com.next.liberdadefinanceirafree.util.StringUtils;

public class ConfiguracoesActivity extends GenericActivity {

    private static final int REQUEST_CODE_CADASTRO_PIN = 1;

    private LinearLayout linearLayoutValoresSistema;
    private LinearLayout linearLayoutGerarPlanilha;
    private LinearLayout linearLayoutRestaurarDePlanilha;
    private LinearLayout linearLayoutVotarApp;
    private LinearLayout linearLayoutEnviarEmail;
    private LinearLayout linearLayoutPin;
    private Switch switchPin;
    private Button buttonCompartilhar;

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_configuracoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.linearLayoutValoresSistema = (LinearLayout) findViewById(R.id.linearLayoutConfiguracoesValoresSistema);
        this.linearLayoutGerarPlanilha = (LinearLayout) findViewById(R.id.linearLayoutConfiguracoesGerarPlanilha);
        this.linearLayoutRestaurarDePlanilha = (LinearLayout) findViewById(R.id.linearLayoutConfiguracoesRestaurarBaseDePlanilha);
        this.linearLayoutVotarApp = (LinearLayout) findViewById(R.id.linearLayoutConfiguracoesVotarApp);
        this.linearLayoutEnviarEmail = (LinearLayout) findViewById(R.id.linearLayoutConfiguracoesEnviarEmail);
        this.linearLayoutPin = (LinearLayout) findViewById(R.id.linearLayoutConfiguracoesPin);
        this.switchPin = (Switch) findViewById(R.id.switchConfiguracoesPin);
        this.buttonCompartilhar = (Button) findViewById(R.id.buttonConfiguracoesCompartilhar);

        this.init();
    }

    private void init() {
        this.linearLayoutValoresSistema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfiguracoesActivity.this, ValoresSistemaActivity.class));
            }
        });

        this.linearLayoutGerarPlanilha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gerarPlanilhaAsync();
            }
        });

        this.linearLayoutRestaurarDePlanilha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibFinanceMessage.showProFeature(ConfiguracoesActivity.this);
            }
        });

        this.linearLayoutVotarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + ConfiguracoesActivity.this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + ConfiguracoesActivity.this.getPackageName())));
                }
            }
        });

        this.buttonCompartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msgToShare = getString(R.string.mensagem_compartilhar_app);
                String shareBody = msgToShare + " " + "http://play.google.com/store/apps/details?id=" + ConfiguracoesActivity.this.getPackageName();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                sharingIntent.putExtra(Intent.EXTRA_HTML_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.compartilhar_app)));
            }
        });

        this.linearLayoutEnviarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:alyssonmyller@gmail.com?subject=Informações - App Liberdade Financeira");
                intent.setData(data);
                startActivity(intent);
            }
        });

        this.linearLayoutPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibFinanceMessage.showProFeature(ConfiguracoesActivity.this);
            }
        });
    }

    @Override
    protected void resume() throws Exception {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String pin = sharedPreferences.getString(Constantes.SHARED_PREFERENCES_PIN, null);
        if (pin == null || !StringUtils.hasLength(pin)){
            this.switchPin.setChecked(false);
        } else {
            this.switchPin.setChecked(true);
        }
        this.switchPin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Intent intent = new Intent(ConfiguracoesActivity.this, PinActivity.class);
                    intent.putExtra(Constantes.EH_CADASTRO, true);
                    startActivityForResult(intent, REQUEST_CODE_CADASTRO_PIN);
                } else {
                    SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constantes.SHARED_PREFERENCES_PIN, null);
                    editor.commit();
                }
            }
        });
    }

    @Override
    protected void activityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_CADASTRO_PIN:
                    break;
            }
        }
    }

    private void gerarPlanilhaAsync() {
        new AsyncTask<Void, Void, Void>() {

            private Exception exception;
            private MaterialStyledDialog dialog;
            private File file;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ProgressBar progressBar = new ProgressBar(ConfiguracoesActivity.this);
                dialog = LibFinanceMessage.criaAlerta(ConfiguracoesActivity.this);
                dialog.setTitle(getResources().getString(R.string.gera_planilha_async_progresso_dialog_msg));
                dialog.setCancelable(false);
                dialog.setCustomView(progressBar, 20, 20, 20, 10);
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    file = LibFinancePlanilha.geraPlanilhaMovimentacoes(ConfiguracoesActivity.this);
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                dialog.dismiss();
                if (exception != null) {
                    LibFinanceMessage.show(ConfiguracoesActivity.this, exception);
                } else {
                    String msg = getResources().getString(R.string.sua_planilha_foi_gerada_em) + " " + file.getAbsolutePath();

                    MaterialStyledDialog alert = LibFinanceMessage.criaAlerta(ConfiguracoesActivity.this);
                    alert.setDescription(msg);
                    alert.setNegative(getResources().getString(R.string.ok), null);
                    alert.setPositive(getResources().getString(R.string.compartilhar), new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);
                        }
                    });
                    alert.show();
                }
            }
        }.execute();
    }

    private void restaurarBaseDePlanilhaAsync() {
        new AsyncTask<Void, Void, Void>() {

            private Exception exception;
            private MaterialStyledDialog dialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ProgressBar progressBar = new ProgressBar(ConfiguracoesActivity.this);
                dialog = LibFinanceMessage.criaAlerta(ConfiguracoesActivity.this);
                dialog.setTitle(getResources().getString(R.string.restaurando_base_de_dados_de_planilha));
                dialog.setCancelable(false);
                dialog.setCustomView(progressBar, 20, 20, 20, 10);
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    LibFinancePlanilha.restaurarBaseDesdePlanilha(ConfiguracoesActivity.this);
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                dialog.dismiss();
                if (exception != null) {
                    LibFinanceMessage.show(ConfiguracoesActivity.this, exception);
                }
            }
        }.execute();
    }


}

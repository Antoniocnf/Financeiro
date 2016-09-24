package br.com.next.liberdadefinanceirafree;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.next.liberdadefinanceirafree.system.Constantes;
import br.com.next.liberdadefinanceirafree.util.StringUtils;

public class PinActivity extends GenericActivity {

    private static final int NUMERO_0 = 0;
    private static final int NUMERO_1 = 1;
    private static final int NUMERO_2 = 2;
    private static final int NUMERO_3 = 3;
    private static final int NUMERO_4 = 4;
    private static final int NUMERO_5 = 5;
    private static final int NUMERO_6 = 6;
    private static final int NUMERO_7 = 7;
    private static final int NUMERO_8 = 8;
    private static final int NUMERO_9 = 9;

    private boolean ehCadastro;

    private TextView txtViewTitulo;
    private EditText editTextPassword;
    private LinearLayout linearLayoutBackspace;
    private LinearLayout linearLayout0;
    private LinearLayout linearLayout1;
    private LinearLayout linearLayout2;
    private LinearLayout linearLayout3;
    private LinearLayout linearLayout4;
    private LinearLayout linearLayout5;
    private LinearLayout linearLayout6;
    private LinearLayout linearLayout7;
    private LinearLayout linearLayout8;
    private LinearLayout linearLayout9;
    private Button buttonEntrar;

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pin);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_navy));
        }
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.hide();
        }

        txtViewTitulo = (TextView) findViewById(R.id.txtViewPinTitulo);

        ehCadastro = getIntent().getExtras().getBoolean(Constantes.EH_CADASTRO, false);
        if (ehCadastro) {
            txtViewTitulo.setText(R.string.cadastre_pin);
        } else {
            txtViewTitulo.setText(R.string.informe_pin);
        }

        editTextPassword = (EditText) findViewById(R.id.editTextPinPassword);
        linearLayout0 = (LinearLayout) findViewById(R.id.linearLayoutPin0);
        linearLayout0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insereNumeralSenha(NUMERO_0);
            }
        });

        linearLayout1 = (LinearLayout) findViewById(R.id.linearLayoutPin1);
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insereNumeralSenha(NUMERO_1);
            }
        });

        linearLayout2 = (LinearLayout) findViewById(R.id.linearLayoutPin2);
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insereNumeralSenha(NUMERO_2);
            }
        });

        linearLayout3 = (LinearLayout) findViewById(R.id.linearLayoutPin3);
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insereNumeralSenha(NUMERO_3);
            }
        });

        linearLayout4 = (LinearLayout) findViewById(R.id.linearLayoutPin4);
        linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insereNumeralSenha(NUMERO_4);
            }
        });

        linearLayout5 = (LinearLayout) findViewById(R.id.linearLayoutPin5);
        linearLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insereNumeralSenha(NUMERO_5);
            }
        });

        linearLayout6 = (LinearLayout) findViewById(R.id.linearLayoutPin6);
        linearLayout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insereNumeralSenha(NUMERO_6);
            }
        });

        linearLayout7 = (LinearLayout) findViewById(R.id.linearLayoutPin7);
        linearLayout7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insereNumeralSenha(NUMERO_7);
            }
        });

        linearLayout8 = (LinearLayout) findViewById(R.id.linearLayoutPin8);
        linearLayout8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insereNumeralSenha(NUMERO_8);
            }
        });

        linearLayout9 = (LinearLayout) findViewById(R.id.linearLayoutPin9);
        linearLayout9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insereNumeralSenha(NUMERO_9);
            }
        });

        linearLayoutBackspace = (LinearLayout) findViewById(R.id.linearLayoutPinBackspace);
        linearLayoutBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apagarUltimoNumero();
            }
        });

        buttonEntrar = (Button) findViewById(R.id.buttonPinEntrar);

        if (ehCadastro){
            buttonEntrar.setText(R.string.btn_cadastrar_pin);
        } else {
            buttonEntrar.setText(R.string.entrar);
        }

        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ehCadastro) {
                    cadastrar();
                } else {
                    entrar();
                }
            }
        });
    }

    private void cadastrar() {
        String textoAtual = editTextPassword.getText().toString();
        if (!StringUtils.hasLength(textoAtual) || textoAtual.length() < 4) {
            Snackbar snackbar = Snackbar.make(buttonEntrar, R.string.pin_deve_conter_4_numeros, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            snackbar.show();
        } else {
            SharedPreferences sp = getSharedPreferences(Constantes.SHARED_PREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(Constantes.SHARED_PREFERENCES_PIN, textoAtual);
            editor.commit();

            Intent intent = getIntent();
            setResult(Activity.RESULT_OK, intent);
            this.finish();
        }
    }

    private void entrar() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String pin = sharedPreferences.getString(Constantes.SHARED_PREFERENCES_PIN, null);

        String textoAtual = editTextPassword.getText().toString();
        if (StringUtils.hasLength(textoAtual) && textoAtual.equals(pin)) {
            Intent intent = getIntent();
            setResult(Activity.RESULT_OK, intent);
            this.finish();
        } else {
            Snackbar snackbar = Snackbar.make(buttonEntrar, R.string.pin_incorreto, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            snackbar.show();
        }
    }

    private void apagarUltimoNumero() {
        String textoAtual = editTextPassword.getText().toString();
        if (textoAtual.length() < 2) {
            editTextPassword.setText("");
        } else {
            if (textoAtual != null && StringUtils.hasLength(textoAtual)){
                editTextPassword.setText(textoAtual.substring(0, textoAtual.length() - 1));
            }
        }
    }

    private void insereNumeralSenha(int numero) {
        String textoAtual = editTextPassword.getText().toString();
        editTextPassword.setText(StringUtils.hasLength(textoAtual) ? (textoAtual + numero) : String.valueOf(numero));
    }

}

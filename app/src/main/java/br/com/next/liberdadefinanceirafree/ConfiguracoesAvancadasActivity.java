package br.com.next.liberdadefinanceirafree;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;
import java.io.IOException;

import br.com.next.liberdadefinanceirafree.system.Constantes;
import br.com.next.liberdadefinanceirafree.system.LibFinanceMessage;
import br.com.next.liberdadefinanceirafree.util.DatabaseUtils;

public class ConfiguracoesAvancadasActivity extends GenericActivity {

    private LinearLayout linearLayoutBackup;
    private LinearLayout linearLayoutRestaurar;
    private LinearLayout linearLayoutEnviarBD;

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_configuracoes_avancadas);
        
        this.linearLayoutBackup = (LinearLayout) findViewById(R.id.linearLayoutConfiguracoesAvancadasBackup);
        this.linearLayoutBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DatabaseUtils.backupDataBase(ConfiguracoesAvancadasActivity.this);
                } catch (Exception e) {
                    LibFinanceMessage.show(ConfiguracoesAvancadasActivity.this, e);
                }
            }
        });

        this.linearLayoutRestaurar = (LinearLayout) findViewById(R.id.linearLayoutConfiguracoesAvancadasRestaurar);
        this.linearLayoutRestaurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DatabaseUtils.importDataBase(ConfiguracoesAvancadasActivity.this);
                } catch (Exception e) {
                    LibFinanceMessage.show(ConfiguracoesAvancadasActivity.this, e);
                }
            }
        });

        this.linearLayoutEnviarBD = (LinearLayout) findViewById(R.id.linearLayoutConfiguracoesAvancadasEnviarBase);
        this.linearLayoutEnviarBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBaseEmail();
            }
        });
    }

    public void sendBaseEmail() {
        try {
            DatabaseUtils.backupDataBase(this);
        } catch (IOException e) {
            LibFinanceMessage.show(this, e);
        }

        String backupDBPath = Constantes.BACKUP_FOLDER + "/" + Constantes.DATABASE_NAME;
        File backupFile = new File(Environment.getExternalStorageDirectory(), backupDBPath);

        Intent email = new Intent(android.content.Intent.ACTION_SEND);
        email.setType("application/octet-stream");
        email.putExtra(Intent.EXTRA_STREAM, Uri.parse(backupFile.getAbsolutePath()));
        email.putExtra(android.content.Intent.EXTRA_STREAM, Uri.parse("file:"+backupFile.getAbsolutePath()));
        startActivity(Intent.createChooser(email, "Enviar..."));
    }

}

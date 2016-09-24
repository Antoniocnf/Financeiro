package br.com.next.liberdadefinanceirafree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.math.BigDecimal;
import java.util.List;

import br.com.next.liberdadefinanceirafree.adapter.ContaListAdapter;
import br.com.next.liberdadefinanceirafree.model.ContaMesDTO;
import br.com.next.liberdadefinanceirafree.service.ContaMesService;
import br.com.next.liberdadefinanceirafree.util.DateUtils;
import br.com.next.liberdadefinanceirafree.util.IntentParameterUtil;
import br.com.next.liberdadefinanceirafree.util.PrecoUtils;

public class ContasActivity extends GenericActivity {

    private LinearLayout linearLayoutRecebimento;
    private ListView listViewContas;
    private TextView txtViewValorRecebido;
    private TextView txtViewValorMesAno;

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_contas);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        this.linearLayoutRecebimento = (LinearLayout) findViewById(R.id.linearLayoutContasRecebimento);
        this.txtViewValorRecebido = (TextView) findViewById(R.id.txtViewContasValorRecebido);
        this.txtViewValorMesAno = (TextView) findViewById(R.id.txtViewContasMesAno);
        this.listViewContas = (ListView) findViewById(R.id.listViewContas);

        MobileAds.initialize(this, "ca-app-pub-2727475796732211~3261323485");

        AdView adView = (AdView) findViewById(R.id.adViewContas); //add the cast
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);
    }

    @Override
    protected void resume() throws Exception {
        linearLayoutRecebimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContasActivity.this, CreditosActivity.class));
            }
        });

        String textRecebimento = getString(R.string.saldo_em);
        String mesAnoAtual = DateUtils.getDescricaoMesEAnoAtual();
        this.txtViewValorMesAno.setText(textRecebimento + " " + mesAnoAtual);
        this.txtViewValorMesAno.setTypeface(getRegularTypeface());

        List<ContaMesDTO> contas = ContaMesService.getContasMesAtual();
        ContaListAdapter adapter = new ContaListAdapter(this, contas);
        this.listViewContas = (ListView) findViewById(R.id.listViewContas);
        this.listViewContas.setAdapter(adapter);
        this.listViewContas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View arg1, int pos, long arg3) {
                ContaMesDTO conta = (ContaMesDTO) adapter.getItemAtPosition(pos);

                Intent intent = new Intent(ContasActivity.this, ContaDetalheActivity.class);
                intent.putExtra(IntentParameterUtil.ID_CONTA, conta.getIdConta());
                startActivity(intent);
            }
        });

        BigDecimal valorTotalMes = BigDecimal.ZERO;
        for (ContaMesDTO conta : contas){
            valorTotalMes = valorTotalMes.add(conta.getValorRestante());
        }

        this.txtViewValorRecebido.setText(PrecoUtils.formatoPadrao(valorTotalMes));
        this.txtViewValorRecebido.setTypeface(getLightTypeface());
    }

    @Override
    protected void createOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contas, menu);
    }

    @Override
    protected void optionsItemSelected(MenuItem item) throws Exception {
        switch (item.getItemId()){
            case R.id.action_creditos:
                startActivity(new Intent(this, CreditosActivity.class));
                break;
        }
    }
}

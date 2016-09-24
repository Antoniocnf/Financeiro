package br.com.next.liberdadefinanceirafree;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import br.com.next.liberdadefinanceirafree.adapter.BalancoDetalheListAdapter;
import br.com.next.liberdadefinanceirafree.model.BalancoMensalDTO;
import br.com.next.liberdadefinanceirafree.model.Movimentacao;
import br.com.next.liberdadefinanceirafree.service.MovimentacaoService;
import br.com.next.liberdadefinanceirafree.util.DateUtils;
import br.com.next.liberdadefinanceirafree.util.IntentParameterUtil;
import br.com.next.liberdadefinanceirafree.util.PrecoUtils;

public class BalancoDetalheActivity extends GenericActivity {

    private BalancoMensalDTO balancoMensalDTO;

    private TextView txtViewDescMesAno;
    private NumberProgressBar numberProgressBar;
    private TextView txtViewTotalConta;
    private TextView txtViewTotalParaInvestir;
    private ListView listViewMovimentacoes;

    @Override
    protected void create(Bundle savedInstanceState) throws Exception {
        setContentView(R.layout.activity_balanco_detalhe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.balancoMensalDTO = (BalancoMensalDTO) getIntent().getExtras().get(IntentParameterUtil.RESUMO_MENSAL_SEL);

        this.txtViewDescMesAno = (TextView) findViewById(R.id.txtViewBalancoDetalheDescMesAno);
        this.numberProgressBar = (NumberProgressBar) findViewById(R.id.progressBarBalancoDetalhe);
        this.txtViewTotalConta = (TextView) findViewById(R.id.txtViewBalancoDetalheTotalConta);
        this.txtViewTotalParaInvestir = (TextView) findViewById(R.id.txtViewBalancoDetalheTotalParaInvestir);
        this.listViewMovimentacoes = (ListView) findViewById(R.id.listViewBalancoDetalheMovimentacoes);

        this.init();
    }

    private void init() throws ParseException {
        String dataFormatada = String.format("%04d-%02d-%02d", balancoMensalDTO.getAno(), balancoMensalDTO.getMes(), 1);
        Date data = DateUtils.convertFromDefaultDatabaseFormat(dataFormatada);
        this.txtViewDescMesAno.setText(DateUtils.getDescricaoMesEAno(data));
        this.txtViewDescMesAno.setTypeface(getLightTypeface());

        this.txtViewTotalParaInvestir.setText(PrecoUtils.formatoPadrao(balancoMensalDTO.getValorCredito()));
        this.txtViewTotalConta.setText(PrecoUtils.formatoPadrao(balancoMensalDTO.getValorDebito()));

        Integer porcentagem = PrecoUtils.calculaPorcentagemDeValorUtilizado(balancoMensalDTO.getValorDebito(), balancoMensalDTO.getValorCredito());
        if (porcentagem > 100){
            numberProgressBar.setProgress(100);
            numberProgressBar.setProgressTextVisibility(NumberProgressBar.ProgressTextVisibility.Invisible);
        } else {
            numberProgressBar.setProgress(porcentagem);
        }
        this.numberProgressBar.setProgress(porcentagem);

        List<Movimentacao> movimentacoes = MovimentacaoService.buscarPorAnoEMes(balancoMensalDTO.getAno(), balancoMensalDTO.getMes());
        BalancoDetalheListAdapter adapter = new BalancoDetalheListAdapter(this, movimentacoes);
        this.listViewMovimentacoes.setAdapter(adapter);
    }

}

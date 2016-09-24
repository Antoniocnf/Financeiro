package br.com.next.liberdadefinanceirafree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.next.liberdadefinanceirafree.adapter.BalancoListAdapter;
import br.com.next.liberdadefinanceirafree.model.BalancoMensalDTO;
import br.com.next.liberdadefinanceirafree.service.BalancoService;
import br.com.next.liberdadefinanceirafree.util.CollectionUtils;
import br.com.next.liberdadefinanceirafree.util.DateUtils;
import br.com.next.liberdadefinanceirafree.util.IntentParameterUtil;

public class BalancoActivity extends GenericActivity {

    private LineChart lineChartBalanco;
    private ListView listViewMeses;
    private List<BalancoMensalDTO> balancos;

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_balanco);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lineChartBalanco = (LineChart) findViewById(R.id.lineChartBalanco);
        listViewMeses = (ListView) findViewById(R.id.listViewMeses);

        this.init();
    }

    /**
     * <p><b>Autoria: </b>Alysson Myller - 13/07/2013</p>
     */
    private void init() {
        this.balancos = BalancoService.buscarTodos();

        BalancoListAdapter adapter = new BalancoListAdapter(this, balancos);
        this.listViewMeses.setAdapter(adapter);
        this.listViewMeses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View arg1, int pos, long arg3) {
                BalancoMensalDTO res = (BalancoMensalDTO) adapter.getItemAtPosition(pos);

                Intent intent = new Intent(BalancoActivity.this, BalancoDetalheActivity.class);
                intent.putExtra(IntentParameterUtil.RESUMO_MENSAL_SEL, res);
                startActivity(intent);
            }
        });

        atualizaGrafico();
    }

    private void atualizaGrafico() {
        List<BalancoMensalDTO> balancos = BalancoService.buscarTodosOrdemCrescente();
        balancos = get6UltimosMeses(balancos);

        ArrayList<Entry> entradasDeCredito = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();



        int pos = 0;
        for (BalancoMensalDTO balanco : balancos){
            entradasDeCredito.add(new Entry(balanco.getValorCredito().floatValue(), pos));

            Calendar cal = Calendar.getInstance();
            cal.set(balanco.getAno(), balanco.getMes() - 1, 1);
            String mesAno = DateUtils.getDescricaoReduzidaMesEAno(cal.getTime());
            labels.add(mesAno);

            pos++;
        }

        LineDataSet dataset = new LineDataSet(entradasDeCredito, "Créditos");
        dataset.setCircleColor(ContextCompat.getColor(this, R.color.green));
        dataset.setCircleColorHole(ContextCompat.getColor(this, R.color.green));
        dataset.setLineWidth(3f);
        dataset.setColor(ContextCompat.getColor(this, R.color.green));
        dataset.setDrawFilled(false);
        dataset.setDrawCubic(true);

        LineData data = new LineData(labels);
        data.addDataSet(dataset);

        ArrayList<Entry> entradasDeDebito = new ArrayList<>();

        pos = 0;
        for (BalancoMensalDTO balanco : balancos){
            if (pos >= 5){
                break;
            }

            entradasDeDebito.add(new Entry(balanco.getValorDebito().floatValue(), pos));
            pos++;
        }

        LineDataSet dataset2 = new LineDataSet(entradasDeDebito, "Débitos");
        dataset2.setCircleColor(ContextCompat.getColor(this, R.color.red));
        dataset2.setCircleColorHole(ContextCompat.getColor(this, R.color.red));
        dataset2.setLineWidth(2f);
        dataset2.setColor(ContextCompat.getColor(this, R.color.red));
        dataset2.setDrawFilled(false);
        dataset2.setDrawCubic(true);

        data.addDataSet(dataset2);
        data.setDrawValues(false);

        lineChartBalanco.setData(data); // set the data and list of lables into chart
        lineChartBalanco.animateY(1000);

        lineChartBalanco.getAxisRight().setEnabled(false);
        lineChartBalanco.getAxisRight().setDrawLabels(false);
        lineChartBalanco.setPinchZoom(false);
        lineChartBalanco.setDescription("");
        lineChartBalanco.setTouchEnabled(false);
        lineChartBalanco.setDoubleTapToZoomEnabled(false);
        lineChartBalanco.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChartBalanco.getAxisLeft().setGridColor(ContextCompat.getColor(this, R.color.whitesmoke));
//        lineChartBalanco.getAxisLeft().setEnabled(false);
        lineChartBalanco.getXAxis().setGridColor(ContextCompat.getColor(this, R.color.whitesmoke));
        lineChartBalanco.getAxisLeft().setSpaceTop(20f);
        lineChartBalanco.getAxisLeft().setSpaceBottom(80f);
        lineChartBalanco.invalidate();
    }

    private List<BalancoMensalDTO> get6UltimosMeses(List<BalancoMensalDTO> balancos) {
        if (CollectionUtils.isEmpty(balancos)){
            return new ArrayList<>();
        }

        if (!CollectionUtils.isEmpty(balancos) && balancos.size() < 2){
            return criarBalancoInicialZerado(balancos.get(0));
        }

        if (balancos.size() > 5){
            return balancos.subList(balancos.size() - 5, balancos.size());
        }

        return balancos;
    }

    private List<BalancoMensalDTO> criarBalancoInicialZerado(BalancoMensalDTO balancoMensalDTO) {
        List<BalancoMensalDTO> balancos = new ArrayList<>();

        BalancoMensalDTO balanco = new BalancoMensalDTO();

        if (balancoMensalDTO.getMes() == 1){
            balanco.setAno(balancoMensalDTO.getAno() - 1);
        } else {
            balanco.setAno(balancoMensalDTO.getAno());
        }

        balanco.setMes(balancoMensalDTO.getMes() - 1);
        balanco.setValorCredito(BigDecimal.ZERO);
        balanco.setValorDebito(BigDecimal.ZERO);
        balancos.add(balanco);
        balancos.add(balancoMensalDTO);

        return balancos;
    }

}

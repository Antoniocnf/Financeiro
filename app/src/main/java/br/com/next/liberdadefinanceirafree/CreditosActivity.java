package br.com.next.liberdadefinanceirafree;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.next.liberdadefinanceirafree.adapter.CreditoListAdapter;
import br.com.next.liberdadefinanceirafree.enums.TipoCredito;
import br.com.next.liberdadefinanceirafree.model.Credito;
import br.com.next.liberdadefinanceirafree.service.CreditoService;
import br.com.next.liberdadefinanceirafree.system.LibFinanceMessage;
import br.com.next.liberdadefinanceirafree.util.AndroidMoneyTextWatcher;
import br.com.next.liberdadefinanceirafree.util.CollectionUtils;
import br.com.next.liberdadefinanceirafree.util.DateUtils;
import br.com.next.liberdadefinanceirafree.util.PrecoUtils;

public class CreditosActivity extends GenericActivity {

    private static final int CONTEXT_MENU_REMOVER = 1;

    private Credito creditoSel;

    private TextView txtViewValorRecebido;
    private TextView txtViewMes;
    private ListView listViewCreditos;

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_creditos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.txtViewValorRecebido = (TextView) findViewById(R.id.txtViewCreditosValorRecebido);
        this.txtViewMes = (TextView) findViewById(R.id.txtViewCreditosMes);
        this.listViewCreditos = (ListView) findViewById(R.id.listViewCreditos);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exibirDialogoAddCredito();
            }
        });

        this.init();
    }

    @Override
    public void createContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle(R.string.selecione_);
        menu.add(0, CONTEXT_MENU_REMOVER, 0, R.string.remover);
    }

    @Override
    public void contextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CONTEXT_MENU_REMOVER:
                removerCreditoSelecionado();
                atualizarListaCreditos();
                break;

            default:
                break;
        }
    }

    /**
     * <p><b>Autoria: </b>Alysson Myller - 21/06/2013</p>
     */
    private void removerCreditoSelecionado() {
        if (creditoSel == null){
            LibFinanceMessage.show(this, getString(R.string._obrigat_rio_selecionar_o_cr_dito_para_remover));
        } else {
            CreditoService.remover(creditoSel);
        }
    }

    /**
     * <p><b>Autoria: </b>Alysson Myller - 21/06/2013</p>
     */
    private void init() {
        this.txtViewMes.setText(DateUtils.getDescricaoMesEAnoAtual());
        this.txtViewMes.setTypeface(getRegularTypeface());

        atualizarListaCreditos();
    }

    /**
     * <p><b>Autoria: </b>Alysson Myller - 21/06/2013</p>
     */
    private void atualizarListaCreditos() {
        List<Credito> creditosDoMes = CreditoService.buscarCreditosMesAtual();
        CreditoListAdapter adapter = new CreditoListAdapter(this, creditosDoMes);
        this.listViewCreditos.setAdapter(adapter);
        this.listViewCreditos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View arg1, int pos, long arg3) {
                creditoSel = (Credito) adapter.getItemAtPosition(pos);
                registerForContextMenu(listViewCreditos);
                return false;
            }
        });

        BigDecimal valorTotal = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(creditosDoMes)){
            for (Credito c : creditosDoMes){
                valorTotal = valorTotal.add(c.getValor());
            }
        }

        this.txtViewValorRecebido.setText(PrecoUtils.formatoPadrao(valorTotal));
        this.txtViewValorRecebido.setTypeface(getLightTypeface());
    }

    /**
     * <p><b>Autoria: </b>Alysson Myller - 14/06/2013</p>
     */
    private void exibirDialogoAddCredito() {
        final MaterialStyledDialog alert = LibFinanceMessage.criaAlerta(this);

        View view = LayoutInflater.from(this).inflate(R.layout.add_credito, null);
        final EditText editTextDescricao = (EditText) view.findViewById(R.id.editTextAddDebitoDescricao);
        final EditText editTextValor = (EditText) view.findViewById(R.id.editTextAddDebitoValor);
        final CheckBox checkBoxRecorrente = (CheckBox) view.findViewById(R.id.checkBoxAddCreditoRecorrente);
        editTextValor.addTextChangedListener(new AndroidMoneyTextWatcher(this, editTextValor));
        editTextValor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    handleSalvarCredito(editTextDescricao, editTextValor, checkBoxRecorrente);
                    alert.dismiss();
                    return true;
                }
                return false;
            }
        });


        alert.setStyle(Style.HEADER_WITH_ICON)
        .setHeaderColor(R.color.green)
        .setIcon(R.drawable.ic_money_plus)
        .setDescription(null)
        .setTitle(null)
        .setCustomView(view, 20, 20, 20, 0)
        .setNegative(getResources().getString(R.string.cancelar), null)
        .setPositive(getResources().getString(R.string.salvar), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                handleSalvarCredito(editTextDescricao, editTextValor, checkBoxRecorrente);
            }
        })
        .show();
    }

    private void handleSalvarCredito(EditText editTextDescricao, EditText editTextValor, CheckBox checkBoxRecorrente){
        String descricaoCredito = editTextDescricao.getText().toString();
        BigDecimal valorCredito = getBigDecimalValueFromEditText(editTextValor);

        boolean erro = false;
        View focusView = null;
        if (descricaoCredito == null || descricaoCredito.isEmpty()){
            editTextDescricao.setError(getString(R.string.informar_este_valor));
            erro = true;
            focusView = editTextDescricao;
        } else if (valorCredito == null || valorCredito.compareTo(BigDecimal.ZERO) < 1){
            editTextValor.setError(getString(R.string.informar_este_valor));
            erro = true;
            focusView = editTextValor;
        }

        if (erro){
            focusView.requestFocus();
        } else {
            if (checkBoxRecorrente.isChecked()){
                exibirDialogoInformaRecorrencia(descricaoCredito, valorCredito);
            } else {
                try {
                    CreditoService.criarCredito(CreditosActivity.this, descricaoCredito, valorCredito, TipoCredito.NOVO_CREDITO);
                    atualizarListaCreditos();
                } catch (Exception e) {
                    LibFinanceMessage.show(this, e);
                }
            }
        }
    }

    /**
     * <p><b>Autoria: </b>Alysson Myller - 05/08/2013</p>
     */
    private void exibirDialogoInformaRecorrencia(final String descricaoCredito, final BigDecimal valorCredito) {
        View view = LayoutInflater.from(this).inflate(R.layout.seleciona_recorrencia, null);

        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroupSelecionaRecorrencia);

        MaterialStyledDialog alert = LibFinanceMessage.criaAlerta(this);
        alert.setHeaderColor(R.color.green);
        alert.setIcon(R.drawable.ic_money_plus);
        alert.setTitle(getString(R.string.conta_detalhe_title_dialogo_seleciona_recorrencia));
        alert.setCustomView(view, 20, 20, 20, 0);
        alert.setNegative(getString(R.string.cancelar), null);
        alert.setPositive(getString(R.string.salvar), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                int radioSelId = radioGroup.getCheckedRadioButtonId();
                switch (radioSelId){
                    case R.id.radioButtonRecorrenciaSempre:
                        try {
                            addCreditoRecorrenteParaSempre(descricaoCredito, valorCredito);
                            atualizarListaCreditos();
                        } catch (Exception e) {
                            dialog.dismiss();
                            LibFinanceMessage.show(CreditosActivity.this, e);
                        }

                        break;
                    case R.id.radioButtonRecorrenciaSelecionaData:
                        exibirDialogoSelecionarDataRecorrencia(descricaoCredito, valorCredito);
                        break;
                }
            }
        });
        alert.show();
    }

    /**
     * <p><b>Autoria: </b>Alysson Myller - 14/06/2013</p>
     */
    private void addCreditoRecorrenteParaSempre(String descricaoCredito, BigDecimal valorCredito) throws Exception {
        if (descricaoCredito == null || valorCredito == null) {
            LibFinanceMessage.show(this, getString(R.string._obrigat_rio_informar_a_descri_o_e_o_valor_do_d_bito_));
        } else {
            // como se fosse para sempre - 1008 meses a partir de 2016 da até 2100
            Calendar hoje = Calendar.getInstance();
            hoje.setTime(new Date());
            hoje.add(Calendar.MONTH, 1008);
            String dataLimiteRecorrencia = DateUtils.convertToDefaultDatabaseFormat(hoje.getTime());
            CreditoService.criarCreditoRecorrente(descricaoCredito, valorCredito, dataLimiteRecorrencia);
            atualizarListaCreditos();
        }
    }

    /**
     * <p><b>Autoria: </b>Alysson Myller - 05/08/2013</p>
     * @param descricaoCredito
     * @param valorCredito
     */
    private void exibirDialogoSelecionarDataRecorrencia(final String descricaoCredito, final BigDecimal valorCredito) {

        // Cria os datePickers
        final View view = LayoutInflater.from(this).inflate(R.layout.dialogo_seleciona_data_recorrencia, null);
        int anoSel = this.atualizaNumberPickerAno(view);
        this.atualizaNumberPickerMes(view, anoSel);

        LibFinanceMessage.criaAlerta(this)
                .setStyle(Style.HEADER_WITH_ICON)
                .setHeaderColor(R.color.green)
                .setIcon(R.drawable.ic_money_plus)
                .setTitle(null)
                .setCustomView(view, 20, 20, 20, 0)
                .setNegative(getResources().getString(R.string.cancelar), null)
                .setPositive(getResources().getString(R.string.salvar), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        NumberPicker npAno = (NumberPicker) view.findViewById(R.id.numberPickerDialogoSelecionaDataRecorrenciaAno);
                        NumberPicker npMes = (NumberPicker) view.findViewById(R.id.numberPickerDialogoSelecionaDataRecorrenciaMes);

                        int ano = npAno.getValue();
                        int mes = npMes.getValue();

                        Calendar cal = Calendar.getInstance();
                        int dia = cal.get(Calendar.DAY_OF_MONTH);

                        String dataLimiteRecorrencia = String.format("%04d-%02d-%02d", ano, mes, dia);
                        try {
                            CreditoService.criarCreditoRecorrente(descricaoCredito, valorCredito, dataLimiteRecorrencia);
                            atualizarListaCreditos();
                        } catch (Exception e) {
                            dialog.dismiss();
                            LibFinanceMessage.show(CreditosActivity.this, e);
                        }
                    }
                })
                .show();
    }

    /**
     * <p><b>Autoria: </b>Alysson Myller - 08/08/2013</p>
     */
    private int atualizaNumberPickerAno(final View view){
        String[] anos = DateUtils.get5Anos();
        int anoSelecionado = Integer.parseInt(anos[0]);

        NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.numberPickerDialogoSelecionaDataRecorrenciaAno);
        numberPicker.setMinValue(anoSelecionado);
        numberPicker.setMaxValue(Integer.parseInt(anos[anos.length-1]));
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setDisplayedValues(anos);
        numberPicker.setValue(anoSelecionado);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                // De acordo com o ano, os meses exibidos devem ser diferentes.
                atualizaNumberPickerMes(view, newVal);
            }
        });

        return anoSelecionado;
    }

    /**
     * <p><b>Autoria: </b>Alysson Myller - 08/08/2013</p>
     */
    private void atualizaNumberPickerMes(View view, int anoSelecionado) {

        // Caso seja selecionado o ano atual, deve-se exibir somente os meses restantes do ano.
        String[] meses = DateUtils.getMesesSelecaoDeAcordoComAno(anoSelecionado);

        NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.numberPickerDialogoSelecionaDataRecorrenciaMes);
        numberPicker.setMaxValue(Integer.parseInt(meses[meses.length-1]));

        try {
            numberPicker.setMinValue(Integer.parseInt(meses[0]));
        } catch (ArrayIndexOutOfBoundsException e) {
            // Não sei porque o sistema dá exceção mas continua funcionando normal !
            // Depois vou olhar =) TODO ALYSSON.
        }

        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setDisplayedValues(meses);
        numberPicker.setValue(Integer.parseInt(meses[0]));
    }

    /**
     * @param descricao
     * @param valor
     * @return
     * <p><b>Autoria: </b>Alysson Myller - 16/07/2013</p>
     */
    private boolean validaDadosInformados(String descricao, BigDecimal valor) {
        return descricao != null && !descricao.isEmpty() && valor != null && valor.compareTo(BigDecimal.ZERO) > 0;
    }
    /**
     * <p><b>Autoria: </b>Alysson Myller - 16/07/2013</p>
     */
    private void exibirDialogDadosInformadosInvalidos() {
        LibFinanceMessage.show(this, getString(R.string.dados_informados_incorretos_por_favor_verifique_));
    }

}

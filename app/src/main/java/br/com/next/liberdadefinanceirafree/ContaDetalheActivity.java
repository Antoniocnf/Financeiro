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
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.next.liberdadefinanceirafree.adapter.ContaDetalheListAdapter;
import br.com.next.liberdadefinanceirafree.model.Conta;
import br.com.next.liberdadefinanceirafree.model.Debito;
import br.com.next.liberdadefinanceirafree.service.ContaService;
import br.com.next.liberdadefinanceirafree.service.DebitoRecorrenteService;
import br.com.next.liberdadefinanceirafree.service.DebitoService;
import br.com.next.liberdadefinanceirafree.service.ParametroChaveValorService;
import br.com.next.liberdadefinanceirafree.system.Constantes;
import br.com.next.liberdadefinanceirafree.system.LibFinanceMessage;
import br.com.next.liberdadefinanceirafree.util.AndroidMoneyTextWatcher;
import br.com.next.liberdadefinanceirafree.util.DateUtils;
import br.com.next.liberdadefinanceirafree.util.IntentParameterUtil;
import br.com.next.liberdadefinanceirafree.util.PrecoUtils;

public class ContaDetalheActivity extends GenericActivity {

    private static final int CONTEXT_MENU_REMOVER = 1;

    private Long idConta;
    private BigDecimal valorTotalCreditoConta;

    private Debito debitoSel;
    private String dataLimiteRecorrencia;
    private String descricaoDebito;
    private BigDecimal valorDebito;

    private TextView txtViewSubtitulo;
    private TextView txtViewMsgNaoHaDebitos;
    private TextView txtViewDescTotalConta;
    private TextView txtViewTotalConta;
    private ListView listViewDebitos;
    private TextView txtViewTotalParaInvestir;
    private NumberProgressBar numberProgressBar;

    @Override
    protected void create(Bundle savedInstanceState) throws Exception {
        setContentView(R.layout.activity_conta_detalhe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exibirDialogAddDebito();
            }
        });

        this.idConta = getIntent().getExtras().getLong(IntentParameterUtil.ID_CONTA);

        this.txtViewSubtitulo = (TextView) findViewById(R.id.txtViewContaDetalheSubtitulo);
        this.txtViewMsgNaoHaDebitos = (TextView) findViewById(R.id.txtViewContaDetalheMsgNaoExisteDebitos);
        this.txtViewTotalConta = (TextView) findViewById(R.id.txtViewContaDetalheTotalConta);
        this.txtViewDescTotalConta = (TextView) findViewById(R.id.txtViewContaDetalheDescTotalConta);
        this.txtViewTotalParaInvestir = (TextView) findViewById(R.id.txtViewContaDetalheTotalParaInvestir);
        this.listViewDebitos = (ListView) findViewById(R.id.listViewContaDetalhe);
        this.numberProgressBar = (NumberProgressBar) findViewById(R.id.progressBarContaDetalhe);

        this.init();
    }

    /**
     * <p><b>Autoria: </b>Alysson Myller - 14/06/2013</p>
     */
    private void exibirDialogAddDebito() {
        final MaterialStyledDialog alert = LibFinanceMessage.criaAlerta(this);

        View view = LayoutInflater.from(this).inflate(R.layout.add_debito, null);
        final EditText editTextDescricao = (EditText) view.findViewById(R.id.editTextAddDebitoDescricao);
        final EditText editTextValor = (EditText) view.findViewById(R.id.editTextAddDebitoValor);
        final CheckBox checkBoxRecorrente = (CheckBox) view.findViewById(R.id.checkBoxAddDebitoRecorrente);
        editTextValor.addTextChangedListener(new AndroidMoneyTextWatcher(this, editTextValor));
        editTextValor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    handleSalvarDebito(editTextDescricao, editTextValor, checkBoxRecorrente);
                    alert.dismiss();
                    return true;
                }
                return false;
            }
        });

        alert.setStyle(Style.HEADER_WITH_ICON)
                .setHeaderColor(R.color.red)
                .setIcon(R.drawable.ic_money_minus)
                .setTitle(null)
                .setCustomView(view, 20, 20, 20, 0)
                .setNegative(getResources().getString(R.string.cancelar), null)
                .setPositive(getResources().getString(R.string.salvar), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        handleSalvarDebito(editTextDescricao, editTextValor, checkBoxRecorrente);
                    }
                })
                .show();
    }

    private void handleSalvarDebito(EditText editTextDescricao, EditText editTextValor, CheckBox checkBoxRecorrente){
        descricaoDebito = editTextDescricao.getText().toString();
        valorDebito = getBigDecimalValueFromEditText(editTextValor);

        Boolean recorrenteDebito = checkBoxRecorrente.isChecked();

        boolean erro = false;
        View focusView = null;
        if (descricaoDebito == null || descricaoDebito.isEmpty()){
            editTextDescricao.setError(getString(R.string.informar_este_valor));
            erro = true;
            focusView = editTextDescricao;
        } else if (valorDebito == null || valorDebito.compareTo(BigDecimal.ZERO) < 1){
            editTextValor.setError(getString(R.string.informar_este_valor));
            erro = true;
            focusView = editTextValor;
        }

        if (erro){
            focusView.requestFocus();
        } else {
            if (recorrenteDebito){
                exibirDialogoInformaRecorrencia();
            } else {
                try {
                    addDebito(descricaoDebito, valorDebito);
                } catch (Exception e) {
                    LibFinanceMessage.show(ContaDetalheActivity.this, e);
                }
            }
        }
    }

    /**
     * @param descricao
     * @param valor
     * <p><b>Autoria: </b>Alysson Myller - 05/08/2013</p>
     */
    private void addDebito(String descricao, BigDecimal valor) throws Exception {
        if (descricao == null || valor == null){
            LibFinanceMessage.show(this, getString(R.string._obrigat_rio_informar_a_descri_o_e_o_valor_do_d_bito_));
        } else {
            DebitoService.criarNovoDebito(idConta, descricao, valor);
            init();
        }
    }

    /**
     * <p><b>Autoria: </b>Alysson Myller - 05/08/2013</p>
     */
    private void exibirDialogoInformaRecorrencia() {
        View view = LayoutInflater.from(this).inflate(R.layout.seleciona_recorrencia, null);

        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroupSelecionaRecorrencia);

        MaterialStyledDialog alert = LibFinanceMessage.criaAlerta(this);
        alert.setHeaderColor(R.color.red);
        alert.setIcon(R.drawable.ic_money_minus);
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
                            addDebitoRecorrenteParaSempre();
                        } catch (Exception e) {
                            dialog.dismiss();
                            LibFinanceMessage.show(ContaDetalheActivity.this, e);
                        }

                        break;
                    case R.id.radioButtonRecorrenciaSelecionaData:
                        exibirDialogoSelecionarDataRecorrencia();
                        break;
                }
            }
        });
        alert.show();
    }

    /**
     * <p><b>Autoria: </b>Alysson Myller - 05/08/2013</p>
     */
    private void exibirDialogoSelecionarDataRecorrencia() {

        // Cria os datePickers
        final View view = LayoutInflater.from(this).inflate(R.layout.dialogo_seleciona_data_recorrencia, null);
        int anoSel = this.atualizaNumberPickerAno(view);
        this.atualizaNumberPickerMes(view, anoSel);

        LibFinanceMessage.criaAlerta(this)
                .setStyle(Style.HEADER_WITH_ICON)
                .setHeaderColor(R.color.red)
                .setIcon(R.drawable.ic_money_minus)
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

                        dataLimiteRecorrencia = String.format("%04d-%02d-%02d", ano, mes, dia);
                        try {
                            addDebitoRecorrenteComRecorrenciaLimite(dataLimiteRecorrencia);
                        } catch (Exception e) {
                            dialog.dismiss();
                            LibFinanceMessage.show(ContaDetalheActivity.this, e);
                        }
                    }
                })
                .show();
    }

    private void addDebitoRecorrenteComRecorrenciaLimite(String dataLimiteRecorrencia) {
        try {
            DebitoService.criarNovoDebitoRecorrente(idConta, descricaoDebito, valorDebito, dataLimiteRecorrencia);
            init();
        } catch (Exception e) {
            LibFinanceMessage.show(ContaDetalheActivity.this, e);
        }
    }

    /**
     * <p><b>Autoria: </b>Alysson Myller - 14/06/2013</p>
     */
    private void addDebitoRecorrenteParaSempre() throws Exception {
        if (descricaoDebito == null || valorDebito == null) {
            LibFinanceMessage.show(this, getString(R.string._obrigat_rio_informar_a_descri_o_e_o_valor_do_d_bito_));
        } else {
            // como se fosse para sempre - 1008 meses a partir de 2016 da até 2100
            Calendar hoje = Calendar.getInstance();
            hoje.setTime(new Date());
            hoje.add(Calendar.MONTH, 1008);
            dataLimiteRecorrencia = DateUtils.convertToDefaultDatabaseFormat(hoje.getTime());
            DebitoService.criarNovoDebitoRecorrente(idConta, descricaoDebito, valorDebito, dataLimiteRecorrencia);
            init();
        }
    }

    /**
     * @return
     * <p><b>Autoria: </b>Alysson Myller - 05/08/2013</p>
     */
    private String[] getOpcoesInformaRecorrencia() {
        String opcaoRecorrenciaParaSempre = getString(R.string.opcao_recorrencia_para_sempre);
        String opcaoRecorrenciaSelecionarData = getString(R.string.opcao_recorrencia_selecionar_data);
        return new String[]{opcaoRecorrenciaParaSempre, opcaoRecorrenciaSelecionarData};
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
     * <p><b>Autoria: </b>Alysson Myller - 10/06/2013</p>
     */
    private void init() throws Exception {
        Conta conta = ContaService.buscarPorId(idConta);

        this.valorTotalCreditoConta = PrecoUtils.getValorTotalContaMes(ContaService.buscarValorTotalCreditoMesAtual(), conta.getPercentualMes());

        String nomeConta = ContaService.getDescricaoNomeConta(this, conta.getTipoConta());
        getSupportActionBar().setTitle(nomeConta);

        int dias = ParametroChaveValorService.getDiasDesdeOInicioDoSistema();
        if (dias >= Constantes.PRAZO_EXIBIR_DETALHE_CONTA){
            this.txtViewSubtitulo.setVisibility(View.GONE);
        } else {
            this.txtViewSubtitulo.setText(ContaService.getDescricaoDetalheConta(this, conta.getTipoConta()));
            this.txtViewSubtitulo.setTypeface(getLightTypeface());
        }

        atualizaListaDebitos();
        atualizaValorTotalConta(valorTotalCreditoConta, idConta);
    }

    private void atualizaListaDebitos() {
        List<Debito> contas = DebitoService.getDebitosDoMesPorIdConta(idConta);
        ContaDetalheListAdapter adapter = new ContaDetalheListAdapter(this, contas);
        this.listViewDebitos.setAdapter(adapter);
        this.listViewDebitos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View arg1, int pos, long arg3) {
                debitoSel = (Debito) adapter.getItemAtPosition(pos);
                registerForContextMenu(listViewDebitos);
                return false;
            }
        });

//        if (contas == null || contas.isEmpty()){
//            this.listViewDebitos.setVisibility(View.GONE);
//
//            this.txtViewMsgNaoHaDebitos.setText(R.string.n_o_h_d_bitos_cadastrados_nesta_conta_para_cadastrar_selecione_o_menu_add_d_bito_);
//            this.txtViewMsgNaoHaDebitos.setTypeface(getCondensedTypeface());
//            this.txtViewMsgNaoHaDebitos.setVisibility(View.VISIBLE);
//        } else {
//            this.listViewDebitos.setVisibility(View.VISIBLE);
//            this.txtViewMsgNaoHaDebitos.setVisibility(View.GONE);
//        }
    }

    private void atualizaValorTotalConta(BigDecimal valorTotalCreditoConta, Long idConta) {
        BigDecimal valorTotalDebitosConta = ContaService.buscarValorTotalDebitoContaMesAtual(idConta);

        String valorConta = PrecoUtils.formatoPadrao(valorTotalCreditoConta);
        this.txtViewTotalParaInvestir.setText(valorConta);

        this.txtViewTotalConta.setText(PrecoUtils.formatoPadrao(valorTotalDebitosConta));

        Integer porcentagem = PrecoUtils.calculaPorcentagemDeValorUtilizado(valorTotalDebitosConta, valorTotalCreditoConta);
        if (porcentagem > 100){
            numberProgressBar.setProgress(100);
            numberProgressBar.setProgressTextVisibility(NumberProgressBar.ProgressTextVisibility.Invisible);
        } else {
            numberProgressBar.setProgress(porcentagem);
        }
    }

    @Override
    public void createContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle(R.string.selecione_);
        menu.add(0, CONTEXT_MENU_REMOVER, 0, R.string.remover);
    }

    @Override
    public void contextItemSelected(MenuItem item) throws Exception {
        switch (item.getItemId()) {
            case CONTEXT_MENU_REMOVER:
                if (debitoSel.getIdDebitoRecorrente() != null && debitoSel.getIdDebitoRecorrente() > 0){
                    exibirDialogDebitoRecorrente();
                } else {
                    DebitoService.removerPorId(debitoSel.getId());
                    init();
                }
                break;

            default:
                break;
        }
    }

    /**
     * <p><b>Autoria: </b>Alysson Myller - 27/07/2013</p>
     */
    private void exibirDialogDebitoRecorrente() {
        LibFinanceMessage.criaAlerta(this)
                .setStyle(Style.HEADER_WITH_ICON)
                .setIcon(R.drawable.ic_money_flow)
                .setDescription(getResources().getString(R.string.este_um_d_bito_recorrente_voc_deseja_apagar_somente_esse_ou_todos_))
                .setNeutral(getResources().getString(R.string.cancelar), null)
                .setNegative(getResources().getString(R.string.somente_esse), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        DebitoService.removerPorId(debitoSel.getId());
                        try {
                            init();
                        } catch (Exception e) {
                            dialog.dismiss();
                            LibFinanceMessage.show(ContaDetalheActivity.this, e);
                        }
                    }
                })
                .setPositive(getResources().getString(R.string.todos), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        DebitoService.removerTodosRecorrentes(debitoSel.getIdDebitoRecorrente());
                        DebitoRecorrenteService.removerPorId(debitoSel.getIdDebitoRecorrente());
                        try {
                            init();
                        } catch (Exception e) {
                            dialog.dismiss();
                            LibFinanceMessage.show(ContaDetalheActivity.this, e);
                        }
                    }
                })
                .show();

    }

}

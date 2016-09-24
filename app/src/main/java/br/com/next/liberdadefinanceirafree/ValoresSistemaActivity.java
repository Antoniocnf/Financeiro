package br.com.next.liberdadefinanceirafree;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import br.com.next.liberdadefinanceirafree.enums.TipoConta;
import br.com.next.liberdadefinanceirafree.model.Conta;
import br.com.next.liberdadefinanceirafree.model.Parametro;
import br.com.next.liberdadefinanceirafree.service.ContaService;
import br.com.next.liberdadefinanceirafree.service.CreditoService;
import br.com.next.liberdadefinanceirafree.service.ParametroService;
import br.com.next.liberdadefinanceirafree.service.SistemaService;
import br.com.next.liberdadefinanceirafree.system.Constantes;
import br.com.next.liberdadefinanceirafree.system.LibFinanceMessage;
import br.com.next.liberdadefinanceirafree.util.AndroidMoneyTextWatcher;
import br.com.next.liberdadefinanceirafree.util.PrecoUtils;

public class ValoresSistemaActivity extends GenericActivity {

    private HashMap<Integer, Integer> mapTipoContaXPercentual;
    private List<Conta> contas;

    private TableRow tableRowValorRendimento;
    private TextView txtViewValorRendimento;

    private TableRow tableRowDiaRecebimento;
    private TextView txtViewDiaRecebimento;

    private ToggleButton toggleButtonEditaPorcentagemContas;
    private TableRow tableRowPorcentagemLiberdadeFinanceira;
    private TextView txtViewValorPorcentagemLiberdadeFinanceira;

    private TableRow tableRowPorcentagemDiversao;
    private TextView txtViewValorPorcentagemDiversao;

    private TableRow tableRowPorcentagemPoupanca;
    private TextView txtViewValorPorcentagemPoupanca;

    private TableRow tableRowPorcentagemInstrucaoFinanceira;
    private TextView txtViewValorPorcentagemInstrucaoFinanceira;

    private TableRow tableRowPorcentagemNecessidadesBasicas;
    private TextView txtViewValorPorcentagemNecessidadesBasicas;

    private TableRow tableRowPorcentagemDoacoes;
    private TextView txtViewValorPorcentagemDoacoes;

    private Button buttonSalvarPorcentagem;

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_valores_sitema);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.tableRowValorRendimento = (TableRow) findViewById(R.id.tableRowValoresSistemaValorRendimento);
        this.txtViewValorRendimento = (TextView) findViewById(R.id.txtViewValoresSistemaValorRendimento);

        this.tableRowDiaRecebimento = (TableRow) findViewById(R.id.tableRowValoresSistemaDiaRecebimento);
        this.txtViewDiaRecebimento = (TextView) findViewById(R.id.txtViewValoresSistemaDiaRecebimento);

        this.toggleButtonEditaPorcentagemContas = (ToggleButton) findViewById(R.id.toggleButtonValoresSistemaEditarContas);
        this.tableRowPorcentagemLiberdadeFinanceira = (TableRow) findViewById(R.id.tableRowValoresSistemaContaLiberdadeFinanceira);
        this.txtViewValorPorcentagemLiberdadeFinanceira = (TextView) findViewById(R.id.txtViewValoresSistemaPorcentagemLiberdadeFinanceira);

        this.tableRowPorcentagemDiversao = (TableRow) findViewById(R.id.tableRowValoresSistemaContaDiversao);
        this.txtViewValorPorcentagemDiversao = (TextView) findViewById(R.id.txtViewValoresSistemaPorcentagemDiversao);

        this.tableRowPorcentagemPoupanca = (TableRow) findViewById(R.id.tableRowValoresSistemaContaPoupanca);
        this.txtViewValorPorcentagemPoupanca = (TextView) findViewById(R.id.txtViewValoresSistemaPorcentagemPoupanca);

        this.tableRowPorcentagemInstrucaoFinanceira = (TableRow) findViewById(R.id.tableRowValoresSistemaContaInstrucaoFinanceira);
        this.txtViewValorPorcentagemInstrucaoFinanceira = (TextView) findViewById(R.id.txtViewValoresSistemaPorcentagemInstrucaoFinanceira);

        this.tableRowPorcentagemNecessidadesBasicas = (TableRow) findViewById(R.id.tableRowValoresSistemaContaNecessidadesBasicas);
        this.txtViewValorPorcentagemNecessidadesBasicas = (TextView) findViewById(R.id.txtViewValoresSistemaPorcentagemNecessidadesBasicas);

        this.tableRowPorcentagemDoacoes = (TableRow) findViewById(R.id.tableRowValoresSistemaContaDoacoes);
        this.txtViewValorPorcentagemDoacoes = (TextView) findViewById(R.id.txtViewValoresSistemaPorcentagemDoacoes);

        this.buttonSalvarPorcentagem = (Button) findViewById(R.id.buttonValoresSistemaSalvar);

        this.init();
    }

    /**
     * <p><b>Autoria: </b>Alysson Myller - 01/08/2013</p>
     */
    private void init() {
        // Atualiza os valores referente ao rendimento mensal
        atualizaValoresRendimentoMensal();

        // Atualiza os valores referente as contas
        contas = ContaService.buscarTodas();
        for (Conta conta : contas){
            atualizaValoresConta(conta);
        }

        // Atualiza o valor das porcentagens das contas
        this.liberaBloqueiaEdicaoPorcentagemConta(false);
        this.toggleButtonEditaPorcentagemContas.setChecked(false);
        this.toggleButtonEditaPorcentagemContas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()){
                    liberaBloqueiaEdicaoPorcentagemConta(true);
                } else {
                    liberaBloqueiaEdicaoPorcentagemConta(false);
                }
            }
        });

        // Atualiza o mapa com os valores que já estão no banco de dados
        this.atualizaMapaTipoContaXPorcentagem(contas);
    }

    @Override
    protected void createOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_valores_sistema, menu);
    }

    @Override
    protected void optionsItemSelected(MenuItem item) throws Exception {
        switch (item.getItemId()){
            case R.id.action_restaurar_valores:
                exibirDialogoConfirmacaoRestaurarValores();
                break;
        }
    }

    /**
     * <p><b>Autoria: </b>Alysson Myller - 10/07/2013</p>
     */
    private void exibirDialogoConfirmacaoRestaurarValores() {
        MaterialStyledDialog alert = LibFinanceMessage.criaAlerta(this);
        alert.setTitle(R.string.tem_certeza);
        alert.setDescription(R.string.confirma_a_restaura_o_completa_dos_dados_);
        alert.setNegative(getString(R.string.cancelar), null);
        alert.setPositive(getString(R.string.sim), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                restaurarValores();
            }
        });
        alert.show();
    }

    /**
     * <p><b>Autoria: </b>Alysson Myller - 14/06/2013</p>
     */
    public void restaurarValores() {
        SistemaService.restaurarValores();

        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = app_preferences.edit();
        editor.putString(Constantes.SHARED_PREFERENCES_PIN, null);
        editor.commit();

        startActivity(new Intent(this, MainActivity.class));
    }

    /**
     * Atualiza o mapa com os valores que já estão no banco de dados
     * <p><b>Autoria: </b>Alysson Myller - 01/08/2013</p>
     */
    private void atualizaMapaTipoContaXPorcentagem(List<Conta> contas) {
        this.mapTipoContaXPercentual = new HashMap<Integer, Integer>();
        for (Conta conta : contas){
            mapTipoContaXPercentual.put(conta.getTipoConta(), conta.getPercentualMes().intValue());
        }
    }

    /**
     * @param liberaBloqueia
     * <p><b>Autoria: </b>Alysson Myller - 01/08/2013</p>
     */
    private void liberaBloqueiaEdicaoPorcentagemConta(boolean liberaBloqueia) {
        this.tableRowPorcentagemLiberdadeFinanceira.setEnabled(liberaBloqueia);
        this.tableRowPorcentagemLiberdadeFinanceira.setFocusable(liberaBloqueia);
        this.txtViewValorPorcentagemLiberdadeFinanceira.setTextColor(liberaBloqueia ? Color.BLACK : Color.GRAY);

        this.tableRowPorcentagemDiversao.setEnabled(liberaBloqueia);
        this.tableRowPorcentagemDiversao.setFocusable(liberaBloqueia);
        this.txtViewValorPorcentagemDiversao.setTextColor(liberaBloqueia ? Color.BLACK : Color.GRAY);

        this.tableRowPorcentagemPoupanca.setEnabled(liberaBloqueia);
        this.tableRowPorcentagemPoupanca.setFocusable(liberaBloqueia);
        this.txtViewValorPorcentagemPoupanca.setTextColor(liberaBloqueia ? Color.BLACK : Color.GRAY);

        this.tableRowPorcentagemInstrucaoFinanceira.setEnabled(liberaBloqueia);
        this.tableRowPorcentagemInstrucaoFinanceira.setFocusable(liberaBloqueia);
        this.txtViewValorPorcentagemInstrucaoFinanceira.setTextColor(liberaBloqueia ? Color.BLACK : Color.GRAY);

        this.tableRowPorcentagemNecessidadesBasicas.setEnabled(liberaBloqueia);
        this.tableRowPorcentagemNecessidadesBasicas.setFocusable(liberaBloqueia);
        this.txtViewValorPorcentagemNecessidadesBasicas.setTextColor(liberaBloqueia ? Color.BLACK : Color.GRAY);

        this.tableRowPorcentagemDoacoes.setEnabled(liberaBloqueia);
        this.tableRowPorcentagemDoacoes.setFocusable(liberaBloqueia);
        this.txtViewValorPorcentagemDoacoes.setTextColor(liberaBloqueia ? Color.BLACK : Color.GRAY);

        this.buttonSalvarPorcentagem.setVisibility(liberaBloqueia ? View.VISIBLE : View.GONE);
        this.buttonSalvarPorcentagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarPorcentagemConta();
            }
        });
    }

    /**
     * <p><b>Autoria: </b>Alysson Myller - 01/08/2013</p>
     */
    private void salvarPorcentagemConta() {
        Integer valorTotal = 0;
        for (Conta conta : contas){
            valorTotal += mapTipoContaXPercentual.get(conta.getTipoConta());
        }

        if (valorTotal != 100){
            LibFinanceMessage.show(this, getString(R.string.valores_sistema_total_porc_nao_confere));
        } else {
            MaterialStyledDialog alert = LibFinanceMessage.criaAlerta(this);
            alert.setDescription(getString(R.string.valores_sistema_salvar_alteracao_porcentagem));
            alert.setNegative(getResources().getString(R.string.nao), new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    init();
                }
            });
            alert.setPositive(getResources().getString(R.string.sim), new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    for (Conta conta : contas){
                        Integer porcentagem = mapTipoContaXPercentual.get(conta.getTipoConta());
                        ContaService.alterarPorcentagemConta(conta.getTipoConta(), porcentagem);
                    }
                    init();
                }
            });
            alert.show();
        }
    }

    /**
     * Atualiza os valores parametrizados
     * <p><b>Autoria: </b>Alysson Myller - 01/08/2013</p>
     */
    private void atualizaValoresRendimentoMensal() {
        this.tableRowValorRendimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exibirDialogoAlterarRendimentoMensal();
            }
        });

        final Parametro parametro = ParametroService.getParametro();
        this.txtViewValorRendimento.setText(PrecoUtils.formatoPadrao(parametro.getValorRendimentoMensal()));

        this.tableRowDiaRecebimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exibirDialogoAlteraDiaRecebimento(parametro);
            }
        });

        this.txtViewDiaRecebimento.setText(String.valueOf(parametro.getDiaRecebimento()));
    }

    /**
     * <p><b>Autoria: </b>Alysson Myller - 01/08/2013</p>
     */
    private void exibirDialogoAlteraDiaRecebimento(Parametro param) {
        final String[] nums = new String[28];
        for (int i = 0; i < nums.length; i ++){
            nums[i] = String.valueOf(i+1);
        }

        View view = LayoutInflater.from(this).inflate(R.layout.insere_valor, null);
        final NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.numberPickerInsereValor);
        numberPicker.setMaxValue(28);
        numberPicker.setMinValue(1);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setDisplayedValues(nums);
        numberPicker.setValue(param.getDiaRecebimento());

        MaterialStyledDialog alert = LibFinanceMessage.criaAlerta(this);
        alert.setTitle("Selecione o Dia");
        alert.setCustomView(view, 20, 20, 20, 0);
        alert.setNegative(getResources().getString(R.string.cancelar), null);
        alert.setPositive(getResources().getString(R.string.salvar), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                int value = numberPicker.getValue();
                Integer valor = Integer.valueOf(nums[value - 1]);
                txtViewDiaRecebimento.setText(String.valueOf(valor));
                ParametroService.alteraDiaRecebimento(valor);
            }
        });
        alert.show();
    }

    /**
     * @param conta
     * <p><b>Autoria: </b>Alysson Myller - 01/08/2013</p>
     */
    private void atualizaValoresConta(final Conta conta) {

        switch (TipoConta.getInstance(conta.getTipoConta())) {
            case LIBERDADE_FINANCEIRA:
                this.tableRowPorcentagemLiberdadeFinanceira.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exibirDialogoAlteraPorcentagemConta(conta, txtViewValorPorcentagemLiberdadeFinanceira);
                    }
                });

                this.txtViewValorPorcentagemLiberdadeFinanceira.setText(String.format("%d%s", conta.getPercentualMes(), "%"));
                break;

            case DIVERSAO:
                this.tableRowPorcentagemDiversao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exibirDialogoAlteraPorcentagemConta(conta, txtViewValorPorcentagemDiversao);
                    }
                });

                this.txtViewValorPorcentagemDiversao.setText(String.format("%d%s", conta.getPercentualMes(), "%"));
                break;

            case POUPANCA:
                this.tableRowPorcentagemPoupanca.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exibirDialogoAlteraPorcentagemConta(conta, txtViewValorPorcentagemPoupanca);
                    }
                });
                this.txtViewValorPorcentagemPoupanca.setText(String.format("%d%s", conta.getPercentualMes(), "%"));
                break;

            case INSTRUCAO_FINANCEIRA:
                this.tableRowPorcentagemInstrucaoFinanceira.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exibirDialogoAlteraPorcentagemConta(conta, txtViewValorPorcentagemInstrucaoFinanceira);
                    }
                });
                this.txtViewValorPorcentagemInstrucaoFinanceira.setText(String.format("%d%s", conta.getPercentualMes(), "%"));
                break;

            case NECESSIDADES_BASICAS:
                this.tableRowPorcentagemNecessidadesBasicas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exibirDialogoAlteraPorcentagemConta(conta, txtViewValorPorcentagemNecessidadesBasicas);
                    }
                });
                this.txtViewValorPorcentagemNecessidadesBasicas.setText(String.format("%d%s", conta.getPercentualMes(), "%"));
                break;

            case DOACOES:
                this.tableRowPorcentagemDoacoes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exibirDialogoAlteraPorcentagemConta(conta, txtViewValorPorcentagemDoacoes);
                    }
                });
                this.txtViewValorPorcentagemDoacoes.setText(String.format("%d%s", conta.getPercentualMes(), "%"));
                break;

            default:
                break;
        }
    }

    /**
     * @param txtViewParaAlterar
     * @param conta
     * <p><b>Autoria: </b>Alysson Myller - 01/08/2013</p>
     */
    private void exibirDialogoAlteraPorcentagemConta(final Conta conta, final TextView txtViewParaAlterar) {
        final String[] nums = new String[]{
                "0", "5", "10", "15", "20", "25",
                "30", "35", "40", "45", "50",
                "55", "60", "65", "70", "75",
                "80", "85", "90", "95", "100"};

        View view = LayoutInflater.from(this).inflate(R.layout.insere_valor, null);
        final NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.numberPickerInsereValor);
        numberPicker.setMaxValue(21);
        numberPicker.setMinValue(1);
        numberPicker.setDisplayedValues(nums);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        // Obtém o valor que fica no mapa (não vai para o banco enquanto não selecionar salvar)
        int valorPorcentagem = (mapTipoContaXPercentual.get(conta.getTipoConta()) / 5) + 1;
        numberPicker.setValue(valorPorcentagem);

        MaterialStyledDialog alert = LibFinanceMessage.criaAlerta(this);
        alert.setTitle(R.string.porcentagem);
        alert.setCustomView(view, 20, 20, 20, 0);
        alert.setNegative(getResources().getString(R.string.cancelar), null);
        alert.setPositive(getResources().getString(R.string.salvar), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                int value = numberPicker.getValue();
                Integer valor = Integer.valueOf(nums[value - 1]);

                txtViewParaAlterar.setText(String.format("%d%s", valor, "%"));
                mapTipoContaXPercentual.put(conta.getTipoConta(), valor);
            }
        });
        alert.show();
    }

    /**
     * <p><b>Autoria: </b>Alysson Myller - 14/06/2013</p>
     */
    private void exibirDialogoAlterarRendimentoMensal() {
        View view = LayoutInflater.from(this).inflate(R.layout.altera_rendimento, null);
        final EditText editText = (EditText) view.findViewById(R.id.editTextAlteraRendimento);
        editText.addTextChangedListener(new AndroidMoneyTextWatcher(this, editText));


        MaterialStyledDialog alert = LibFinanceMessage.criaAlerta(this);

        alert.setCustomView(view, 20, 20, 20, 0);
        alert.setTitle(getResources().getString(R.string.digite_o_valor));
        alert.setNegative(getResources().getString(R.string.cancelar), null);
        alert.setPositive(getResources().getString(R.string.salvar), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                BigDecimal valor = getBigDecimalValueFromEditText(editText);

                if (SistemaService.validaDadosInformados(valor)){
                    alterarRendimento(valor);
                    txtViewValorRendimento.setText(PrecoUtils.formatoPadrao(valor));
                } else {
                    LibFinanceMessage.show(ValoresSistemaActivity.this, getString(R.string.dados_informados_incorretos_por_favor_verifique_));
                }
            }
        });
        alert.show();
    }

    /**
     * @param valor
     * <p><b>Autoria: </b>Alysson Myller - 14/06/2013</p>
     */
    private void alterarRendimento(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) < 0){
            LibFinanceMessage.show(this, getString(R.string._obrigat_rio_informar_o_valor));
        } else {
            CreditoService.alterarRendimentoMensal(this, valor);
        }
    }

}

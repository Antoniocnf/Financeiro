package br.com.next.liberdadefinanceirafree;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

import br.com.next.liberdadefinanceirafree.enums.TipoCredito;
import br.com.next.liberdadefinanceirafree.service.CreditoService;
import br.com.next.liberdadefinanceirafree.service.ParametroService;
import br.com.next.liberdadefinanceirafree.util.AndroidMoneyTextWatcher;
import br.com.next.liberdadefinanceirafree.util.DatePatternUtils;
import br.com.next.liberdadefinanceirafree.util.DateUtils;

public class ThirdFragment extends Fragment {

    private EditText editTextRendimento;
    private EditText editTextSaldoAtual;
    private CheckBox checkBoxSemRendimentoMensal;
    private Button buttonComecar;

    private boolean temRendimentoMensal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        temRendimentoMensal = true;

        View v = inflater.inflate(R.layout.third_frag, container, false);

        TextView tv = (TextView) v.findViewById(R.id.tvFragThird);
        tv.setText(getArguments().getString("msg"));
        tv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf"));

        editTextRendimento = (EditText) v.findViewById(R.id.editTextMainRendimento);
        editTextRendimento.requestFocus();
        editTextRendimento.addTextChangedListener(new AndroidMoneyTextWatcher(getActivity(), editTextRendimento));

        editTextSaldoAtual = (EditText) v.findViewById(R.id.editTextMainSaldoAtual);
        editTextSaldoAtual.addTextChangedListener(new AndroidMoneyTextWatcher(getActivity(), editTextSaldoAtual));

        checkBoxSemRendimentoMensal = (CheckBox) v.findViewById(R.id.checkBoxMainSemRendimentoMensal);
        checkBoxSemRendimentoMensal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox check = (CheckBox) v;
                if (check.isChecked()){
                    temRendimentoMensal = false;
                    editTextRendimento.setEnabled(false);
                } else {
                    temRendimentoMensal = true;
                    editTextRendimento.setEnabled(true);
                }
            }
        });

        buttonComecar = (Button) v.findViewById(R.id.buttonMainComecar);
        buttonComecar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean erro = false;

                BigDecimal valorCreditoMensal = null;
                if (temRendimentoMensal){
                    valorCreditoMensal = getBigDecimalValueFromEditText(editTextRendimento);
                    if (valorCreditoMensal.compareTo(BigDecimal.ZERO) < 1) {
                        editTextRendimento.setError(getString(R.string.informar_este_valor));
                        erro = true;
                    }
                }

                if (erro){
                    editTextRendimento.requestFocus();
                } else {
                    String dataHoje = DateUtils.now(DatePatternUtils.YYYYMMDD);
                    ParametroService.criaParametro(valorCreditoMensal, 1, dataHoje);

                    BigDecimal valorSaldoAtual = getBigDecimalValueFromEditText(editTextSaldoAtual);
                    CreditoService.criarCredito(getActivity(), getResources().getString(R.string.saldo_atual), valorSaldoAtual, TipoCredito.NOVO_CREDITO);

                    getActivity().finish();

                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

        return v;
    }

    /**
     * @param field
     * @return <p><b>Autoria: </b>Alysson Myller - 06/11/2012</p>
     */
    public BigDecimal getBigDecimalValueFromEditText(EditText field) {
        if (field != null && field.getText() != null && field.getText().length() > 0) {
            String value = getOnlyDigits(field.getText().toString());
            return new BigDecimal(value).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_EVEN);
        }
        return BigDecimal.ZERO;
    }

    /**
     * Obtém somente os números do parâmetro recebido
     *
     * @param value
     * @return <p><b>Autoria: </b>Alysson Myller - 08/11/2012</p>
     */
    private String getOnlyDigits(String value) {
        String res = "";
        for (Character s : value.toCharArray()) {
            if (Character.isDigit(s)) {
                res = res + s;
            }
        }
        return res;
    }

    public static ThirdFragment newInstance(String text) {

        ThirdFragment f = new ThirdFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}
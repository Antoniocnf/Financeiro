package br.com.next.liberdadefinanceirafree;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Window;
import android.view.WindowManager;

import java.text.ParseException;

import br.com.next.liberdadefinanceirafree.dao.autosql.GenericRepository;
import br.com.next.liberdadefinanceirafree.service.ContaService;
import br.com.next.liberdadefinanceirafree.service.ParametroChaveValorService;
import br.com.next.liberdadefinanceirafree.service.SistemaService;

public class MainActivity extends GenericActivity {

    private static final int REQUEST_CODE_PIN = 1;

    @Override
    protected void create(Bundle savedInstanceState) throws ParseException {
        GenericRepository.setContext(getApplicationContext());

        // Verifica se já existe os dados cadastrados, ou seja, não é a primeira vez que está usando o programa.
        if (SistemaService.existeDadosJaCadastrados()) {
            this.finish();
            startActivity(new Intent(this, HomeActivity.class));
        }

        // Senão, é primeira vez que está executando o programa e não possuía o libfinance antigo (free).
        else {
            ContaService.criarDadosContas(this);
            ParametroChaveValorService.criarParametroInicioSistema();
            this.init();
        }
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_navy));
        }

        ActionBar ab = getSupportActionBar();
        if (ab != null){
            ab.hide();
        }
        setContentView(R.layout.activity_main);

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    protected void activityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_PIN:
                    this.finish();
                    startActivity(new Intent(this, HomeActivity.class));
                    break;
            }
        } else {
            finish();
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    return FirstFragment.newInstance(getResources().getString(R.string.o_sistema_ir_dividir_o_seu_rendimento));
                case 1:
                    return SecondFragment.newInstance(getResources().getString(R.string.utilize_o_valor_total_que_voc_tem_em_cada_conta));
                case 2:
                    return ThirdFragment.newInstance(getResources().getString(R.string.por_favor_informe_os_valores_abaixo));
                default:
                    return ThirdFragment.newInstance("ThirdFragment, Default");
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}

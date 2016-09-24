package br.com.next.liberdadefinanceirafree.system;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;

import br.com.next.liberdadefinanceirafree.R;

/**
 * Classe utilitária para interagir com usuario atraves de mensagens
 *
 * @author Alysson Myller
 * @since 09/2011
 */
public abstract class LibFinanceMessage {

    /**
     * @param context
     * @param e       <p><b>Autoria: </b>Alysson Myller / Maycon César - 25/10/2012</p>
     */
    public static void show(Context context, Throwable e) {
        criaAlerta(context)
                .setTitle(R.string.erro)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setDescription(e.getMessage() != null ? e.getMessage() : context.getResources().getString(R.string.erro_inesperado))
                .setScrollable(true)
                .setNeutral(context.getResources().getString(R.string.ok), null)
                .show();
    }

    /**
     * @param context <p><b>Autoria: </b>Alysson Myller / Maycon César - 25/10/2012</p>
     */
    public static void show(Context context, String msg) {
        criaAlerta(context)
                .setTitle(Constantes.APP_NAME)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setDescription(msg)
                .setNeutral(context.getResources().getString(R.string.ok), null)
                .show();
    }

    /**
     * @param context
     * @return <p><b>Autoria: </b>Alysson Myller - 25/10/2012</p>
     */
    public static MaterialStyledDialog criaAlerta(Context context) {
        return new MaterialStyledDialog(context)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setTitle(Constantes.APP_NAME)
                .withIconAnimation(false);
    }

    /**
     * @param context
     * @return <p><b>Autoria: </b>Alysson Myller - 23/09/2016</p>
     */
    public static void showProFeature(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.go_pro_description, null);
        Button button = (Button) view.findViewById(R.id.buttonProEuQuero);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
            }
        });

        new MaterialStyledDialog(context)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setTitle("Vem Ser PRO")
                .withIconAnimation(false)
                .setHeaderDrawable(R.drawable.pro_header_image)
                .setCustomView(view)
                .show();
    }

//    /**
//     * @param context
//     * @param e
//     * <p><b>Autoria: </b>Alysson Myller / Maycon César - 25/10/2012</p>
//     */
//    public static void show(Context context, Throwable e) {
//        AlertDialog.Builder alert = criaAlerta(context);
//        alert.setTitle("Erro");
//        alert.setMessage(e.getMessage() != null ? e.getMessage() : "Erro inesperado!");
//        alert.setPositiveButton("Ok", null);
//        alert.show();
//    }
//
//    /**
//     * @param context
//     * <p><b>Autoria: </b>Alysson Myller / Maycon César - 25/10/2012</p>
//     */
//    public static void show(Context context, String msg) {
//        AlertDialog.Builder alert = criaAlertaDefault(context);
//        alert.setMessage(msg);
//        alert.setPositiveButton("Ok", null);
//        alert.show();
//    }
//
//    /**
//     * @param context
//     * @return
//     * <p><b>Autoria: </b>Alysson Myller - 25/10/2012</p>
//     */
//    public static AlertDialog.Builder criaAlerta(Context context){
//        return criaAlertaDefault(context);
//    }
//
//    /**
//     * @param context
//     * @return
//     * <p><b>Autoria: </b>Alysson Myller - 26/10/2012</p>
//     */
//    private static AlertDialog.Builder criaAlertaDefault(Context context){
//        AlertDialog.Builder alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
//        alert.setTitle("::. Liberdade Financeira .::");
//        return alert;
//    }

}

package br.com.next.liberdadefinanceirafree.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Classe utilitária para datas
 * 
 * @author thiago
 * @author tadeunca
 * @since 18/08/2011
 */
public final class DateUtils {

	public static final long MILISSEGUNDOS_DIA = 86400000;
	
	private DateUtils() {
		super();
	}
	
	public static String now(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date());
	}
	
	public static String nowMinus(long minutesInMillis) {
		Date date = new Date(System.currentTimeMillis() - minutesInMillis);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	/**
	 * Converte uma data para uma String
	 */
	public static String convert(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DatePatternUtils.DDMMYYYY);
		return sdf.format(date);
	}
	
	/**
	 * converte String em data
	 * @param data
	 * @return
	 * @throws ParseException
	 */
	public static Date convertStringDate(String data) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(DatePatternUtils.DDMMYYYY);
		return df.parse(data);
	}
	
	/**
	 * Parse a string into date without throw exception
	 * 
	 * @param data
	 * @return
	 */
	public static Date stringDateOrNull(String data) {
		try {
			return convertStringDate(data);
		} catch(ParseException e) {
			return null;
		}
	}
	
	public static int getDiffDays(Date day1, Date day2) {
		Calendar date1 = Calendar.getInstance();
		date1.setTime(day1);
		date1 = getOnlyDate(date1);
		
		Calendar date2 = Calendar.getInstance();
		date2.setTime(day2);
		date2 = getOnlyDate(date2);

		int daysBetween = 0;
		while (date1.before(date2)) {
			date1.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return daysBetween;
	}
	
	/**
	 * @param c1
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 23/03/2013</p>
	 */
	private static Calendar getOnlyDate(Calendar c1){
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);
		return c1;
	}

	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 14/06/2013</p>
	 */
	public static String getDescricaoMesEAnoAtual() {
		Calendar c = Calendar.getInstance();
		String mesAtual = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
		String letraInicial = mesAtual.substring(0, 1).toUpperCase();
		String restante = mesAtual.substring(1, mesAtual.length());
		mesAtual = letraInicial + restante;
		return mesAtual + " " + c.get(Calendar.YEAR);
	}
	
	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 31/07/2013</p>
	 */
	public static String getDescricaoMesEAno(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		String mesAtual = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
		String letraInicial = mesAtual.substring(0, 1).toUpperCase();
		String restante = mesAtual.substring(1, mesAtual.length());
		mesAtual = letraInicial + restante;
		return mesAtual + " " + c.get(Calendar.YEAR);
	}

	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 14/09/2016</p>
	 */
	public static String getDescricaoReduzidaMesEAno(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		String mesAtual = c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
		String letraInicial = mesAtual.substring(0, 1).toUpperCase();
		String restante = mesAtual.substring(1, mesAtual.length());
		mesAtual = letraInicial + restante;

		String anoReduzido = String.valueOf(c.get(Calendar.YEAR)).substring(2, 4);
		return mesAtual + " " + anoReduzido;
	}
	
	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 15/06/2013</p>
	 */
	public static boolean ehDiaPrimeiroDoMes() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DAY_OF_MONTH) == 1;
	}
	
	/**
	 * Recebe uma data em string no formato yyyy-MM-dd
	 * e retorna uma data em Date.
	 * 
	 * @author 	Alysson Myller
	 * @since	23/07/2012
	 */
	public static Date convertFromDefaultDatabaseFormat(String databaseDate) throws ParseException{
		return new SimpleDateFormat(DatePatternUtils.YYYYMMDD).parse(databaseDate);
	}

	/**
	 * @param date
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 05/08/2013</p>
	 */
	public static String convertToDefaultDatabaseFormat(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DatePatternUtils.YYYYMMDD);
		return sdf.format(date);
	}

	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 08/08/2013</p>
	 */
	public static String[] get5Anos() {
		Calendar c = Calendar.getInstance();
		int anoAtual = c.get(Calendar.YEAR);
		
		String[] anos = new String[5];
		for (int i = 0; i < 5; i ++){
			anos[i] = String.valueOf(anoAtual + i);
		}
		
		return anos;
	}

	/**
	 * @param datePickerAnoSel
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 08/08/2013</p>
	 */
	public static String[] getMesesSelecaoDeAcordoComAno(Integer datePickerAnoSel) {
		Calendar c = Calendar.getInstance();
		int anoAtual = c.get(Calendar.YEAR);

		if (datePickerAnoSel == anoAtual){
			// Busca os meses a partir do próximo até o final do ano
			int mesAtual = c.get(Calendar.MONTH) + 1;
			String meses[] = new String[12 - mesAtual];
			for (int i = 0; i < 12 - mesAtual; i ++){
				meses[i] = String.valueOf(mesAtual + 1 + i);
			}
			return meses;
		} else {
			// Busca todos os meses do ano
			String meses[] = new String[12];
			for (int i = 0; i < 12; i ++){
				meses[i] = String.valueOf(i+1);
			}
			return meses;
		}
	}
	
	
	/**
	 * Recebe uma data em string no formato yyyy-MM-dd e 
	 * retorna uma data em string no format dd/MM/yyyy
	 * 
	 * @author 	Alysson Myller
	 * @throws  ParseException 
	 * @since	19/07/2012
	 */
	public static String convertFromDataBase(String databaseDate) throws ParseException{
		return new SimpleDateFormat(DatePatternUtils.DDMMYYYY).format(new SimpleDateFormat(DatePatternUtils.YYYYMMDD).parse(databaseDate));
	}

	/**
	 *
	 * @param fromFormat
	 * @param toFormat
	 * @param dataFormatada
	 * @return
	 * @throws ParseException
     */
	public static String convertFromTo(String fromFormat, String toFormat, String dataFormatada) throws ParseException {
		Date date = new SimpleDateFormat(fromFormat).parse(dataFormatada);
		return new SimpleDateFormat(toFormat).format(date);
	}

	/**
	 *
	 * @param hoje
	 * @param dataLimite
     * @return
     */
    public static int getDiffMeses(Date hoje, Date dataLimite) {
		Calendar date1 = Calendar.getInstance();
		date1.setTime(hoje);
		date1 = getOnlyDate(date1);

		Calendar date2 = Calendar.getInstance();
		date2.setTime(dataLimite);
		date2 = getOnlyDate(date2);

		int monthBetween = 0;
		while (date1.before(date2)) {
			date1.add(Calendar.MONTH, 1);
			monthBetween++;
		}
		return monthBetween;
	}

	/**
	 *
	 * @param data
	 * @return
     */
    public static String getDescricaoReduzidaDiaMes(Date data) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);

		String mes = c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
		String letraInicial = mes.substring(0, 1).toUpperCase();
		String restante = mes.substring(1, mes.length());
		mes = letraInicial + restante;

		String dia = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		return dia + " " + mes;
	}

	public static boolean ehData1AntesData2(Date data1, Date data2) {
		Calendar date1 = Calendar.getInstance();
		date1.setTime(data1);
		date1 = getOnlyDate(date1);

		Calendar date2 = Calendar.getInstance();
		date2.setTime(data2);
		date2 = getOnlyDate(date2);

		return date1.before(date2);
	}

}

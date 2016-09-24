package br.com.next.liberdadefinanceirafree.service;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.next.liberdadefinanceirafree.R;
import br.com.next.liberdadefinanceirafree.enums.TipoConta;
import br.com.next.liberdadefinanceirafree.enums.TipoMovimentacao;
import br.com.next.liberdadefinanceirafree.model.Movimentacao;
import br.com.next.liberdadefinanceirafree.system.Constantes;
import br.com.next.liberdadefinanceirafree.system.LibFinanceMessage;
import br.com.next.liberdadefinanceirafree.util.DatePatternUtils;
import br.com.next.liberdadefinanceirafree.util.DateUtils;
import br.com.next.liberdadefinanceirafree.util.PrecoUtils;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Por definição, a planilha tem como base os seguintes atributos:
 * Coluna 0 = Campo data do movimento
 * Coluna 1 = Campo tipo do movimento
 * Coluna 2 = Campo descrição da conta de débito
 * Coluna 3 = Campo descrição do movimento
 * Coluna 4 = Campo valor do movimento
 * 
 * @author Alysson Myller
 * @since  26/08/2013
 */
public class LibFinancePlanilha {
	
	private static final int QUANTIDADE_COLUNAS = 5;
	
	/**
	 * @param context 
	 * @param sheet
	 * @param linha
	 * @param key 
	 * @return
	 * @throws WriteException
	 * @throws RowsExceededException
	 * <p><b>Autoria: </b>Alysson Myller - 26/08/2013</p>
	 * @throws ParseException 
	 */
	public static int preencheCabecalho(Context context, WritableSheet sheet, int linha, String key) throws WriteException, RowsExceededException, ParseException {
		// Quebras de linha para o cabeçalho
		linha = linha + 2;
		WritableCellFormat formatoCabecalho = getFormatoCabecalho();
		
		String descricaoMesAno = getDescricaoMesEAnoPorChave(key);
		Label labelDescricaoMesAno = new Label(0, linha, descricaoMesAno, formatoCabecalho);
		sheet.addCell(labelDescricaoMesAno);
		sheet.mergeCells(0, linha, 4, linha);
		
		// Passa para a próxima linha
		linha = linha + 1;
		
		int coluna = 0;
		String descricaoColuna = getDescricaoColunaCabecalho(context, coluna);
		Label labelData = new Label(coluna, linha, descricaoColuna, formatoCabecalho);
		sheet.addCell(labelData);
		
		coluna = 1;
		descricaoColuna = getDescricaoColunaCabecalho(context, coluna);
		Label labelTipo = new Label(coluna, linha, descricaoColuna, formatoCabecalho);
		sheet.addCell(labelTipo);
		
		coluna = 2;
		descricaoColuna = getDescricaoColunaCabecalho(context, coluna);
		Label labelConta = new Label(coluna, linha, descricaoColuna, formatoCabecalho);
		sheet.addCell(labelConta);
		
		coluna = 3;
		descricaoColuna = getDescricaoColunaCabecalho(context, coluna);
		Label labelDescricao = new Label(coluna, linha, descricaoColuna, formatoCabecalho); 
		sheet.addCell(labelDescricao);

		coluna = 4;
		descricaoColuna = getDescricaoColunaCabecalho(context, coluna);
		Label labelValor = new Label(coluna, linha, descricaoColuna, formatoCabecalho);
		sheet.addCell(labelValor);
		
		// Retorna a próxima linha a ser escrita
		linha = linha + 1;
		return linha;
	}
	
	/**
	 * Coluna 0 = Campo data do movimento
	 * Coluna 1 = Campo tipo do movimento
	 * Coluna 2 = Campo descrição da conta de débito
	 * Coluna 3 = Campo descrição do movimento
	 * Coluna 4 = Campo valor do movimento
	 * 
	 * @param coluna
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 30/08/2013</p>
	 */
	private static String getDescricaoColunaCabecalho(Context context, int coluna){
		switch (coluna) {
		case 0: return context.getString(R.string.planilha_descricao_coluna_cabecalho_data_movimento);
		case 1: return context.getString(R.string.planilha_descricao_coluna_cabecalho_tipo_movimento);
		case 2: return context.getString(R.string.planilha_descricao_coluna_cabecalho_descricao_conta);
		case 3: return context.getString(R.string.planilha_descricao_coluna_cabecalho_descricao_movimento);
		case 4: return context.getString(R.string.planilha_descricao_coluna_cabecalho_valor_movimento);
		
		default: 
			return "";
		}
	}
	
	/**
	 * @param key
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 26/08/2013</p>
	 * @throws ParseException 
	 */
	private static String getDescricaoMesEAnoPorChave(String key) throws ParseException {
		key = key + "-01";
		Date date = DateUtils.convertFromDefaultDatabaseFormat(key);
		return DateUtils.getDescricaoMesEAno(date);
	}

	/**
	 * @return
	 * @throws WriteException
	 * <p><b>Autoria: </b>Alysson Myller - 26/08/2013</p>
	 */
	public static WritableCellFormat getFormatoCabecalho() throws WriteException{
		WritableFont wfontStatus = new WritableFont(WritableFont.createFont("Arial"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
	    WritableCellFormat fCellstatus = new WritableCellFormat(wfontStatus);
	    fCellstatus.setWrap(true);
	    fCellstatus.setAlignment(jxl.format.Alignment.CENTRE);
	    fCellstatus.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	    fCellstatus.setBackground(Colour.GRAY_25);
	    fCellstatus.setBorder(Border.ALL, BorderLineStyle.MEDIUM, Colour.BLACK);
	    return fCellstatus;
	}
	
	/**
	 * @return
	 * @throws WriteException
	 * <p><b>Autoria: </b>Alysson Myller - 26/08/2013</p>
	 */
	public static WritableCellFormat getFormatoNumero() throws WriteException{
		WritableFont wfontStatus = new WritableFont(WritableFont.createFont("Arial"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
	    WritableCellFormat fCellstatus = new WritableCellFormat(wfontStatus);
	    fCellstatus.setWrap(true);
	    fCellstatus.setAlignment(jxl.format.Alignment.CENTRE);
	    fCellstatus.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	    return fCellstatus;
	}
	
	/**
	 * @return
	 * @throws WriteException
	 * <p><b>Autoria: </b>Alysson Myller - 29/08/2013</p>
	 */
	public static WritableCellFormat getFormatoValorCredito() throws WriteException{
		WritableFont wfontStatus = new WritableFont(WritableFont.createFont("Arial"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.GREEN);
	    WritableCellFormat fCellstatus = new WritableCellFormat(wfontStatus);
	    fCellstatus.setWrap(true);
	    fCellstatus.setAlignment(jxl.format.Alignment.CENTRE);
	    fCellstatus.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	    return fCellstatus;
	}
	
	/**
	 * @return
	 * @throws WriteException
	 * <p><b>Autoria: </b>Alysson Myller - 29/08/2013</p>
	 */
	public static WritableCellFormat getFormatoValorDebito() throws WriteException{
		WritableFont wfontStatus = new WritableFont(WritableFont.createFont("Arial"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.RED);
	    WritableCellFormat fCellstatus = new WritableCellFormat(wfontStatus);
	    fCellstatus.setWrap(true);
	    fCellstatus.setAlignment(jxl.format.Alignment.CENTRE);
	    fCellstatus.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	    return fCellstatus;
	}
	
	/**
	 * @return
	 * @throws WriteException
	 * <p><b>Autoria: </b>Alysson Myller - 26/08/2013</p>
	 */
	public static WritableCellFormat getFormatoTexto() throws WriteException{
		WritableFont wfontStatus = new WritableFont(WritableFont.createFont("Arial"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
	    WritableCellFormat fCellstatus = new WritableCellFormat(wfontStatus);
	    fCellstatus.setWrap(true);
	    fCellstatus.setAlignment(jxl.format.Alignment.LEFT);
	    fCellstatus.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	    return fCellstatus;
	}
	
	/**
	 * Coluna 0 = Campo data do movimento
	 * Coluna 1 = Campo tipo do movimento
	 * Coluna 2 = Campo descrição da conta de débito
	 * Coluna 3 = Campo descrição do movimento
	 * Coluna 4 = Campo valor do movimento
	 * 
	 * @param context
	 * @param coluna
	 * @param m
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 26/08/2013</p>
	 * @throws ParseException 
	 */
	public static String getDescricaoConteudo(Context context, int coluna, Movimentacao m) throws ParseException{
    	switch (coluna) {
		case 0: return DateUtils.convertFromDataBase(m.getData());
		case 1: return TipoMovimentacao.CREDITO.getValor().equals(m.getTipoMovimentacao()) ? context.getString(R.string.tipo_movimentacao_credito) : context.getString(R.string.tipo_movimentacao_debito);
		case 2: return m.getTipoConta() != null && m.getTipoConta() > 0 ? ContaService.getDescricaoNomeConta(context, m.getTipoConta()) : "";
		case 3: return m.getDescricao();
		case 4: return PrecoUtils.formatoPadrao(m.getValor());
			
		default:
			return "";
		}
    }
	
	/**
	 * <p><b>Autoria: </b>Alysson Myller - 26/08/2013</p>
	 * @throws IOException 
	 * @throws WriteException 
	 * @throws ParseException 
	 */
	public static File geraPlanilhaMovimentacoes(Context context) throws IOException, WriteException, ParseException{
		String planilha = Environment.getExternalStorageDirectory().getAbsolutePath() + Constantes.SYSTEM_FOLDER;
		File filePlanilha = new File(planilha);
		if (!filePlanilha.exists()){
			filePlanilha.mkdir();
		}

		File filePlanilhaGerada = new File(filePlanilha, "libfinance.xls");
		WritableWorkbook workbook = Workbook.createWorkbook(filePlanilhaGerada);
    	WritableSheet sheet = workbook.createSheet("Movts", 0);
    	
    	formataTamanhoColunas(sheet);
    	
    	int linha = 0;
    	Map<String, List<Movimentacao>> mapDataXMovimentacoes = MovimentacaoService.getMapDataXMovimentacoes();
    	for (String key : mapDataXMovimentacoes.keySet()){
    		List<Movimentacao> movs = mapDataXMovimentacoes.get(key);
    		
    		// Preenche o cabeçalho do mês
    		linha = LibFinancePlanilha.preencheCabecalho(context, sheet, linha, key);
    		
    		// i = linha ; j = coluna
    		for (int i = 0; i < movs.size(); i++){
        		for (int j = 0; j < QUANTIDADE_COLUNAS; j ++){
        			Movimentacao mov = movs.get(i);
        			
        			String texto = LibFinancePlanilha.getDescricaoConteudo(context, j, mov);
        			WritableCellFormat formatoCelula = LibFinancePlanilha.getFormatadoPorColuna(j, mov.getTipoMovimentacao());
        			
        			Label label = new Label(j, linha, texto, formatoCelula); 
        			sheet.addCell(label);
        		}
        		
        		linha = linha + 1;
        	}	
    	}
    	
    	workbook.write(); 
    	workbook.close();

		return filePlanilhaGerada;
	}

	/**
	 * Coluna 0 = Campo data do movimento
	 * Coluna 1 = Campo tipo do movimento
	 * Coluna 2 = Campo descrição da conta de débito
	 * Coluna 3 = Campo descrição do movimento
	 * Coluna 4 = Campo valor do movimento
	 * 
	 * @param sheet
	 * <p><b>Autoria: </b>Alysson Myller - 27/08/2013</p>
	 */
	private static void formataTamanhoColunas(WritableSheet sheet) {
		sheet.setColumnView(0, 20); 
		sheet.setColumnView(1, 18);  
		sheet.setColumnView(2, 30);
		sheet.setColumnView(3, 60);
		sheet.setColumnView(4, 15);
	}

	/**
	 * Coluna 0 = Campo data do movimento
	 * Coluna 1 = Campo tipo do movimento
	 * Coluna 2 = Campo descrição da conta de débito
	 * Coluna 3 = Campo descrição do movimento
	 * Coluna 4 = Campo valor do movimento
	 * 
	 * @param coluna
	 * @param tipoMovimentacao
	 * @return
	 * @throws WriteException
	 * <p><b>Autoria: </b>Alysson Myller - 26/08/2013</p>
	 */
	private static WritableCellFormat getFormatadoPorColuna(int coluna, Integer tipoMovimentacao) throws WriteException {
		switch (coluna) {
		case 0: return getFormatoNumero();
		case 1: return getFormatoTexto();
		case 2: return getFormatoTexto();
		case 3: return getFormatoTexto();
		case 4: return tipoMovimentacao.equals(TipoMovimentacao.CREDITO.getValor()) ? getFormatoValorCredito() : getFormatoValorDebito();
		default: return getFormatoTexto();
		}
	}

	/**
	 *
	 * @throws Exception
     */
	public static void restaurarBaseDesdePlanilha(Context context) throws Exception {
		File sdCard = Environment.getExternalStorageDirectory();
		File filePlanilha = new File(sdCard.getAbsolutePath(), "/libfinance");
		File inputWorkbook = new File(filePlanilha, "libfinance.xls");

		if (!inputWorkbook.exists()){
			LibFinanceMessage.show(context, context.getResources().getString(R.string.nenhuam_planilha_encontrada_para_restaurar));
		} else {
			Workbook w = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet = w.getSheet(0);

			int linhas = sheet.getRows();
			if (linhas < 5){
				LibFinanceMessage.show(context, context.getResources().getString(R.string.planilha_sem_dados_suficientes_para_restaurar));
			} else {
				List<Movimentacao> movtos = new ArrayList<>();

				// Os dados começam na linha 5 (ou seja j = 4)
				for (int j = 4; j < sheet.getRows(); j++) {

					Movimentacao movimentacao = new Movimentacao();
					for (int i = 0; i < sheet.getColumns(); i++) {
						Cell cell = sheet.getCell(i, j);
						String data = cell.getContents();

						System.out.println("i = " + i + "  --  j = " + j);
						System.out.println("content = " + data);

						// Coluna da data
						if (i == 0) {

							// Caso seja a última linha do mês, pula 4 linhas e começa de novo
							if (data == null || data.isEmpty()){
								j = j + 4;
								break;
							}

							movimentacao.setData(DateUtils.convertFromTo(DatePatternUtils.DDMMYYYY, DatePatternUtils.YYYYMMDD, data));
						}

						// Coluna do tipo de movimentação
						if (i == 1){
							movimentacao.setTipoMovimentacao("Crédito".equals(data) ? TipoMovimentacao.CREDITO.getValor() : TipoMovimentacao.DEBITO.getValor());
						}

						// Coluna da conta
						if (i == 2){
							if (data != null && !data.isEmpty()){
								movimentacao.setTipoConta(TipoConta.getInstance(data).getValor());
							}
						}

						// Coluna da descrição
						if (i == 3){
							movimentacao.setDescricao(data);
						}

						// Coluna do valor
						if (i == 4){
							data = data.replace(".", "");
							data = data.replace(",", ".");
							movimentacao.setValor(new BigDecimal(data));
						}
					}

					movtos.add(movimentacao);
				}

				for (Movimentacao m : movtos){
					if (TipoMovimentacao.CREDITO.getValor().equals(m.getTipoMovimentacao())){
						CreditoService.criarCreditoDeMovimentacao(m);
					} else if (TipoMovimentacao.DEBITO.getValor().equals(m.getTipoMovimentacao())){
						DebitoService.criarDebitoDeMovimentacao(m);
					}
				}
			}
		}
	}

}

package br.com.next.liberdadefinanceirafree.service;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import br.com.next.liberdadefinanceirafree.dao.autosql.GenericRepository;
import br.com.next.liberdadefinanceirafree.dao.autosql.annotation.Table;
import br.com.next.liberdadefinanceirafree.dao.script.ScriptGenerator;
import br.com.next.liberdadefinanceirafree.enums.TipoCredito;
import br.com.next.liberdadefinanceirafree.model.Parametro;
import br.com.next.liberdadefinanceirafree.system.Constantes;
import br.com.next.liberdadefinanceirafree.util.DatePatternUtils;
import br.com.next.liberdadefinanceirafree.util.DateUtils;


/**
 * @author Alysson Myller
 * @since  10/06/2013
 */
public class SistemaService {
	
	/**
	 * @return True caso exista dados nas tabelas necessárias
	 * <p><b>Autoria: </b>Alysson Myller - 10/06/2013</p>
	 */
	public static boolean existeDadosJaCadastrados(){
		GenericRepository rep = GenericRepository.getInstanceFor(Parametro.class);
		Parametro param = (Parametro) rep.findObject(null);
		
		return param != null && param.getValorRendimentoMensal() != null && param.getValorRendimentoMensal().compareTo(BigDecimal.ZERO) > 0;
	}

	/**
	 * <p><b>Autoria: </b>Alysson Myller - 14/06/2013</p>
	 */
	public static void restaurarValores() {
		GenericRepository rep = GenericRepository.getInstanceFor(Void.class);
		for (Class<?> clazz : ScriptGenerator.mappedClasses()){
			rep.executeSQL(" DELETE FROM " + clazz.getAnnotation(Table.class).name());
		}
	}

	/**
	 * <p><b>Autoria: </b>Alysson Myller - 15/06/2013</p>
	 * @throws Exception 
	 */
	public static void verificaNovoMes(Context context) throws Exception {
		// Caso ainda não tenha feito o cálculo desse mês
		if (ehNovoMes()){
			
			// Calcula o valor que sobrou do mês passado
			BigDecimal valorRestanteTodasContas = ContaService.getValorRestanteContasMesPassado();
			CreditoService.criarCredito(context, valorRestanteTodasContas, TipoCredito.RESTANTE_MES_PASSADO);

			// Caso tenha rendimento mensal
			BigDecimal rendimentoMensal = ParametroService.buscarUltimoValorRendimentoMensal();
			if (rendimentoMensal != null && rendimentoMensal.compareTo(BigDecimal.ZERO) > 0){
				Date dataUltimaVerificacaoMes = ParametroService.getDataUltimaVerificacaoMes();
				Date hoje = new Date();
				int diffMeses = DateUtils.getDiffMeses(dataUltimaVerificacaoMes, hoje);

				Integer diaRecebimento = ParametroService.getDiaRecebimento();

				// Eu faço esse for pq se o usuário ficar sem entrar no sistema por um longo período, o próprio sistema
				// cadastra automático os créditos que meses que ele possa ter ficado sem utilizar
				Calendar dataUltimoMes = Calendar.getInstance();
				dataUltimoMes.setTime(dataUltimaVerificacaoMes);
				for (int i = 0; i < diffMeses; i ++){
					dataUltimoMes.add(Calendar.MONTH, 1);
					dataUltimoMes.set(Calendar.DAY_OF_MONTH, diaRecebimento);

					CreditoService.criarCredito(context, rendimentoMensal, TipoCredito.RENDIMENTO_MENSAL);
				}
			}

			// Atualiza a data de última verificação do mês
			String dataHoje = DateUtils.now(DatePatternUtils.YYYYMMDD);
			ParametroService.atualizaDataUltimaVerificacaoMes(dataHoje);
		}
	}

	/**
	 * @return
	 * @throws Exception
	 * <p><b>Autoria: </b>Alysson Myller - 26/07/2013</p>
	 */
	private static boolean ehNovoMes() throws Exception {
		Date dataUltimaVerificacaoMes = ParametroService.getDataUltimaVerificacaoMes();

		Calendar cal = Calendar.getInstance();
		cal.setTime(dataUltimaVerificacaoMes);
		int mesUltimaVerificacao = cal.get(Calendar.MONTH);
		
		Calendar hoje = Calendar.getInstance();
		int mesAtual = hoje.get(Calendar.MONTH);

		return mesAtual > mesUltimaVerificacao;
	}

	/**
	 * <p><b>Autoria: </b>Alysson Myller - 26/07/2013</p>
	 */
	public static void criaPastasNecessarias() {
		String systemFolder = Environment.getExternalStorageDirectory() + Constantes.SYSTEM_FOLDER;
		File system = new File(systemFolder);
		if (!system.exists()){
			system.mkdir();
		}

		String backupFolder = Environment.getExternalStorageDirectory() + Constantes.BACKUP_FOLDER;
		File backup = new File(backupFolder);
		if (!backup.exists()){
			backup.mkdir();
		}
	}

	/**
	 * @param valor
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 16/07/2013</p>
	 */
	public static boolean validaDadosInformados(BigDecimal valor) {
		return valor != null && valor.compareTo(BigDecimal.ZERO) > 0;
	}

}

package br.com.next.liberdadefinanceirafree.util;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import br.com.next.liberdadefinanceirafree.dao.DataBaseAccess;
import br.com.next.liberdadefinanceirafree.system.Constantes;


/**
 * @author Alysson Myller
 * @since  08/2012
 */
public class DatabaseUtils {
	
	private static final String SYSTEM_DB_FOLDER = "/data/br.com.next.liberdadefinanceirafree/databases";
	private static final String SYSTEM_DB_PATH = SYSTEM_DB_FOLDER + File.separator + Constantes.DATABASE_NAME;
	
	/**
	 * @return
	 * @throws IOException
	 * <p><b>Autoria: </b>Alysson Myller / Thiago - 08/2012</p>
	 */
	public static boolean backupDataBase(Context context) throws IOException  {
		File sd = Environment.getExternalStorageDirectory();
		File data = Environment.getDataDirectory();

		if (sd.canWrite()) {

			String backupDBPath = Constantes.BACKUP_FOLDER + "/" + Constantes.DATABASE_NAME;
			File currentDB = new File(data, SYSTEM_DB_PATH);
			File backupDB = new File(sd, backupDBPath);

			if (currentDB.exists()) {

				FileChannel src = new FileInputStream(currentDB).getChannel();
				FileChannel dst = new FileOutputStream(backupDB).getChannel();
				dst.transferFrom(src, 0, src.size());

				src.close();
				dst.close();

				Toast.makeText(context, "Backup feito com sucesso!", Toast.LENGTH_SHORT).show();

				return true;
			}
		}
		return false;
	}

	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller / Thiago - 08/2012</p>
	 */
	public static void deleteDataBase() {
		validaExisteDatabase();
		
		File data = Environment.getDataDirectory();
		File currentDB = new File(data, SYSTEM_DB_PATH);
		currentDB.delete();
	}
	
	/**
	 * @throws Exception
	 * <p><b>Autoria: </b>Alysson Myller / Thiago - 08/2012</p>
	 * @throws IOException 
	 */
	public static void importDataBase(Context context) throws Exception, IOException {
		deleteDataBase();
		
        File currentDB = new File(Environment.getDataDirectory(), SYSTEM_DB_FOLDER);
        if(currentDB.canWrite()) {
            File backupDB = new File(Environment.getExternalStorageDirectory(), Constantes.BACKUP_FOLDER + File.separator + Constantes.DATABASE_NAME);
            
            if(backupDB.exists()) {
                FileChannel src = new FileInputStream(backupDB).getChannel();
                FileChannel dst = new FileOutputStream(currentDB + File.separator + Constantes.DATABASE_NAME).getChannel();
                dst.transferFrom(src, 0, src.size());
                
                src.close();
                dst.close();
                
                DataBaseAccess.restartConnection(context);
                Toast.makeText(context, "Base de dados importada", Toast.LENGTH_SHORT).show();
            } else {
            	throw new Exception("Não existe um backup de database {0} para ser importado");
            }
        } else {
        	throw new Exception("Não é possível escrever na pasta: " + currentDB.getAbsolutePath() + " \nPossível erro: A pasta não existe");
        }
	}
	
	/**
	 * @return
	 * <p><b>Autoria: </b>Alysson Myller - 08/2012</p>
	 */
	public static void validaExisteDatabase() {
		File file = new File(Environment.getDataDirectory(), SYSTEM_DB_PATH);
		if (!file.exists()){
			throw new RuntimeException("Não existe database: {0}"); 
		}
	}



}

package br.edu.ufcg.lsi.deuzicachico.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;
import br.edu.ufcg.lsi.deuzicachico.models.Foco;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ControladorFoco {
	private Gson gson;
	private static final String STORAGE = Environment
			.getExternalStorageDirectory().getPath();
	
	public ControladorFoco(){
		gson = new GsonBuilder().registerTypeAdapter(String.class,
				new StringVaziaExclusaoJson()).create();
	}
	
	public void salvarJSON(Foco foco) throws IOException {
		
		String jsText = gson.toJson(foco);
		String path = STORAGE + "/DeuZicaChico/focos/";
		createDirectoryOnSdCard(path);
		
		File file = new File(path, "Foco-" + foco.getId().toString()
				+ ".json");
		FileOutputStream out = new FileOutputStream(file, true);
		out.write(jsText.getBytes());
		out.flush();
		out.close();
	}
	
	
	public void deletarJSON(Foco foco) throws FileNotFoundException {
		String path = STORAGE + "/DeuZicaChico/focos/";

		File file = new File(path, "Foco-" + foco.getId().toString()
				+ ".json");
		if (file.exists()) {
			file.delete();
		} else {
			throw new FileNotFoundException();
		}
	}
	
	public static void createDirectoryOnSdCard(String path) {
		File newFolder = new File(path);

		boolean exist = newFolder.exists();

		if (!exist) {
			newFolder.mkdir();
		}
	}
	
}

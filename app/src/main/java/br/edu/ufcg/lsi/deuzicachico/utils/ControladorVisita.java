package br.edu.ufcg.lsi.deuzicachico.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;
import br.edu.ufcg.lsi.deuzicachico.models.Foco;
import br.edu.ufcg.lsi.deuzicachico.models.Visita;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ControladorVisita {
	private Gson gson;
	private static final String STORAGE = Environment
			.getExternalStorageDirectory().getPath();
	
	public ControladorVisita(){
		gson = new GsonBuilder().registerTypeAdapter(String.class,
				new StringVaziaExclusaoJson()).create();
	}
	
	public void salvarJSON(Visita foco) throws IOException {
		
		String jsText = gson.toJson(foco);
		String path = STORAGE + "/DeuZicaChico/visita/";
		createDirectoryOnSdCard(path);
		
		File file = new File(path, "Visita-" + foco.getId().toString()
				+ ".json");
		FileOutputStream out = new FileOutputStream(file, true);
		out.write(jsText.getBytes());
		out.flush();
		out.close();
	}
	
	
	public void deletarJSON(Visita foco) throws FileNotFoundException {
		String path = STORAGE + "/DeuZicaChico/visita/";

		File file = new File(path, "Visita-" + foco.getId().toString()
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

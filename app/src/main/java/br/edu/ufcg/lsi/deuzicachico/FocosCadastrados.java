package br.edu.ufcg.lsi.deuzicachico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;

import br.edu.ufcg.lsi.deuzicachico.adapters.FocoAdapter;
import br.edu.ufcg.lsi.deuzicachico.models.Foco;
import br.edu.ufcg.lsi.deuzicachico.utils.StringVaziaExclusaoJson;
import br.edu.ufcg.lsi.deuzicachico.R;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class FocosCadastrados extends Activity {
	private Gson gson;
	private static final String STORAGE = Environment
			.getExternalStorageDirectory().getPath();
	private List<Foco> listaFocos = new ArrayList<Foco>(); 
	private GridView grid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_focos_cadastrados);
		
		gson = new GsonBuilder().registerTypeAdapter(String.class,
				new StringVaziaExclusaoJson()).create();
		
		grid = (GridView) findViewById(R.id.grid_view_fotos);
		
		carregarFocos();
		if(listaFocos.size() == 0){
			TextView nao_cadastrado = (TextView) findViewById(R.id.texto_foco_cadastrado);
			nao_cadastrado.setText("Nenhum foco cadastrado por você");
		}
		grid.setAdapter(new FocoAdapter(getApplicationContext(),
				listaFocos));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.focos_cadastrados, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void carregarFocos() {
		String path = STORAGE + "/DeuZicaChico/focos/";
		File[] files1 = ListFileInDirectory(path);

		if (files1 != null && files1.length > 0) {
			for (int i = 0; i < files1.length; i++) {
				String textoJS = getTextFromFile(files1[i]);
				JsonReader reader = new JsonReader(new StringReader(textoJS));
				reader.setLenient(true);
				Foco foco = gson.fromJson(reader, Foco.class);
				listaFocos.add(foco);
			}
		}

	}
	
	
	public File[] ListFileInDirectory(String path) {
		File f = new File(path);
		return f.listFiles();
	}
	
	public String getTextFromFile(File file) {
		BufferedReader in;
		String text = "";
		String textResult = "";

		try {
			in = new BufferedReader(new FileReader(file.getAbsolutePath()));
			while (in.ready()) {
				text = in.readLine();
				textResult = textResult + text;
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return textResult;
	}
}

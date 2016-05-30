package br.edu.ufcg.lsi.deuzicachico;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import br.edu.ufcg.lsi.deuzicachico.R;

public class MainActivity extends Activity {

	ImageButton botao_denuncia, botao_vistoria, botao_focos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		botao_denuncia = (ImageButton) findViewById(R.id.botao_denuncia_foco);
		botao_vistoria = (ImageButton) findViewById(R.id.botao_visitas);
		botao_focos = (ImageButton) findViewById(R.id.botao_focos);
		
		
		botao_denuncia.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						CadastroFoco.class);
				startActivity(intent);
			}
		});
		botao_focos.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						FocosCadastrados.class);
				startActivity(intent);
			}
		});

		botao_vistoria.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						GerenciadorVisitas.class);
				startActivity(intent);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
}

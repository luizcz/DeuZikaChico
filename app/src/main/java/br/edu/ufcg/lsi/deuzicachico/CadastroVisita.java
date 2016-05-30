package br.edu.ufcg.lsi.deuzicachico;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.edu.ufcg.lsi.deuzicachico.models.Foco;
import br.edu.ufcg.lsi.deuzicachico.models.Visita;
import br.edu.ufcg.lsi.deuzicachico.utils.ControladorFoco;
import br.edu.ufcg.lsi.deuzicachico.utils.ControladorVisita;
import br.edu.ufcg.lsi.deuzicachico.utils.FallbackLocationTracker;
import br.edu.ufcg.lsi.deuzicachico.utils.HttpConnection;
import br.edu.ufcg.lsi.deuzicachico.utils.Loading;
import br.edu.ufcg.lsi.deuzicachico.utils.ProviderLocationTracker;
import br.edu.ufcg.lsi.deuzicachico.utils.ProviderLocationTracker.ProviderType;
import br.edu.ufcg.lsi.deuzicachico.R;

public class CadastroVisita extends Activity {
	
	
	private EditText et_agente, et_data, et_obs;
	private Button bt_salvar;
	private Location location;
	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastro_visita);
		
		activity = this;
		
		et_agente = (EditText) findViewById(R.id.et_nome_agente);
		et_data = (EditText) findViewById(R.id.et_data);
		et_obs = (EditText) findViewById(R.id.et_obs_visita);
		bt_salvar = (Button) findViewById(R.id.botao_salvar_visita);
		
		bt_salvar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (location != null) {
					Visita visita = new Visita(et_obs.getText().toString(), et_agente.getText().toString(), location.getLatitude(), location.getLongitude(), et_data.getText().toString());
					AsyncTaskEnviarFormularioVisita taskEnviarFormularioVisita = new AsyncTaskEnviarFormularioVisita(visita, activity);
					taskEnviarFormularioVisita.execute();
					ControladorVisita controlador = new ControladorVisita();
					try {
						controlador.salvarJSON(visita);
					} catch (IOException e) {
						e.printStackTrace();
					}

				}else{
					showToast("Não foi possível capturar sua localização");
					location = capturaLocalizacao();
				}

			}
			public void showToast(String text) {
				Toast.makeText(activity.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
			}
		});
		
		location = capturaLocalizacao();
		if(location == null){			
			CadastroFoco.showPleaseEnableGps("GPS", this);
		}
	}
	public Location capturaLocalizacao(){
		Location local = null;
		ProviderLocationTracker provider = new ProviderLocationTracker(getApplicationContext(), ProviderType.GPS);
		FallbackLocationTracker tracker = new FallbackLocationTracker(getApplicationContext(), ProviderType.NETWORK);
		if(provider.hasLocation()){
			 local =  provider.getLocation();
		}else if(provider.hasPossiblyStaleLocation()){
			local = provider.getPossiblyStaleLocation();
		}else if(tracker.hasLocation()){
			local = tracker.getLocation();
		}else if(tracker.hasPossiblyStaleLocation()){
			local = tracker.getPossiblyStaleLocation(); 
		}
		
		return local;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cadastro_visita, menu);
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

class AsyncTaskEnviarFormularioVisita extends AsyncTask<String, String, String> {

	private Loading loadingView;
	private Context context;
	private Visita visita;
	private Activity activity;


	public AsyncTaskEnviarFormularioVisita(Visita visita, Activity activity) {
		this.visita = visita;
		this.context = activity.getApplicationContext();
		this.loadingView = new Loading(activity);
		this.activity = activity;

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		loadingView.showInflate("Enviando...");
	}

	@Override
	protected String doInBackground(String... arg0) {
		String answer = HttpConnection.getSetDataWebVisita(visita);
		return answer;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		if (result.equals("1")) {
			showToast("Nova visita cadastrada");
		} else {
			showToast("Erro no servidor");
		}

		loadingView.stopInflate();
		activity.onBackPressed();
	}
	
	public void showToast(String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}

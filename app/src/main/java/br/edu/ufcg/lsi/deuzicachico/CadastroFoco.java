package br.edu.ufcg.lsi.deuzicachico;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import br.edu.ufcg.lsi.deuzicachico.models.Foco;
import br.edu.ufcg.lsi.deuzicachico.models.Foto;
import br.edu.ufcg.lsi.deuzicachico.utils.ControladorFoco;
import br.edu.ufcg.lsi.deuzicachico.utils.FallbackLocationTracker;
import br.edu.ufcg.lsi.deuzicachico.utils.HttpConnection;
import br.edu.ufcg.lsi.deuzicachico.utils.Loading;
import br.edu.ufcg.lsi.deuzicachico.utils.ProviderLocationTracker;
import br.edu.ufcg.lsi.deuzicachico.utils.ProviderLocationTracker.ProviderType;
import br.edu.ufcg.lsi.deuzicachico.R;

import com.google.gson.Gson;

public class CadastroFoco extends Activity {

	private String TAG = "CadastroFocoActivity";
	private Uri mImageUri;
	static final int REQUEST_TAKE_PHOTO = 1;
	private ImageView view;
	private EditText et_obs;
	private Button salvar;
	private Bitmap bitmap;
	private File imageFile = null;
	private OutputStream fOut = null;
	private Location location;
	private String answer;
	private Foto foto;
	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastro_foco);

		view = (ImageView) findViewById(R.id.foto_denuncia);
		et_obs = (EditText) findViewById(R.id.et_obs);
		salvar = (Button) findViewById(R.id.botao_salvar);
		
		activity = this;

		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				tirarFoto();
			}
		});

		salvar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (location != null || foto == null) {
					final Foco foco = new Foco(foto, location.getLatitude(), location.getLongitude(), et_obs.getText().toString());
					final String json = new Gson().toJson(foco);
					AsyncTaskEnviarFormulario taskEnviarFormulario = new AsyncTaskEnviarFormulario(foco, activity);
					taskEnviarFormulario.execute();
					ControladorFoco controlador = new ControladorFoco();
					try {
						controlador.salvarJSON(foco);
					} catch (IOException e) {
						e.printStackTrace();
					}

				}else{
                    if(location == null){
                        showToast("Não foi possível capturar sua localização");
                        location = capturaLocalizacao();
                    }else{
                        showToast("Não é possivel enviar sem foto");
                        tirarFoto();
                    }
                }

			}
			public void showToast(String text) {
				Toast.makeText(activity.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
			}
		});
		
		
		
		location = capturaLocalizacao();
		
		
		if(location == null){			
			showPleaseEnableGps("GPS", this);
		}
		
		//showToast("Lat " + location.getLatitude() + "Long "+ location.getLongitude());
		
		tirarFoto();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cadastro_foco, menu);
		return true;
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

	public void tirarFoto() {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		File photo = null;
		try {
			// place where to store camera taken picture
			photo = createTemporaryFile("picture", ".jpg");
			photo.delete();
		} catch (Exception e) {
			Log.v(TAG, "Can't create file to take picture!");
		}
		mImageUri = Uri.fromFile(photo);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
		// start camera intent
		startActivityForResult(intent, REQUEST_TAKE_PHOTO);
	}

	private File createTemporaryFile(String part, String ext) throws Exception {
		File tempDir = Environment.getExternalStorageDirectory();
		tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
		if (!tempDir.exists()) {
			tempDir.mkdir();
		}
		return File.createTempFile(part, ext, tempDir);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
			this.grabImage(view);

		}
		location = capturaLocalizacao();

		super.onActivityResult(requestCode, resultCode, intent);
	}

	public void grabImage(ImageView imageView) {
		this.getContentResolver().notifyChange(mImageUri, null);
		ContentResolver cr = this.getContentResolver();
		if (bitmap != null) {
			bitmap = null;
		}
		try {
			bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr,
					mImageUri);
			Bitmap scaledBitmap = scaleDown(bitmap, 1000, true);
			imageView.setImageBitmap(scaledBitmap);
			Bitmap out = Bitmap.createScaledBitmap(bitmap, 800, 600, false);
			//imageView.setBackground(null);
			// imageView.setImageBitmap(bitmap);
			saveFoto(out);
			// imageView.setImageBitmap(bitmap);
			if (bitmap != null) {
				bitmap = null;
			}
			clearApplicationData();

			showToast("Foto capturada com sucesso!");

		} catch (Exception e) {
			Log.i("exception", e.getMessage());
			showToast("Erro ao capturar foto!!!");

		}
	}

	private void showToast(String text) {
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)
				.show();
	}

	private boolean saveFoto(Bitmap image) {
		boolean error = false;
		Boolean retorno = false;
		String path = Environment.getExternalStorageDirectory().toString();
		String idPath = "DeuZicaChico/";

		Calendar cal = Calendar.getInstance();
		int mYear = cal.get(Calendar.YEAR);
		int mMonth = cal.get(Calendar.MONTH);
		int mDay = cal.get(Calendar.DAY_OF_MONTH);
		int second = cal.get(Calendar.SECOND);
		int minute = cal.get(Calendar.MINUTE);
		int hourofday = cal.get(Calendar.HOUR_OF_DAY);
		String imageName = "foto_" + mDay + "_" + mMonth + "_" + mYear + "_"
				+ hourofday + "_" + minute + "_" + second + ".jpg";
		String imagePath = idPath + "/" + imageName;

		if (!error) {
			createDirectoryOnSdCard(idPath);
			imageFile = new File(path, imagePath);
			try {
				fOut = new FileOutputStream(imageFile);
			} catch (FileNotFoundException e) {

				Log.i(TAG, "Erro ao criar fOut - FileOutputStrean");
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			image.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object   
			byte[] b = baos.toByteArray(); 
			String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
			foto = new Foto(imageFile.getAbsolutePath(), imageName, encodedImage);
			image.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
			image = null;
			bitmap = null;

			try {

				fOut.close();

			} catch (IOException e) {
				Log.i(TAG, "Erro ao executar o flush fOut");
			}
			try {
				fOut.close();
			} catch (IOException e) {

				Log.i(TAG, "Erro ao dar close no fOut");
			}

			try {
				MediaStore.Images.Media.insertImage(getContentResolver(),
						imageFile.getAbsolutePath(), imageFile.getName(),
						imageFile.getName());
			} catch (FileNotFoundException e1) {
				Log.i(TAG, "Erro ao salvar foto");
				e1.printStackTrace();
				error = true;
			}

			retorno = true;

		}

		if (!error) {
		}

		else {
			view.setImageDrawable(null);

		}
		return retorno;
	}

	public static void createDirectoryOnSdCard(String nameFolder) {
		File externalStorage = Environment.getExternalStorageDirectory();
		String externalPath = externalStorage.getPath();
		File newFolder = new File(externalPath + "/" + nameFolder);

		boolean exist = newFolder.exists();

		if (!exist) {
			newFolder.mkdir();
		}
	}

	public void clearApplicationData() {
		File cache = getCacheDir();
		File appDir = new File(cache.getParent());
		if (appDir.exists()) {
			String[] children = appDir.list();
			for (String s : children) {
				if (!s.equals("lib")) {
					deleteDir(new File(appDir, s));
					Log.i("TAG",
							"**************** File /data/data/APP_PACKAGE/" + s
									+ " DELETED *******************");
				}
			}
		}
	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
			boolean filter) {
		float ratio = Math.min((float) maxImageSize / realImage.getWidth(),
				(float) maxImageSize / realImage.getHeight());
		int width = Math.round((float) ratio * realImage.getWidth());
		int height = Math.round((float) ratio * realImage.getHeight());

		Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width, height,
				filter);
		return newBitmap;
	}

	public static void showPleaseEnableGps(String provider,
			final Activity activity) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

		alertDialog.setTitle(provider + " SETTINGS");

		alertDialog
				.setMessage(provider
						+ " provavelmente está desativado! Por favor, ative o GPS para futura captura de coordenadas geográficas!");

		alertDialog.setPositiveButton("Configurações",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						activity.startActivity(intent);
					}
				});

		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		alertDialog.show();
	}
}

class AsyncTaskEnviarFormulario extends AsyncTask<String, String, String> {

	private Loading loadingView;
	private Context context;
	private Foco foco;
	private Activity activity;


	public AsyncTaskEnviarFormulario(Foco foco, Activity activity) {
		this.foco = foco;
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
		String answer = HttpConnection.getSetDataWeb(foco);
		return answer;

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		if (result.equals("1")) {
			showToast("Novo foco cadastrada");
		}else {
			showToast("Erro no servidor");
		}

		loadingView.stopInflate();
		activity.onBackPressed();
	}
	
	public void showToast(String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}

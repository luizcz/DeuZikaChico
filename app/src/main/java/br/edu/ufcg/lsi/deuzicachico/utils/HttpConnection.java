package br.edu.ufcg.lsi.deuzicachico.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import br.edu.ufcg.lsi.deuzicachico.models.Foco;
import br.edu.ufcg.lsi.deuzicachico.models.Visita;

public class HttpConnection {
	public static String getSetDataWeb(Foco wd){
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(Config.FILE_UPLOAD_URL);
		String answer = "";
		
		try{
			ArrayList<NameValuePair> valores = new ArrayList<NameValuePair>();
			valores.add(new BasicNameValuePair("data", wd.getDataDenuncia().toString()));
			valores.add(new BasicNameValuePair("lat", wd.getLatitude().toString()));
			valores.add(new BasicNameValuePair("long", wd.getLongitude().toString()));
			valores.add(new BasicNameValuePair("desc", wd.getObservacao()));
			valores.add(new BasicNameValuePair("problema", "TB_PROBLEMA"));
			valores.add(new BasicNameValuePair("img-mime", wd.getFoto().getMime()));
			valores.add(new BasicNameValuePair("img-image", wd.getFoto().getBase64()));
						
			httpPost.setEntity(new UrlEncodedFormEntity(valores));
			HttpResponse resposta = httpClient.execute(httpPost);
			answer = EntityUtils.toString(resposta.getEntity());
		}
		catch (UnsupportedEncodingException e) { e.printStackTrace(); }
		catch (ClientProtocolException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); }
		return(answer);
	}
	
	public static String getSetDataWebVisita(Visita wd){
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(Config.FILE_VISITA_UPLOAD_URL);
		String answer = "";
		
		try{
			ArrayList<NameValuePair> valores = new ArrayList<NameValuePair>();
			valores.add(new BasicNameValuePair("data", wd.getData().toString()));
			valores.add(new BasicNameValuePair("lat", wd.getLatitude().toString()));
			valores.add(new BasicNameValuePair("long", wd.getLongitude().toString()));
			valores.add(new BasicNameValuePair("desc", wd.getObservacao()));
			valores.add(new BasicNameValuePair("nome", wd.getNomeAgente()));
			valores.add(new BasicNameValuePair("problema", "TB_VISITA"));

						
			httpPost.setEntity(new UrlEncodedFormEntity(valores));
			HttpResponse resposta = httpClient.execute(httpPost);
			answer = EntityUtils.toString(resposta.getEntity());
		}
		catch (UnsupportedEncodingException e) { e.printStackTrace(); }
		catch (ClientProtocolException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); }
		return(answer);
	}
}

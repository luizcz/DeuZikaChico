package br.edu.ufcg.lsi.deuzicachico.network;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class Connection {

	private static DefaultHttpClient connection = null;

	public synchronized static DefaultHttpClient getInstance() {
		if (connection == null) {
			connection = new DefaultHttpClient();
		}
		return connection;
	}

	public static String[] requestPOST(String url, String jsonText)
			throws ClientProtocolException, IOException {

		getInstance().getParams().setIntParameter("http.socket.timeout", 30000);
		getInstance().getParams().setIntParameter("http.connection.timeout",
				40000);
		HttpPost httppost = new HttpPost(url);
		StringEntity stringEntity;
		String[] result = new String[2];
		String statusCode = null;

		stringEntity = new StringEntity(jsonText, HTTP.UTF_8);
		httppost.setHeader("Content-type", "application/json; charset=utf-8");
		httppost.setEntity(stringEntity);
		HttpResponse response = getInstance().execute(httppost);
		HttpEntity httpEntity = response.getEntity();

		if (httpEntity != null) {
			InputStream inputStream = httpEntity.getContent();
			result[0] = convertStreamToString(inputStream);

		}

		statusCode = String.valueOf(response.getStatusLine().getStatusCode());
		result[1] = statusCode;
		Log.i("STATUS CODE RESQUEST", statusCode);

		return result;
	}

	private static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	private static Scanner scanner(java.io.InputStream is) {
		return new java.util.Scanner(is, "UTF-8");
	}

}

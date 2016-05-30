package br.edu.ufcg.lsi.deuzicachico.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import br.edu.ufcg.lsi.deuzicachico.R;

public class Loading {

	private AlertDialog alertDialog;
	private Activity activity;

	public Loading(Activity activity) {
		this.activity = activity;
	}

	@SuppressLint("InflateParams")
	public void showInflate(String message) {
		LayoutInflater factory = LayoutInflater.from(activity);
		final View viewInflate = factory.inflate(R.layout.activity_load, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setView(viewInflate);

		TextView tvMessage = (TextView) viewInflate.findViewById(R.id.text_view_message_load);
		tvMessage.setText(message);

		alertDialog = builder.create();
		alertDialog.show();
	}

	public void stopInflate() {
		alertDialog.cancel();
	}
}
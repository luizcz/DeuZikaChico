package br.edu.ufcg.lsi.deuzicachico.adapters;

import java.io.ByteArrayInputStream;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.edu.ufcg.lsi.deuzicachico.models.Visita;
import br.edu.ufcg.lsi.deuzicachico.R;


public class VisitaAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<Visita> item;

	static ByteArrayInputStream is = null;

	public VisitaAdapter(Context context, List<Visita> listaDeFocos) {
		this.item = listaDeFocos;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return item.size();
	}

	@Override
	public Visita getItem(int position) {
		return item.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Suport iconHolder;

		if (view == null) {
			view = mInflater.inflate(R.layout.item_list_visita, null);
			iconHolder = new Suport();

			iconHolder.agente = (TextView) view.findViewById(R.id.textView_nome_agente);
			iconHolder.data = (TextView) view.findViewById(R.id.textView_data_visita);
			iconHolder.obs = (TextView) view.findViewById(R.id.textView_observacoes);
			

			view.setTag(iconHolder);

		}
		
		iconHolder = (Suport) view.getTag();

		
		iconHolder.agente.setText(item.get(position).getNomeAgente().toString());
		iconHolder.data.setText(item.get(position).getData().toString());
		iconHolder.obs.setText(item.get(position).getObservacao().toString());

		return view;
	}

	/** * Classe de suporte para os itens do layout. */
	private class Suport {

		TextView agente, data, obs;
	}



}

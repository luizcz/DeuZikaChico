package br.edu.ufcg.lsi.deuzicachico.adapters;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.edu.ufcg.lsi.deuzicachico.models.Foco;
import br.edu.ufcg.lsi.deuzicachico.R;


public class FocoAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<Foco> item;

	static ByteArrayInputStream is = null;

	public FocoAdapter(Context context, List<Foco> listaDeFocos) {
		this.item = listaDeFocos;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return item.size();
	}

	@Override
	public Foco getItem(int position) {
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
			view = mInflater.inflate(R.layout.item_foto_grid, null);
			iconHolder = new Suport();
			iconHolder.foto = (ImageView) view
					.findViewById(R.id.image_view_adapter_foto);
			iconHolder.texto = (TextView) view.findViewById(R.id.texto_descricao);
			

			view.setTag(iconHolder);

		}
		
		iconHolder = (Suport) view.getTag();
		File f = new File(item.get(position).getFoto().getFoto());

		if (f != null) {
			iconHolder.foto.setImageBitmap(decodeFile(f));
		}
		
		iconHolder.texto.setText(item.get(position).getObservacao().toString());

		return view;
	}

	/** * Classe de suporte para os itens do layout. */
	private class Suport {

		ImageView foto;
		TextView texto;
	}

	int IMAGE_MAX_SIZE = 256;

	private Bitmap decodeFile(File f) {
		Bitmap b = null;

		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;

		FileInputStream fis = null;
		try {
			
			
			
			
			fis = new FileInputStream(f);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BitmapFactory.decodeStream(fis, null, o);
		try {
			if (fis != null) {
				fis.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int scale = 1;
		if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
			scale = (int) Math.pow(
					2,
					(int) Math.ceil(Math.log(IMAGE_MAX_SIZE
							/ (double) Math.max(o.outHeight, o.outWidth))
							/ Math.log(0.5)));
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		try {
			fis = new FileInputStream(f);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		b = BitmapFactory.decodeStream(fis, null, o2);
		try {
			if(fis != null){
				fis.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return b;
	}

}


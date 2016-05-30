package br.edu.ufcg.lsi.deuzicachico.utils;


import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class StringVaziaExclusaoJson extends TypeAdapter<String> {

	@Override
	public String read(JsonReader arg0) throws IOException {
		String value = arg0.nextString();
		return value;
	}

	@Override
	public void write(JsonWriter arg0, String arg1) throws IOException {
		if(arg1 != null && arg1.equals("")){
			arg0.nullValue();
			return;
		}
		arg0.value(arg1);
	}


}

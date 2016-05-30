package br.edu.ufcg.lsi.deuzicachico.models;


public class Foto {
	private String Caminhofoto;
	private String nome;
	private String mime;
	private String base64;
	
	
	public Foto(String foto, String nome, String base64){
		this.Caminhofoto = foto;
		this.nome = nome;
		this.mime = "jpg";
		this.base64 = base64;
		
	}

	public String getFoto() {
		return Caminhofoto;
	}

	public void setFoto(String foto) {
		this.Caminhofoto = foto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	

	
	public String getMime() {
		return mime;
	}
	public void setMime(String mime) {
		this.mime = mime;
	}
	public void setMimeFromImgPath(String imgPath) {
		String[] aux = imgPath.split("\\.");
		this.mime = aux[aux.length - 1];
	}

	public String getCaminhofoto() {
		return Caminhofoto;
	}

	public void setCaminhofoto(String caminhofoto) {
		Caminhofoto = caminhofoto;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}
	

	
	
}



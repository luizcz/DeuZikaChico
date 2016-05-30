package br.edu.ufcg.lsi.deuzicachico.models;

import java.util.Date;

public class Foco {
	
	private Long id;
	private Foto foto;
	private Double latitude;
	private Double longitude;
	private String observacao;
	private Date dataDenuncia;
	private boolean foiEnviado;
	
	public Foco(Foto foto, Double latitude, Double longitude, String observacao){
		this.foto = foto;
		this.latitude = latitude;
		this.longitude = longitude;
		this.observacao = observacao;
		Date calendar = new Date();
		id = calendar.getTime();
		dataDenuncia = new Date();
		foiEnviado = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Foto getFoto() {
		return foto;
	}

	public void setFoto(Foto foto) {
		this.foto = foto;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Date getDataDenuncia() {
		return dataDenuncia;
	}

	public void setDataDenuncia(Date dataDenuncia) {
		this.dataDenuncia = dataDenuncia;
	}

	public boolean isFoiEnviado() {
		return foiEnviado;
	}

	public void setFoiEnviado(boolean foiEnviado) {
		this.foiEnviado = foiEnviado;
	}
	
}

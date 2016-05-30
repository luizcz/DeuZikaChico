package br.edu.ufcg.lsi.deuzicachico.models;

import java.util.Date;

public class Visita {
	private Long id;
	private String observacao;
	private String nomeAgente;
	private Double latitude;
	private Double longitude;
	private String data;
	
	public Visita(String observacao, String nomeAgente, Double latitude, Double longitude, String data){
		this.observacao = observacao;
		this.nomeAgente = nomeAgente;
		this.latitude = latitude;
		this.longitude = longitude;
		Date time = new Date();
		this.id = time.getTime();
		this.data = data;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getNomeAgente() {
		return nomeAgente;
	}

	public void setNomeAgente(String nomeAgente) {
		this.nomeAgente = nomeAgente;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	

}

package com.nkyrim.thessapp.domain;

import java.io.Serializable;
import java.util.Date;

public class ObjectivePoi implements Serializable {
	private int id;
	private String completedOn;
	private Poi poi;

	public ObjectivePoi(int id, String completedOn, Poi poi) {
		this.id = id;
		this.completedOn = completedOn;
		this.poi = poi;
	}

	public int getId() {
		return id;
	}

	public String getCompletedOn() {
		return completedOn;
	}

	public Poi getPoi() {
		return poi;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCompletedOn(String completedOn) {
		this.completedOn = completedOn;
	}

	public void setPoi(Poi poi) {
		this.poi = poi;
	}
}


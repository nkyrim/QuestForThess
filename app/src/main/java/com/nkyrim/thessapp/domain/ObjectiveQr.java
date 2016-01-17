package com.nkyrim.thessapp.domain;

import java.io.Serializable;

public class ObjectiveQr implements Serializable {
	private int id;
	private String completedOn;
	private QrCode qr;

	public ObjectiveQr(QrCode qr) {
		this.qr = qr;
	}

	public ObjectiveQr(int id, String completedOn, QrCode qr) {
		this.id = id;
		this.completedOn = completedOn;
		this.qr = qr;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCompletedOn() {
		return completedOn;
	}

	public void setCompletedOn(String completedOn) {
		this.completedOn = completedOn;
	}

	public QrCode getQr() {
		return qr;
	}

	public void setQr(QrCode qr) {
		this.qr = qr;
	}
}

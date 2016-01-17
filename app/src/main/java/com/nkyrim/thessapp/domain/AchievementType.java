package com.nkyrim.thessapp.domain;

import java.io.Serializable;

public class AchievementType implements Serializable {
	private int id;
	private String title;
	private String description;
	private int reqobjectives;

	public AchievementType(int id, String title, String description, int reqobjectives) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.reqobjectives = reqobjectives;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public int getReqobjectives() {
		return reqobjectives;
	}
}

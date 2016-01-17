package com.nkyrim.thessapp.domain;

public class Achievement {
	private int id;
	private String completedOn;
	private AchievementType type;

	public Achievement(String completedOn, AchievementType type) {
		this.completedOn = completedOn;
		this.type = type;
	}

	public Achievement(int id, String completedOn, AchievementType type) {
		this.id = id;
		this.completedOn = completedOn;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public String getCompletedOn() {
		return completedOn;
	}

	public AchievementType getType() {
		return type;
	}
}

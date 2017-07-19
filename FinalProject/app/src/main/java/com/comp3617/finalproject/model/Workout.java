package com.comp3617.finalproject.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by andrewchen on 2017-07-13.
 */

public class Workout extends RealmObject {
	private String fileName;
	private Date date;
	private double distance;
	private long duration;
	private String name;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setWorkout(String fileName, String name, Date date, double distance, long duration) {
		setFileName(fileName);
		setName(name);
		setDate(date);
		setDistance(distance);
		setDuration(duration);
	}

	@Override
	public String toString() {
		return "Workout{" + "fileName='" + fileName + '\'' + ", date=" + date + ", distance=" + distance + ", duration="
				+ duration + ", name='" + name + '\'' + '}';
	}

}

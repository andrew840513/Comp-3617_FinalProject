package com.comp3617.finalproject.com.comp3617.finalproject.gpx;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Andrew on 2017-07-12.
 */

@Root(name = "wpt")
public class WPT {
	@Attribute(name = "lat")
	private double latitude;

	@Attribute(name = "lon")
	private double longitude;

	@Element(name = "ele")
	private double elevation;

	@Element(name = "time")
	private String time;

	public WPT(double latitude, double longitude, double elevation) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.elevation = elevation;
		setTime();
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getElevation() {
		return elevation;
	}

	public Date getTime() throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		return df.parse(time);
	}

	private void setTime() {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
		df.setTimeZone(tz);
		this.time = df.format(new Date());
	}
}
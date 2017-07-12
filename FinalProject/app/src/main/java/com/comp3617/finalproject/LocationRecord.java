package com.comp3617.finalproject;

import android.content.Context;
import android.util.Log;

import com.comp3617.finalproject.com.comp3617.finalproject.gpx.GPX;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class LocationRecord {
	static Map<String, String> attributes = new HashMap<>();
	static XmlSerializer root;
	StringWriter write;
	Context context;

	public LocationRecord(Context context) {
		this.context = context;
	}

	// public void init() {
	// root = Xml.newSerializer();
	// write = new StringWriter();
	// try {
	// root.setOutput(write);
	//
	// // Start Document
	// root.startDocument("UTF-8", true);
	//
	// // OpenTag
	// XmlSerializer gpx = root.startTag("", "gpx");
	// for (Map.Entry<String, String> entry : attributes.entrySet()) {
	// gpx.attribute("", entry.getKey(), entry.getValue());
	// }
	//
	// } catch (Exception e) {
	// Log.wtf("Andrew", e.getMessage());
	// }
	// }
	//
	// public void addWpt(double latitude, double longitude, double elevation) {
	// try {
	// root.startTag("", "wpt").attribute("", "lat", Double.toString(latitude)).attribute("", "lon",
	// Double.toString(longitude));
	// root.startTag("", "ele").text(Double.toString(elevation));
	// root.endTag("", "ele");
	// root.startTag("", "time").text(getTime());
	// root.endTag("", "time");
	// root.endTag("", "wpt");
	// } catch (Exception e) {
	// Log.wtf("Andrew", e.getMessage());
	// }
	// }
	//
	// public void endDocument() {
	// try {
	// root.endTag("", "gpx");
	// root.endDocument();
	// Log.d("Andrew", write.toString());
	// } catch (Exception e) {
	// Log.wtf("Andrew", e.getMessage());
	// }
	// }

	private String getTime() {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
		df.setTimeZone(tz);
		String nowAsISO = df.format(new Date());
		return nowAsISO;
	}

	public void saveFile() {
		String fileName = new Date().getTime() + "Test.gpx";
		File file = new File(context.getFilesDir(), fileName);
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(file);
			outputStream.write(write.toString().getBytes());
			outputStream.close();
		} catch (Exception e) {
			Log.wtf("Andrew", e.getMessage());
		}
	}

	public GPX readFile(String fileName) {
		Serializer serializer = new Persister();
		File sourse = new File(context.getFilesDir(), fileName);
		try {
			return serializer.read(GPX.class, sourse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void deleteFile(String fileName) {
		File file = new File(context.getFilesDir(), fileName + ".gpx");
		file.delete();
	}

	public void getListFiles() {
		ArrayList<String> inFiles = new ArrayList<String>();
		File[] files = context.getFilesDir().listFiles();
		for (File file : files) {
			if (file.getName().endsWith(".gpx")) {
				inFiles.add(file.getName());
			}
		}

		Log.d("Andrew", "gpx:" + inFiles.toString());
	}
}
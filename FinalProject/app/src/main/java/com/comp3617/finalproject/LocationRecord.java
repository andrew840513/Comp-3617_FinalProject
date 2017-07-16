package com.comp3617.finalproject;

import android.content.Context;
import android.util.Log;

import com.comp3617.finalproject.gpx.GPX;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

class LocationRecord {
    private Context context;

    LocationRecord(Context context) {
        this.context = context;
    }

    String saveFile(String name, GPX gpx) {
        String fileName = new Date().getTime() + name;
        File file = new File(context.getFilesDir(), fileName + ".gpx");
        Serializer serializer = new Persister();
        try {
            serializer.write(gpx, file);
        } catch (Exception e) {
            Log.e(context.getString(R.string.LocationErrorTag), e.getMessage());
        }
        return fileName;
    }

    GPX readFile(String fileName) {
        Serializer serializer = new Persister();
        File sourse = new File(context.getFilesDir(), fileName + ".gpx");
        try {
            return serializer.read(GPX.class, sourse);
        } catch (Exception e) {
            Log.e(context.getString(R.string.LocationErrorTag), e.getMessage());
        }
        return null;
    }

    void deleteFile(String fileName) {
        File file = new File(context.getFilesDir(), fileName + ".gpx");

        if (file.delete()) {
            Log.d(context.getString(R.string.LocationDebugTag), "success delete file" + fileName);
        } else {
            Log.e(context.getString(R.string.LocationErrorTag), "error delete file" + fileName);
        }
    }

    ArrayList<String> getListFiles() {
        ArrayList<String> inFiles = new ArrayList<>();
        File[] files = context.getFilesDir().listFiles();
        for (File file : files) {
            if (file.getName().endsWith(".gpx")) {
                inFiles.add(file.getName());
            }
        }

        Log.d(context.getString(R.string.LocationDebugTag), "gpx:" + inFiles.toString());
        return inFiles;
    }

    @SuppressWarnings("unused")
    void deleteAllFiles(){
        for (String fileName:getListFiles()) {
            deleteFile(fileName.substring(0,fileName.length()-4));
            Log.d("Andrew","delete gpx:"+ fileName);
        }
    }
}
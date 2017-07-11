package com.comp3617.finalproject;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

class LocationRecord{
    static Map<String,String> attributes = new HashMap<>();
    static XmlSerializer root;
    StringWriter write;
    public LocationRecord(){
        attributes.put("xmlns","\"xmlns\": \"http://www.topografix.com/GPX/1/1\"");
        attributes.put("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
        attributes.put("xsi:schemaLocation","http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd");
        init();
    }


    public void init(){
        root = Xml.newSerializer();
        write = new StringWriter();
        try{
            root.setOutput(write);

            //Start Document
            root.startDocument("UTF-8",true);

            //OpenTag
            XmlSerializer gpx = root.startTag("","gpx");
            for (Map.Entry<String,String> entry: attributes.entrySet()) {
                gpx.attribute("", entry.getKey(), entry.getValue());
            }

        }catch (Exception e){
            Log.wtf("Andrew",e.getMessage());
        }
    }

    public void addWpt(double latitude, double longitude, double elevation){
        try{
            root.startTag("","wpt").attribute("","lat",Double.toString(latitude)).attribute("","lon",Double.toString(longitude));
            root.startTag("","ele").text(Double.toString(elevation));
            root.endTag("","ele");
            root.startTag("","time").text(getTime());
            root.endTag("","time");
            root.endTag("","wpt");
        }catch (Exception e){
            Log.wtf("Andrew",e.getMessage());
        }
    }

    public void endDocument(){
        try{
            root.endTag("","gpx");
            root.endDocument();
            Log.d("Andrew",write.toString());
        }catch (Exception e){
            Log.wtf("Andrew",e.getMessage());
        }
    }

    private String getTime(){
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        return nowAsISO;
    }

    public void saveFile(Context context){
        String fileName = new Date().getTime() + "Test.xml";
        File file = new File(context.getFilesDir(),fileName);
        FileOutputStream outputStream;
        try{
            outputStream = new FileOutputStream(file);
            outputStream.write(write.toString().getBytes());
            outputStream.close();
        }catch (Exception e){
            Log.wtf("Andrew",e.getMessage());
        }
    }

    public void readFile(Context context){

    }
}
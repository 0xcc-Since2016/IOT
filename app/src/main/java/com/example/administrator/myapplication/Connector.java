package com.example.administrator.myapplication;

/**
 * Created by wand on 2016/11/3.
 * Make this class into a powerful
 * Efficient Reusable WEBSITE Connector
 * and Requester.
 */

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.net.URL;
import java.util.Set;


public class Connector {

    private static String target_url = "";
    private static String post_data  = "";

    public Connector(String target_url){

        //Initialize Target_url and request_method
        this.target_url = target_url;
        Log.d("[*]CheckURL ", target_url);

    }

    public String launchGETReq(){

        HttpURLConnection conn = null;
        try {
            URL url = new URL(target_url);
            //Cast url.openConnection into HttpURLConnection:
            //When Open Connection
            //There will be added a http header ahead.
            conn = (HttpURLConnection) url.openConnection();
            //StatusCode Checker:
            /*
            int status_code = conn.getResponseCode();
            if (status_code == 200){
                //Req OK
                //Acuqire InputStream of connector.
                InputStream is =  conn.getInputStream();
                return readHTTPStream(is);
            }else{

                //Req with problem:
                Log.d("[*]STATUSCODEERROR: " , ""+status_code);
                return "";
            }
            */

            InputStream in = new BufferedInputStream(conn.getInputStream());
            return Utils.readStream(1024*16,in);


        }catch(Exception e){
            Log.d("[*]HTTPREQERROR: " , e.toString());
            return "";
        }finally{
            conn.disconnect();
        }

    }

    public String launchPOSTReq(String data){

        //Data-Pattern is:
        //data1=a&data2=b&data3=c etc.
        HttpURLConnection conn = null;
        try{
            URL url = new URL(this.target_url);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            byte[] reqBody = new String(data).getBytes("UTF-8");

            //conn.setChunkedStreamingMode(0);
            //Write Request Body into BufferedOutputStream
            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            writeStream(out, reqBody);

            //Acquire Acks.
            InputStream in = new BufferedInputStream(conn.getInputStream());
            return Utils.readStream(1024*16, in);
            //return readHTTPStream(is);

        }catch(Exception e) {
            Log.d("[*] PostFailed", e.toString());
            return "";
        }finally{
            conn.disconnect();
        }
    }

    public String uploadFile(String usrname, String filename , String filetype , int filelength,  byte[] filecontent){

        HttpURLConnection conn = null;
        try{

            //Init Line of upload part
            String fileheader  = "--" + Settings.fileSeparator + "\r\n";
            fileheader += "Content-Disposition: form-data; name=\"uploaded\"; filename=\"" +
            filename + "." + filetype + "\"" + "\r\n" + "Content-Type: application/" + filetype + "\r\n\r\n";
            byte[] contentHeader = fileheader.getBytes("UTF-8");
            byte[] contentEnder  = Settings.endofline.getBytes("UTF-8");

            filelength += contentHeader.length;
            filelength += contentEnder.length ;
            filelength += "\r\n".getBytes("UTF-8").length;

            URL url = new URL(this.target_url);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            //-----------------------------
            //-------------------------------
            //-------------------------------
            conn.setRequestProperty("Content-Length", filelength+"");
            conn.setRequestProperty("Content-Type","multipart/form-data;boundary=" + Settings.fileSeparator);
            conn.setRequestProperty("AndroidReq", Settings.sec_token);
            conn.setRequestProperty("username", usrname);
            //conn.setChunkedStreamingMode(0);
            //Write Request Body into BufferedOutputStream
            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            out.write(contentHeader);
            out.write(filecontent);
            out.write("\r\n".getBytes("UTF-8"));
            out.write(contentEnder);
            out.flush();
            out.close();
            //Acquire Acks.
            InputStream in = new BufferedInputStream(conn.getInputStream());
            return Utils.readStream(1024*16,in);
            //return readHTTPStream(is);
        }catch(Exception e) {
            Log.d("[*] PostFailed", e.toString());
            return "";
        }finally{
            conn.disconnect();
        }
    }


    public void setTarget_url(String url){

        this.target_url = url;
    }

    public void writeStream(OutputStream out,byte[] reqBody) throws IOException{

        out.write(reqBody);
        out.flush();
        out.close();
    }



}

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
            return readHTTPStream(in);

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
            return readHTTPStream(in);
            //return readHTTPStream(is);
        }catch(Exception e) {
            Log.d("[*] PostFailed", e.toString());
            return "";
        }finally{
            conn.disconnect();
        }
    }

    public String uploadFile(byte[] filecontent){

        HttpURLConnection conn = null;
        try{
            URL url = new URL(this.target_url);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //conn.setChunkedStreamingMode(0);
            //Write Request Body into BufferedOutputStream
            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            writeStream(out, filecontent);

            //Acquire Acks.
            InputStream in = new BufferedInputStream(conn.getInputStream());
            return readHTTPStream(in);
            //return readHTTPStream(is);
        }catch(Exception e) {
            Log.d("[*] PostFailed", e.toString());
            return "";
        }finally{
            conn.disconnect();
        }
    }


    public String readHTTPStream(InputStream is){

        byte[] res_bytes = null;
        BufferedInputStream bufferedis    = new BufferedInputStream(is);
        ByteArrayOutputStream byteArrayos = new ByteArrayOutputStream();
        BufferedOutputStream bufferedos   = new BufferedOutputStream(byteArrayos);
        byte[] buffer = new byte[1024 * 16];
        int length    = 0;
        try{

            //Get Materials from one InputStream: bufferedis
            while((length = bufferedis.read(buffer)) > 0){
                //EveryTime Got FROM length-byte from buffer
                //Write them into bufferedos
                bufferedos.write(buffer, 0, length);
            }
            //Whole NetWork-Packet is inside bufferedos and byteArrayos.
            //Flush them out now.
            bufferedos.flush();
            res_bytes = byteArrayos.toByteArray();
            return getStringWithBytes(res_bytes);

        }catch(Exception e){

            Log.d("[*]READINGBUFFERERROR", e.toString());
            return "";

        }finally{
            //Trying to close bufferedos
            try{
                bufferedos.close();
            }catch(Exception e){
                Log.d("[*]CLOSEBUFFERERROR", e.toString());
            }
            try{
                bufferedis.close();
            }catch(Exception e){
                Log.d("[*]CLOSEBUFFERERROR", e.toString());
            }
        }
    }

    public String getStringWithBytes(byte[] bytes){

        String str = "";
        try{
            str = new String(bytes, "UTF-8");
        }catch(Exception e){
            Log.d("[*]BYTETOSTRINGERROR", e.toString());
        }
        return str;
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

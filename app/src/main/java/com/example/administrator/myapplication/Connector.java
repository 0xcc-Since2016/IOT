package com.example.administrator.myapplication;

/**
 * Created by wand on 2016/10/27.
 */

//Import Http Libs.
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.*;
import java.util.List;
import java.util.Map;
import android.util.Log;

public class Connector {

    public static final String TIME_OUT = "操作超时";
    public static final String NETWORK_GET = "NETWORK_GET";
    public static final String NETWORK_POST= "NETWORK_POST";

    private String username = "";
    private String password = "";

    public Connector(String user, String pass){

        //Set User and Pass
        username = "uname=" + user;
        password = "upwd=" + pass;

    }

    public String HttpLogin(){

        String res = null;
        HttpURLConnection conn  = null;
        String responseBody     = null;
        String responseHeader   = null;
        //URL Package Method.
        try {
            URL target_url = new URL(Settings.login_url);
            //Open a url connection.
            conn = (HttpURLConnection)target_url.openConnection();
            conn.setRequestMethod("POST");
            //Open Requester in explicit-mode.
            conn.setDoOutput(true);
            conn.connect();
            //String requestHeader = getRequestHeader(conn);
            OutputStream os = conn.getOutputStream();
            byte[] requestBody = new String(username + "&" + password).getBytes("UTF-8");
            //Write Http-Request Body into OutputStream and send.
            os.write(requestBody);
            os.flush();
            os.close();
            //Connection data returned here.
            InputStream is = conn.getInputStream();
            responseBody     = getStringByBytes(getBytesByInputStream(is));
            responseHeader   = getResponseHeader(conn);

        }catch(Exception e){
            e.printStackTrace();
            return "Connection to Server Error.";
        }
        finally{
            if(conn!=null)
                conn.disconnect();
            return responseBody;
        }

    }

    public String getResponseHeader(HttpURLConnection conn) {
        Map<String, List<String>> responseHeaderMap = conn.getHeaderFields();
        int size = responseHeaderMap.size();
        StringBuilder sbResponseHeader = new StringBuilder();
        for(int i = 0; i < size; i++){
            String responseHeaderKey = conn.getHeaderFieldKey(i);
            String responseHeaderValue = conn.getHeaderField(i);
            sbResponseHeader.append(responseHeaderKey);
            sbResponseHeader.append(":");
            sbResponseHeader.append(responseHeaderValue);
            sbResponseHeader.append("\n");
        }
        return sbResponseHeader.toString();
    }

    public byte[] getBytesByInputStream(InputStream is) {
        byte[] bytes = null;
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos);
        byte[] buffer = new byte[1024 * 8];
        int length = 0;
        try {
            //Read till finish in.
            while ((length = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
            bos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }

    public String getStringByBytes(byte[] bytes) {
        String str = "";
        try {
            str = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public boolean Check_login(){

        String ret = HttpLogin();
        ret = ret.trim();
        if(ret.equals("OK")){
            Log.d("Ret-MSG",ret);
            return true;
        }
        else{
            return false;
        }
    }

}

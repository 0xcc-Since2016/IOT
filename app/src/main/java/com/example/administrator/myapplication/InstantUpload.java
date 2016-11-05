package com.example.administrator.myapplication;

/**
 * Created by wand on 2016/11/6.
 */

import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class InstantUpload extends Thread{

    String username = "";
    String filename = "";
    String filedir  = "";

    public InstantUpload(String usrname, String filename, String filedir){

        this.username = usrname;
        this.filename = filename;
        this.filedir  = filedir;

    }

    @Override
    public void run(){

        Connector conn = new Connector(Settings.upload_url);
        DataStore dtstore = new DataStore();

        try {

            File file = new File(this.filedir);

            String[] temp = this.filename.split("\\.");
            String suffix = temp[temp.length-1];

            byte[] content = dtstore.getByteFile(this.filedir);
            int filelength = content.length;
            String fileName= this.filename.replace("." + suffix , "");
            String uploadres = conn.uploadFile(this.username, fileName,suffix,filelength,content);

        }catch(Exception e){
            Log.d("FileUPLOADERROR", e.toString());
        }
    }
}


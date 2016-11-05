package com.example.administrator.myapplication;

import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wand on 2016/11/4.
 */



public class FileUpload extends Thread{

    String usrname = "";
    public FileUpload(String usrname){

        this.usrname = usrname;
    }

    @Override
    public void run(){

        Map<String,Object> UpdateLog = new HashMap<String,Object>();
        Connector conn = new Connector(Settings.upload_url);
        DataStore dtstore = new DataStore();

        while(true){
            try {
                sleep(5000);
                File file = new File(Settings.folder + "/cache");
                String[] filename = file.list();
                for(int i=0;i < filename.length; i++)
                {
                    String tempfile   = filename[i];
                    String[] temp = filename[i].split("\\.");
                    String suffix = temp[temp.length-1];
                    //If Is Record , Upload it.
                    if ( suffix.equals("arm") && !UpdateLog.containsKey(tempfile)){

                        byte[] content = dtstore.getByteFile(Settings.folder + "/cache" + "/" + tempfile);
                        int filelength = content.length;
                        String fileName= tempfile.replace("." + suffix , "");
                        String uploadres = conn.uploadFile(this.usrname, fileName,suffix,filelength,content);
                        UpdateLog.put(tempfile, "1");
                    }
                }
            }catch(Exception e){
                Log.d("FileUPLOADERROR", e.toString());
            }
        }

    }

}

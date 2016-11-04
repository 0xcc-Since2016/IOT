package com.example.administrator.myapplication;

import android.util.Log;

import java.io.File;

/**
 * Created by wand on 2016/11/4.
 */



public class FileUpload extends Thread{

    @Override
    public void run(){

        Connector conn = new Connector(Settings.upload_url);
        while(true){
            try {
                sleep(5000);
                File file = new File(Settings.folder + "/cache");
                String[] filename = file.list();

            }catch(Exception e){
                Log.d("FileUPLOADERROR", e.toString());
            }
        }

    }

}

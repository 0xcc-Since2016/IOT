package com.example.administrator.myapplication;

/**
 * Created by wand on 2016/10/27.
 */


import java.io.*;

//Query for how to use android.content.Context?
public class DataStore{

    //Save buffer content to default folder of application.
    ///data/data/package/file.txt
    public void saveToFile(byte[] buffer , String filepath) throws IOException{

        File testFile = new File(filepath);
        //Use Appending mode to write content into file.
        FileOutputStream fs_1 = new FileOutputStream(testFile,true);
        fs_1.write(buffer);
        fs_1.close();
    }

    public String getFromFile(String filepath) throws IOException{

        byte[] buffer = new byte[1024*4];
        File testFile = new File(filepath);
        FileInputStream is_1 = new FileInputStream(testFile);
        is_1.read(buffer);
        return new String(buffer, "UTF-8");

    }

}

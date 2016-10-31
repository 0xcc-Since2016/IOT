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

}

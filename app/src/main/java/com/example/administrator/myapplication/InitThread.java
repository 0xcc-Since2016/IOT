package com.example.administrator.myapplication;

/**
 * Created by wand on 2016/10/28.
 * This Thread Prepares For Some Init Works
 * Including: Checking Connection with remote server
 * Getting Access from remote database
 * Init File folders Under Certain Directory
 * Create another thread to prevent block.
 */

import java.io.File;

public class InitThread extends Thread{

    @Override
    public void run(){

        //Check Some Folders' existance:
        CheckExistence();
        //SQLite Init Process:
        InitSQLite();
        //Check connection to Remote Server:80

    }

    public void CheckExistence(){

        //Check Pic Folder
        //Check Video Folder
        //Check Sound Rec Folder
        //GPS stored in Database
        //Other things like user-data
        //store path inside database.
        File file = new File(Settings.folder + Settings.pic_folder);
        if(!file.exists()){
            file.mkdir();
        }
        file = new File(Settings.folder + Settings.video_folder);
        if(!file.exists()){
            file.mkdir();
        }
        file = new File(Settings.folder + Settings.soundrec_folder);
        if(!file.exists()){
            file.mkdir();
        }
        file = new File(Settings.folder + Settings.database_folder);
        if(!file.exists()){
            file.mkdir();
        }
    }

    public void InitSQLite(){

        //Init Process starts.
        SqlConnector conn = new SqlConnector();

    }

}

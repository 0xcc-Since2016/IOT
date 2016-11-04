package com.example.administrator.myapplication;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by wand on 2016/10/27.
 * SQLite Connector
 * Use SQLite to store necessary information
 * needed for next step useage.
 */

public class SqlConnector {

    private SQLiteDatabase db_opt = null;

    public SqlConnector(){

        //Create Or Open a database with db_name specified.
        db_opt = SQLiteDatabase.openOrCreateDatabase(Settings.folder + "/" + Settings.db_name, null);
        InitTables();
    }

    //CURD Operations Listed here:

    //Init Each Table If they're not present.
    public void InitTables(){
        try {
            db_opt.execSQL(Settings.create_user);
            db_opt.execSQL(Settings.create_gps);
            db_opt.execSQL(Settings.create_pic);
            db_opt.execSQL(Settings.create_voice);
        }catch(Exception e){
            Log.d("[*]Exec_CreateTblError", e.toString());
        }
        finally {
            db_opt.close();
        }
    }

}

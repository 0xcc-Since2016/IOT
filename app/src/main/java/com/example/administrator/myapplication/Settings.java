package com.example.administrator.myapplication;

/**
 * Created by wand on 2016/10/28.
 * Basic Configurations On
 * Database Connector
 * Server Connector
 * FileBasicPath
 * etc
 */

public class Settings {

    //Basic Settings
    public static String folder         = "/data/data/com.example.administrator.myapplication";
    public static String label_file     = "/Label.txt";
    public static String Server_ip      = "192.168.191.2";
    public static int    Server_port    = 3000; //Server Listen port 3000 by Default.

    //Folder Names
    public static String pic_folder     = "/pic";
    public static String video_folder   = "/video";
    public static String soundrec_folder= "/record";
    public static String database_folder= "/databases";

    //Store Setttings
    public static String label          = folder + "/labels/";

    //SQLite Settings
    public static String db_name        = "INSTREC.db";
    public static String username       = "0xcc";
    public static String passwd         = "since2016";
    public static String user_tb        = "user";
    public static String gps_tb         = "gps";
    public static String pic_tb         = "pic";
    public static String voice_tb       = "voi";

    //Create table element
    public static String create_user    = "CREATE TABLE IF NOT EXISTS " + Settings.user_tb
                                        + "(username varchar(50) PRIMARY KEY, Passwd varchar(50))" ;
    public static String create_gps     = "CREATE TABLE IF NOT EXISTS " + Settings.gps_tb
                                        + "(longitude varchar(50), latitude varchar(50), createTime varchar(50))";
    public static String create_pic     = "CREATE TABLE IF NOT EXISTS " + Settings.pic_tb
                                        + "(pic_path varchar(200), createTime varchar(100))";
    public static String create_voice   = "CREATE TABLE IF NOT EXISTS " + Settings.voice_tb
                                        + "(voi_path varchar(200), createTime varchar(100))";

    //Remote Server Settings
    public static String sec_token      = "amRrMS44MA==";
    public static String login_url      = "http://" + Server_ip + ":" + Server_port + "/login";
    public static String defaultMethod  = "POST";
    public static String register_url   = "http://" + Server_ip + ":" + Server_port + "/register";
    public static String upload_url     = "http://" + Server_ip + ":" + Server_port + "/uploaded";

}

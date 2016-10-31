package com.example.administrator.myapplication;

/**
 * Created by wand on 2016/10/31.
 */

public class AsyncHttp extends Thread{

    String username = "";
    String password = "";

    public AsyncHttp(String user, String pass){

        username = user;
        password = pass;

    }
    @Override
    public void run(){

        Connector conn = new Connector(username , password);

    }
}

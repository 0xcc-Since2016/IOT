package com.example.administrator.myapplication;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by wand on 2016/11/5.
 */


public class Utils {

    public static String readStream(int bufferBytes, InputStream is){

        byte[] res_bytes;
        int length;
        BufferedInputStream bufferedis    = new BufferedInputStream(is);
        ByteArrayOutputStream byteArrayos = new ByteArrayOutputStream();
        BufferedOutputStream bufferedos   = new BufferedOutputStream(byteArrayos);
        byte[] buffer = new byte[bufferBytes];

        try{

            //Get Materials from one InputStream: bufferedis
            while((length = bufferedis.read(buffer)) > 0){
                //EveryTime Got FROM length-byte from buffer
                //Write them into bufferedos
                bufferedos.write(buffer, 0, length);
            }

            //Whole NetWork-Packet is inside bufferedos and byteArrayos.
            //Flush them out now.
            bufferedos.flush();
            res_bytes = byteArrayos.toByteArray();
            return getStringWithBytes(res_bytes);

        }catch(Exception e){

            Log.d("[*]READINGBUFFERERROR", e.toString());
            return "";

        }finally{
            //Trying to close bufferedos
            try{
                bufferedos.close();
            }catch(Exception e){
                Log.d("[*]CLOSEBUFFERERROR", e.toString());
            }
            try{
                bufferedis.close();
            }catch(Exception e){
                Log.d("[*]CLOSEBUFFERERROR", e.toString());
            }
        }
    }

    public static String getStringWithBytes(byte[] bytes){

        String str = "";
        try{
            str = new String(bytes, "UTF-8");
        }catch(Exception e){
            Log.d("[*]BYTETOSTRINGERROR", e.toString());
        }
        return str;
    }

    public static byte[] byteMerger(byte[] bytearray1, byte[] bytearray2){

        //Copy in the Sequence of [bytearray1 , bytearray2]
        byte[] bytearray = new byte[bytearray1.length + bytearray2.length];
        System.arraycopy(bytearray1,0,bytearray,0,bytearray1.length);
        System.arraycopy(bytearray2,0,bytearray,bytearray1.length,bytearray2.length);
        return bytearray;
    }


    public static byte[] readByteStream(int bufferBytes, InputStream is){

        byte[] res_bytes;
        int length;
        BufferedInputStream bufferedis    = new BufferedInputStream(is);
        ByteArrayOutputStream byteArrayos = new ByteArrayOutputStream();
        BufferedOutputStream bufferedos   = new BufferedOutputStream(byteArrayos);
        byte[] buffer = new byte[bufferBytes];

        try{

            //Get Materials from one InputStream: bufferedis
            while((length = bufferedis.read(buffer)) > 0){
                //EveryTime Got FROM length-byte from buffer
                //Write them into bufferedos
                bufferedos.write(buffer, 0, length);
            }

            //Whole NetWork-Packet is inside bufferedos and byteArrayos.
            //Flush them out now.
            bufferedos.flush();
            res_bytes = byteArrayos.toByteArray();
            return res_bytes;

        }catch(Exception e){

            Log.d("[*]READINGBUFFERERROR", e.toString());
            return "".getBytes();

        }finally{
            //Trying to close bufferedos
            try{
                bufferedos.close();
            }catch(Exception e){
                Log.d("[*]CLOSEBUFFERERROR", e.toString());
            }
            try{
                bufferedis.close();
            }catch(Exception e){
                Log.d("[*]CLOSEBUFFERERROR", e.toString());
            }
        }
    }

}



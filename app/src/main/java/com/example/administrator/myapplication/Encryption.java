package com.example.administrator.myapplication;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class Encryption {
    public static String sKey ="1234567812345678";


    public static String encrypt(String sSrc) {
        try {
            if (sKey == null) {
                return null;
            }

            if (sKey.length() != 16) {
                return null;
            }
            byte[] raw = sKey.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes());

            return new BASE64Encoder().encode(encrypted);
        }catch (Exception ex){
            System.out.println(ex.toString());
            return null;
        }

    }


    public static String decrypt(String sSrc) {
        try {

            if (sKey == null) {
                return null;
            }

            if (sKey.length() != 16) {
                return null;
            }
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("0102030405060708"
                    .getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original);
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

//    public static void main(String[] args) throws Exception {
//
//        String cKey = "1234567812345678";
//
//        String cSrc = "data";
//        System.out.println(cSrc);
//
//        String enString = AES.Encrypt(cSrc, cKey);
//
//        String DeString = AES.Decrypt(enString, cKey);
//
//    }
}
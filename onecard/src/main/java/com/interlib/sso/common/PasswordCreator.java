package com.interlib.sso.common;


import sun.misc.BASE64Encoder;

import java.util.Random;
import java.security.MessageDigest;
/**
 * 密码生成规则工具util
 * 
 * @author Administrator
 *
 */
public class PasswordCreator {

    public static class GeneratedPassword {
        private String clearText;
        private String encrypted;

        private GeneratedPassword(String clearText, String encrypted) {
            this.clearText = clearText;
            this.encrypted = encrypted;
        }

        public String getClearText() {
            return clearText;
        }

        public String getEncrypted() {
            return encrypted;
        }
    }
    //the below number 13,58,74 you can change any number for you company.
    public static GeneratedPassword generate(int length) {
        Random rnd = new Random(System.currentTimeMillis());
        int letterLength = length - 1;
        byte[] buf = new byte[letterLength];
        for (int i = 0; i < letterLength; ++i) {
            int idx = Math.abs(rnd.nextInt()) % 13;
            int offset = idx % 2 == 1 ? 58 : 74;
            int ch = offset + idx;
            buf[i] = (byte) ch;
        }
        String clearText = insertRandomNumber(buf);
        String encrypted = encryptUnsaltedSha1(clearText);

        return new GeneratedPassword(clearText, encrypted);
    }
    //the below number 9, you can change any number for you company.
    private static String insertRandomNumber(byte[] buf) {
        StringBuffer sb = new StringBuffer(new String(buf));
        Random rnd = new Random(System.currentTimeMillis());
        int number = Math.abs(rnd.nextInt()) % 9;
        int offset = Math.abs(rnd.nextInt()) % (sb.length());
        sb.insert(offset, number);
        return sb.toString();
    }

    private static String encryptUnsaltedSha1(String password) {
        try {
            String alg = "SHA-1";
            MessageDigest digest = MessageDigest.getInstance(alg);
            digest.update(password.getBytes("UTF-8"));
            return new BASE64Encoder().encode(digest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Other service can invoke this util like below:
      public static void main(String[] args) {
        GeneratedPassword password=generate(6);
        System.out.println(password.getClearText());
        System.out.println(password.getEncrypted());
        }
}
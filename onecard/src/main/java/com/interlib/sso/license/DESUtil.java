/*
 * DESTest.java 1.0.6 2005-12-1
 * Copyright 2002 Tuchang, Inc. All rights reserved.
 * 创建日期 2005-12-1
 */
package com.interlib.sso.license;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * DES的加密类。
 * @author toofu
 * @reviewer toofu
 * @version 1.0, 2003-9-12
 * @env JDK1.4.1_02
 * @modified toofu, 2005-12-1
 */
public class DESUtil {
    public static int _DES = 1;

    public static int _DESede = 2;

    public static int _Blowfish = 3;

    private Cipher p_Cipher;

    private SecretKey p_Key;

    private String p_Algorithm;
    /**
     * 选择运算法则
     * @param al 参数为整数
     */
    private void selectAlgorithm(int al) {
        switch (al) {
        default:
        case 1:
            this.p_Algorithm = "DES";
            break;
        case 2:
            this.p_Algorithm = "DESede";
            break;
        case 3:
            this.p_Algorithm = "Blowfish";
            break;
        }
    }

    public DESUtil(int algorithm) throws Exception {
        this.selectAlgorithm(algorithm);
        //可以加载没有出现的其他类型
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        //根据不同的类型获取密码生成器
        this.p_Cipher = Cipher.getInstance(this.p_Algorithm);
    }

    /**
     * 获取所生成密码的字节型值
     * @return
     */
    public byte[] getKey(String key) {
        return this.checkKey(key).getEncoded();
    }
    /**
     * 初始化密码，这里的密码使用来声称密码串的
     * @param key 这个参数是非随机产生密码时，要输入的密码。从而制作出密码。
     * @return
     */
    private SecretKey checkKey(String key) {
        try {
            if (this.p_Key == null) {
                KeyGenerator keygen = KeyGenerator
                        .getInstance(this.p_Algorithm);
                
//                 SecureRandom sr = new SecureRandom(key.getBytes());
                 SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
                 sr.setSeed(key.getBytes());
                 keygen.init(168, sr);
                this.p_Key = keygen.generateKey();
            }
        } catch (Exception nsae) {
            System.out.println("生成密码串错误撒!");
        }
        return this.p_Key;
    }

    public void setKey(byte[] enckey) {
        this.p_Key = new SecretKeySpec(enckey, this.p_Algorithm);
    }

    public byte[] encode(byte[] data, byte[] enckey) throws Exception {
        this.setKey(enckey);
        this.p_Cipher.init(Cipher.ENCRYPT_MODE,this.p_Key);//this.checkKey());
        return this.p_Cipher.doFinal(data);
    }
    public byte[] decode(byte[] encdata, byte[] enckey) throws Exception {
        this.setKey(enckey);
        this.p_Cipher.init(Cipher.DECRYPT_MODE, this.p_Key);
        return this.p_Cipher.doFinal(encdata);
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int i = 0; i < b.length; i++) {
            stmp = Integer.toHexString(b[i] & 0xFF);
            if (stmp.length() == 1) {
                hs += "0" + stmp;
            } else {
                hs += stmp;
            }
        }
        return hs.toUpperCase();
    }

    public static byte[] hex2byte(String hex) throws IllegalArgumentException {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }
        char[] arr = hex.toCharArray();
        byte[] b = new byte[hex.length() / 2];
        for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
            String swap = "" + arr[i++] + arr[i];
            int byteint = Integer.parseInt(swap, 16) & 0xFF;
            b[j] = new Integer(byteint).byteValue();
        }
        return b;
    }
    public static String myDESEncode(String info){
    	//DES 类
       try {
			DESUtil dst = new DESUtil(DESUtil._DESede);
//			密钥文件(byte) 密匙数组
	        byte[] key = dst.getKey("{中华&#$图创@!?><interlib}"); //获取随机生成的密钥.以字节的方式显示出来.
	        //String hexkey = DESUtil.byte2hex(key); //生成十六进制密钥
	        byte[] enc = dst.encode(info.getBytes(),key); //生成加密文件(byte)
	        //byte[] dec = dst.decode((new String(enc)).getBytes(), key);//.decode(enc,DESUtil.hex2byte(hexkey));
	        //System.out.println("dd"+ new String(dec));
	        return new String(DESUtil.byte2hex(enc));
		} catch (Exception e) {
			System.out.println("认证识别错误！");
		} // 声明DES 的类型
        return "-1";
    }
    public static String myDESDecode(String info){
    	//DES 类
        DESUtil dst;
		try {
			dst = new DESUtil(DESUtil._DESede);
//			密钥文件(byte) 密匙数组
	        byte[] key = dst.getKey("{中华&#$图创@!?><interlib}"); //获取随机生成的密钥.以字节的方式显示出来.
	        //String hexkey = DESUtil.byte2hex(key); //生成十六进制密钥
	        byte[] dec = dst.decode(DESUtil.hex2byte(info), key);//.decode(enc,DESUtil.hex2byte(hexkey));
	        return new String(dec);
		} catch (Exception e) {
			System.out.println("认证识别错误！");
		} // 声明DES 的类型
        return "-1";
    }
    //public static boid
    public static void main(String[] args) throws Exception {
    }

}
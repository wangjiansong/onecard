/*
 * LocalInfo.java 1.0.6 2005-12-1
 * Copyright 2002 Tuchang, Inc. All rights reserved.
 * 创建日期 2005-12-1
 */
package com.interlib.sso.license;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;




/**
 * 静态类,初始化的时候会把所有的成员变量及成员函数全部初始化
 * 这是用户在安装之前发出的授权请求串的处理.
 * 这个类做成图形界面的比较容易理解，但是处理图形界面相当罗嗦，这里先进性实现。
 * @author toofu
 * @reviewer 	toofu
 * @version     1.0, 2003-9-12
 * @env		    JDK1.4.1	
 * @modified	toofu,	2005-12-1
 */

public class LocalInfo {
    /**
     * 本机器的操作系统类型
     */
    public static String LOCALHOST_OS_TYPE=getOperatorSystem();
    /**
     * 本机器的最后一个网卡的物理地址
     */
    public static String LOCALHOST_MAC=getLocalMac();
    /**
     * 本机器的机器名字
     */
    public static String LOCALHOST_NAME=getHostName();
    /**
     * 本机器的IP地址
     */
    public static String LOCALHOST_IP=getIpAddress();
    /**
     * 本机器的机器信息属性
     * @return
     */
    public static Properties LOCALHOST_INFO_PROPERTIES=getInfoProperties();
    /**
     * 获取本机器的机器信息属性
     * @return
     */
    public static Properties getInfoProperties(){
            Properties p=new Properties();
            p.put("LOCALHOST_OS_TYPE",LOCALHOST_OS_TYPE);
            p.put("LOCALHOST_MAC",LOCALHOST_MAC);
            p.put("LOCALHOST_NAME",LOCALHOST_NAME);
            p.put("LOCALHOST_IP",LOCALHOST_IP);
            return p;
    }
    /**
     * 获取本机器最后一个网卡的物理地址，返回本地物理地址。
     * 如果返回值为空,说明返回的是空
     * @return Mac String
     */
    public static String getLocalMac() {
        return FindMacAddr.getMacAddress();
    }
    /**
     * 获取本机器机器名,获取失败,返回空字符串
     * @return hostName
     */
    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName()+"";
        } catch (UnknownHostException e) {
            System.out.println("无法获取机器名!");
        }
        return "";
    }
    /**
     * 获取本机器的最后一个网卡的IP地址.如果获取失败,返回空字符串
     * @return hostIP  
     */
    public static String getIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 获取本机器的操作系统类型
     * @param args
     */
    public static String getOperatorSystem() {
        return System.getProperty("os.name");
    }
    /**
     * 测试Main类
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("获取本机器的操作系统类型:"+LOCALHOST_OS_TYPE);
        System.out.println("本机器最后一个网卡的MAC:"+LOCALHOST_MAC);
        System.out.println("本机器最后一个网卡的IP:"+LOCALHOST_IP);
        System.out.println("本机器的机器名:"+LOCALHOST_NAME);
        System.out.println("本机器的机器名:"+LOCALHOST_INFO_PROPERTIES);
    }
}

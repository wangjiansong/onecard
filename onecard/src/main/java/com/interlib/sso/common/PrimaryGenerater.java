package com.interlib.sso.common;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;  

import sun.security.x509.SerialNumber;
/**
 * 流水号生成规则
 * @author Administrator
 *
 */

/**
 * 无聊的时候写了一个简易的生成流水号的类生成的格式大致如下:
 * XXyyMMdd0001
*/
public class PrimaryGenerater {
	private static int i = 0;
    
    public static void main(String[] args) throws Exception
    {
    	String num = "90000000";
	 int numvalue = Integer.parseInt(num) + 1;
	 String num_value = "E999"+numvalue; 
            System.out.println(num_value);
        
    }
     
    public static String getNext() throws Exception
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String today = formatter.format(new Date());
         
        String next = String.valueOf(i++);
         
        switch (next.length())
        {
            case 1:
                next = "E9999000000" + next;
                break;
                 
            case 2:
                next = "E999900000" + next;
                break;
                 
            case 3:
                next = "E99990000" + next;
                break;
            case 4:
                next = "E9999000" + next;
                break;
            case 5:
                next = "E999900" + next;
                break;
            case 6:
                next = "E99990" + next;
                break;
            case 7:
                next = "E9999" + next;
                break;
            default:
                throw new Exception("超过最大值");
        }
         
        return next;
    }
	
}

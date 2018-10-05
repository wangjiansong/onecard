package sms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import net.sf.json.JSONObject;

public class TestSms {
	 /** 
     * @param args 
     */  
	public static void main(String[] args) throws IOException {
        System.out.println(getData());
    }
    public static String getData() throws IOException {
    	String vcode = "";
	    //生成验证码 
	    for (int i = 0; i < 6; i++) {
	        vcode = vcode + (int) (Math.random() * 9);
	    }
    	
        // 创建指定url的url对象,这里的地址是:淘宝商品搜索建议
//        URL url = new URL("https://suggest.taobao.com/sug?code=utf-8&q=电脑&callback=cb");
    	
    	String strURL = "http://121.14.195.204:5555/smscloudservice/sms/SendSms?smsUser=sms0001&smsPassword=uy901wa8" +
   	         "&phone=13986154687&content=" +
   	        		 URLEncoder.encode("【统一用户】虚拟读者注册验证码"+vcode
   	        				 +"，60秒之内有效，请不要把验证码泄露给其他人。如非本人操作，请不要理会。", "UTF-8")+"&sendFirst=1";
               URL url = new URL(strURL);
    	
        // 创建http链接对象
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        // 设置请求方式
        con.setRequestMethod("POST");
        // 打开链接,上一步和该步骤作用相同，可以省略
        con.connect();
         
        // 获取请求返回内容并设置编码为UTF-8
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        // 将返回数据拼接为字符串
        StringBuffer sb = new StringBuffer();
        // 临时字符串
        String temp = null;
        // 获取数据
        while ((temp = reader.readLine()) != null) {
            sb.append(temp);
        }
        // 关闭流
        reader.close();
        return sb.toString();
    }
  
}  

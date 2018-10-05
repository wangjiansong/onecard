package com.interlib.sso.acs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/** 
 * @author home
 */
public class Clients extends Thread {

	private static Socket server;
	
	private PrintStream out;
	
	public String send(String sendMsg, String charset) throws UnsupportedEncodingException, IOException {
		String returnMsg = "";
		out = new PrintStream(server.getOutputStream(), false, charset);
		out.println(sendMsg);
		out.flush();
		BufferedReader in = new BufferedReader(new InputStreamReader(server
				.getInputStream(), charset));
		returnMsg = in.readLine();
		System.out.println(returnMsg);
		return returnMsg;
	}
	public void close() throws IOException {
		server.close();
	}
	
	public void startup(String ip, int port) throws UnknownHostException, IOException {
		server = new Socket(InetAddress.getByName(ip), port);
	}
}

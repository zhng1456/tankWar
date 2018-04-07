package com.wang.core;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetClient {
	public void connect(String IP,int port){
		try {
			Socket s = new Socket(IP,port);
			System.out.println("Connected to Server");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

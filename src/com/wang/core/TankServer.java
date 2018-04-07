package com.wang.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务器类
 * @author Administrator
 *
 */
public class TankServer {
	/**
	 * TCP监听端口
	 */
	public static final int TCP_PORT=8888;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ServerSocket ss=new ServerSocket(TCP_PORT);
			while(true){
				Socket s=ss.accept();
				System.out.println("A Client Connect Addr:"+s.getInetAddress()+":"+s.getPort());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

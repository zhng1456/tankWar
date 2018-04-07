package com.wang.core;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
	//一系列连上来的客户端
	List<Client> clients=new ArrayList<Client>();
	public void start(){
		try {
			ServerSocket ss=new ServerSocket(TCP_PORT);
			while(true){
				Socket s=ss.accept();
				System.out.println("A Client Connect Addr:"+s.getInetAddress()+":"+s.getPort());
				DataInputStream dis=new DataInputStream(s.getInputStream());
				//接受客户端发来的udp端口
				int udpPort=dis.readInt();
				String ip=s.getInetAddress().getHostAddress();
				Client c=new Client(ip, udpPort);
				clients.add(c);
				s.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TankServer().start();
	}
	/**
	 * 内部类，保留连接上来的客户端的基本信息
	 * @author Administrator
	 *
	 */
	private class Client{
		String IP;
		int udpPort;
		public Client(String iP, int udpPort) {
			IP = iP;
			this.udpPort = udpPort;
		}
		
	}
}

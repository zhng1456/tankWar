package com.wang.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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
	public static final int UDP_PORT=6666;
	//客户端的标识
	private static int ID=100;
	//一系列连上来的客户端
	List<Client> clients=new ArrayList<Client>();
	public void start(){
		//启动UDP的线程
		new Thread(new UDPThread()).start();
		ServerSocket ss=null;
		try {
			ss=new ServerSocket(TCP_PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("UDP Thread start at port:"+UDP_PORT);
		while(true){
			Socket s=null;
			try {
				s=ss.accept();
				System.out.println("A Client Connect Addr:"+s.getInetAddress()+":"+s.getPort());
				DataInputStream dis=new DataInputStream(s.getInputStream());
				//接受客户端发来的udp端口
				int udpPort=dis.readInt();
				String ip=s.getInetAddress().getHostAddress();
				Client c=new Client(ip, udpPort);
				clients.add(c);
				//发给客户端，客户端的id
				DataOutputStream dps=new DataOutputStream(s.getOutputStream());
				dps.writeInt(ID++);
			} catch (Exception e) {
				// TODO: handle exception
			}finally{
				if(s!=null){
					try {
						s.close();
						s=null;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
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
	private class UDPThread implements Runnable{
		byte[] buf=new byte[1024];
		/**
		 * 接受客户端发来的数据，并转发给其他客户端
		 */
		public void run() {
			// TODO Auto-generated method stub
				DatagramSocket ds=null;
				try {
					ds=new DatagramSocket(UDP_PORT);
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			while(ds!=null){
				DatagramPacket dp=new DatagramPacket(buf,buf.length);
				try {
					ds.receive(dp);
					System.out.println("a packet recieve!");
					//将数据转发给其他客户端
					for(int i=0;i<clients.size();i++){
						Client c=clients.get(i);
						dp.setSocketAddress(new InetSocketAddress(c.IP,c.udpPort));
						ds.send(dp);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

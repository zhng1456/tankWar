package com.wang.core;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class NetClient {
	//UDP端口的起始端口
	private static int UDP_PORT_START=2223;
	private int udpPort;
	private DatagramSocket ds=null;
	TankClient tc;
	public NetClient(TankClient tc){
		this.tc=tc;
		udpPort=UDP_PORT_START++;
		try {
			ds=new DatagramSocket(udpPort);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void connect(String IP,int port){
		Socket s=null;
		try {
			s = new Socket(IP,port);
			//将这个客户端的UDP端口号发送过去
			DataOutputStream dos=new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			//接受唯一的id
			DataInputStream dis=new DataInputStream(s.getInputStream());
			int id=dis.readInt();
			System.out.println("Connected to Server and Server give me a ID:"+id);
			//保存id号
			tc.myTank.setId(id);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		TankNewMsg msg=new TankNewMsg(tc.myTank);
		send(msg);
		
		new Thread(new UDPRecieveThread()).start();
	}
	public void send(TankNewMsg msg){
		msg.send(ds,"127.0.0.1",TankServer.UDP_PORT);
	}
	/**
	 * 内部类，客户端UDP的接收线程
	 * @author Administrator
	 *
	 */
	private class UDPRecieveThread implements Runnable{
		byte[] buf=new byte[1024];
		@Override
		public void run() {
			// TODO Auto-generated method stub
		while(ds!=null){
			DatagramPacket dp=new DatagramPacket(buf,buf.length);
			try {
				ds.receive(dp);
				System.out.println("a packet recieve from server");
				//开始解析
				parse(dp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
		private void parse(DatagramPacket dp) {
			// TODO Auto-generated method stub
			ByteArrayInputStream bais=new ByteArrayInputStream(buf,0,dp.getLength());
			DataInputStream dis=new DataInputStream(bais);
			//解析 
			TankNewMsg msg=new TankNewMsg();
			msg.parse(dis);
		}
}
}

package com.wang.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
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
	}
	public void send(TankNewMsg msg){
		msg.send(ds,"127.0.0.1",TankServer.UDP_PORT);
	}
}

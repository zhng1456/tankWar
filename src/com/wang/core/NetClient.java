package com.wang.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetClient {
	//UDP�˿ڵ���ʼ�˿�
	private static int UDP_PORT_START=2223;
	private int udpPort;
	TankClient tc;
	public NetClient(TankClient tc){
		this.tc=tc;
		udpPort=UDP_PORT_START++;
	}
	public void connect(String IP,int port){
		Socket s=null;
		try {
			s = new Socket(IP,port);
			//������ͻ��˵�UDP�˿ںŷ��͹�ȥ
			DataOutputStream dos=new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			//����Ψһ��id
			DataInputStream dis=new DataInputStream(s.getInputStream());
			int id=dis.readInt();
			System.out.println("Connected to Server and Server give me a ID:"+id);
			//����id��
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
	}
	
}

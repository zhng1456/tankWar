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
 * ��������
 * @author Administrator
 *
 */
public class TankServer {
	/**
	 * TCP�����˿�
	 */
	public static final int TCP_PORT=8888;
	public static final int UDP_PORT=6666;
	//�ͻ��˵ı�ʶ
	private static int ID=100;
	//һϵ���������Ŀͻ���
	List<Client> clients=new ArrayList<Client>();
	public void start(){
		//����UDP���߳�
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
				//���ܿͻ��˷�����udp�˿�
				int udpPort=dis.readInt();
				String ip=s.getInetAddress().getHostAddress();
				Client c=new Client(ip, udpPort);
				clients.add(c);
				//�����ͻ��ˣ��ͻ��˵�id
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
	 * �ڲ��࣬�������������Ŀͻ��˵Ļ�����Ϣ
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
		 * ���ܿͻ��˷��������ݣ���ת���������ͻ���
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
					//������ת���������ͻ���
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

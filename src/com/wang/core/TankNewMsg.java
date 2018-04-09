package com.wang.core;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class TankNewMsg {
	private Tank tank;

	public TankNewMsg(Tank tank) {
		super();
		this.tank = tank;
	}
	public TankNewMsg() {
		// TODO Auto-generated constructor stub
	}
	public void send(DatagramSocket ds,String ip,int udpPort) {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		DataOutputStream dos=new DataOutputStream(baos);
		//发送相关的信息
		try {
			dos.writeInt(tank.getId());
			dos.writeInt(tank.getX());
			dos.writeInt(tank.getY());
			dos.writeInt(tank.getDir().ordinal());
			dos.writeBoolean(tank.isGood());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] buf=baos.toByteArray();
		try {
			DatagramPacket dp=new DatagramPacket(buf,buf.length,new InetSocketAddress(ip,udpPort));
			ds.send(dp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void parse(DataInputStream dis) {
		// TODO Auto-generated method stub
		try {
			int id=dis.readInt();
			int x=dis.readInt();
			int y=dis.readInt();
			Dir dir=Dir.values()[dis.readInt()];
			boolean good=dis.readBoolean();
			System.out.println("id:"+id+"-x:"+x+"-y:"+y+"-dir:"+dir+"-good:"+good);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

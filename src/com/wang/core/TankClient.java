package com.wang.core;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TankClient extends Frame{
	public static final int GAME_WIDTH=800;
	public static final int GAME_HEIGHT=600;
	//̹��
	Tank myTank=new Tank(50,50,this);
	//�ӵ�
	Missile m=null;
	//���û�������˸������ 
	Image offScreenImage=null;
	
	@Override
	public void paint(Graphics g) {
		//��̹��
		myTank.draw(g);
		//���ӵ�
		if(m!=null)
		m.draw(g);
	}
	@Override
	public void update(Graphics g) {
		if(offScreenImage==null){
			offScreenImage=this.createImage(GAME_WIDTH,GAME_HEIGHT);
		}
		//ȡ��ͼƬ��Graphics����
		Graphics gOffScreen=offScreenImage.getGraphics();
		Color c=gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0,0,GAME_WIDTH,GAME_HEIGHT);
		//�ָ���ɫ
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage,0,0,null);
	}
	public void lauchFrame(){
		this.setLocation(400,300);
		this.setSize(GAME_WIDTH,GAME_HEIGHT);
		this.setTitle("tankWar");
		//���ü����¼�������������ʵ��
		this.addWindowListener(new WindowAdapter(){
			//��д�رմ��ڵ�ʱ��
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);//ֱ���˳� 
			}
		});
		//������ı䴰�ڴ�С
		this.setResizable(false);
		this.setBackground(Color.GREEN);
		//������̼���
		this.addKeyListener(new KeyMonitor());
		setVisible(true);
		//�����߳�
		new Thread(new PaintThread()).start();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TankClient tc = new TankClient();
		tc.lauchFrame();
	}
	//ÿ��һ��ʱ���ػ�һ�Σ���tank�ƶ�,���߳���ʵ��
	private class PaintThread implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//���õ����ⲿ��װ���repaint
			while(true){
				repaint();//repaint�ȵ���update���ٵ���paint
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	private class KeyMonitor extends KeyAdapter{
		//�ͷŰ���
		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			myTank.keyReleased(e);
		}
		//���̰���
		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			myTank.keyPressed(e);
		}
		
	}
}
	

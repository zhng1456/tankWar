package com.wang.core;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TankClient extends Frame{
	public static final int GAME_WIDTH=800;
	public static final int GAME_HEIGHT=600;
	//����
	int x=50;
	int y=50;
	//���û�������˸������ 
	Image offScreenImage=null;
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
	@Override
	public void paint(Graphics g) {
		//ȡ��ԭ�ȵ���ɫ
		Color c=g.getColor();
		g.setColor(Color.RED);
		//ָ������,������Բ
		g.fillOval(x,y,30,30);
		//�ָ�
		g.setColor(c);
		y+=5;
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
		setVisible(true);
		//�����߳�
		new Thread(new PaintThread()).start();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TankClient tc = new TankClient();
		tc.lauchFrame();
	}
}

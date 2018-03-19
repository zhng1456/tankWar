package com.wang.core;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TankClient extends Frame{
	//����
	int x=50;
	int y=50;
	//ÿ��һ��ʱ���ػ�һ�Σ���tank�ƶ�,���߳���ʵ��
	private class PaintThread implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//���õ����ⲿ��װ���repaint
			while(true){
				repaint();
				try {
					Thread.sleep(50);
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
	public void lauchFrame(){
		this.setLocation(400,300);
		this.setSize(800,600);
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

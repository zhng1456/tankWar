package com.wang.core;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TankClient extends Frame{
	public static final int GAME_WIDTH=800;
	public static final int GAME_HEIGHT=600;
	//�Լ���̹��
	Tank myTank=new Tank(50,50,true,Tank.Direction.STOP,this);
	//�з���̹��
	List<Tank> tanks=new ArrayList<Tank>();
	//�ӵ�
	//Missile m=null;
	List<Missile> missiles=new ArrayList<Missile>();
	//��ը
	List<Explode> explodes=new ArrayList<Explode>();
	//���û�������˸������ 
	Image offScreenImage=null;
	//ǽ
	Wall w1=new Wall(100,200,20,150,this);
	Wall w2=new Wall(300,100,300,20,this);
	//Ѫ��
	Blood b=new Blood();
	@Override
	public void paint(Graphics g) {
		//��ʾ�ӵ�����
		g.drawString("missiles count:"+missiles.size(),10,50);
		//��ʾ��ը���� 
		g.drawString("explodes count:"+explodes.size(),10,70);
		//��ʾ�з�̹������
		g.drawString("tanks count:"+tanks.size(),10,90);
		//��ʾ̹������ֵ
		g.drawString("tank life:"+myTank.getLife(),10,110);
		//�����������⣬�����¼������
		if(tanks.size()<=0){
			for(int i=0;i<5;i++){
				tanks.add(new Tank(50+40*(i+1),50,false,Tank.Direction.D,this));
			}
		}
		//��̹��
		myTank.draw(g);
		//��ǽ
		w1.draw(g);
		w2.draw(g);
		//��Ѫ��
		b.draw(g);
		//��Ѫ��
		myTank.eat(b);
		//���ط���̹��
		for(int i=0;i<tanks.size();i++){
			Tank t=tanks.get(i);
			//�ж��Ƿ���ǽ��ײ
			t.collideWithWall(w1);
			t.collideWithWall(w2);
			//�ж�̹����̹���Ƿ���ײ��
			t.collideWithTank(tanks);
			t.draw(g);
		}
		//����ը��Ч��
		for(int i=0;i<explodes.size();++i){
			Explode e=explodes.get(i);
			e.draw(g);
		}
		//���ӵ�
		for(int i=0;i<missiles.size();++i){
			Missile m=missiles.get(i);
			//�Ƿ����
			m.hitTank(myTank);
			m.hitTanks(tanks);
			//�ӵ���ǽ��ײ
			m.hitWall(w1);
			m.hitWall(w2);
			if(!m.isbLive()) missiles.remove(i);
			else
				m.draw(g);
		}
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
		//��ӵз���̹��
		for(int i=0;i<10;i++){
			tanks.add(new Tank(50+40*(i+1),50,false,Tank.Direction.D,this));
		}
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
	

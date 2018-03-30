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
	//自己的坦克
	Tank myTank=new Tank(50,50,true,Tank.Direction.STOP,this);
	//敌方的坦克
	List<Tank> tanks=new ArrayList<Tank>();
	//子弹
	//Missile m=null;
	List<Missile> missiles=new ArrayList<Missile>();
	//爆炸
	List<Explode> explodes=new ArrayList<Explode>();
	//利用缓冲解决闪烁的问题 
	Image offScreenImage=null;
	@Override
	public void paint(Graphics g) {
		//显示子弹数量
		g.drawString("missiles count:"+missiles.size(),10,50);
		//显示爆炸数量 
		g.drawString("explodes count:"+explodes.size(),10,70);
		//显示敌方坦克数量
		g.drawString("tanks count:"+tanks.size(),10,90);
		//画坦克
		myTank.draw(g);
		//画地方的坦克
		for(int i=0;i<tanks.size();i++){
			Tank t=tanks.get(i);
			t.draw(g);
		}
		//画爆炸的效果
		for(int i=0;i<explodes.size();++i){
			Explode e=explodes.get(i);
			e.draw(g);
		}
		//画子弹
		for(int i=0;i<missiles.size();++i){
			Missile m=missiles.get(i);
			//是否击中
			m.hitTank(myTank);
			m.hitTanks(tanks);
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
		//取到图片的Graphics对象
		Graphics gOffScreen=offScreenImage.getGraphics();
		Color c=gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0,0,GAME_WIDTH,GAME_HEIGHT);
		//恢复颜色
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage,0,0,null);
	}
	public void lauchFrame(){
		//添加敌方的坦克
		for(int i=0;i<10;i++){
			tanks.add(new Tank(50+40*(i+1),50,false,Tank.Direction.D,this));
		}
		this.setLocation(400,300);
		this.setSize(GAME_WIDTH,GAME_HEIGHT);
		this.setTitle("tankWar");
		//设置监听事件，用匿名类来实现
		this.addWindowListener(new WindowAdapter(){
			//重写关闭窗口的时间
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);//直接退出 
			}
		});
		//不允许改变窗口大小
		this.setResizable(false);
		this.setBackground(Color.GREEN);
		//加入键盘监听
		this.addKeyListener(new KeyMonitor());
		setVisible(true);
		//启动线程
		new Thread(new PaintThread()).start();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TankClient tc = new TankClient();
		tc.lauchFrame();
	}
	//每隔一段时间重画一次，让tank移动,用线程来实现
	private class PaintThread implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//调用的是外部包装类的repaint
			while(true){
				repaint();//repaint先调用update，再调用paint
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
		//释放按键
		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			myTank.keyReleased(e);
		}
		//键盘按键
		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			myTank.keyPressed(e);
		}
	}
}
	

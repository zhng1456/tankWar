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
	//坐标
	int x=50;
	int y=50;
	//利用缓冲解决闪烁的问题 
	Image offScreenImage=null;
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
	@Override
	public void paint(Graphics g) {
		//取到原先的颜色
		Color c=g.getColor();
		g.setColor(Color.RED);
		//指定矩形,画内切圆
		g.fillOval(x,y,30,30);
		//恢复
		g.setColor(c);
		y+=5;
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
		setVisible(true);
		//启动线程
		new Thread(new PaintThread()).start();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TankClient tc = new TankClient();
		tc.lauchFrame();
	}
}

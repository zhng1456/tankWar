package com.wang.core;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TankClient extends Frame{
	//坐标
	int x=50;
	int y=50;
	//每隔一段时间重画一次，让tank移动,用线程来实现
	private class PaintThread implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//调用的是外部包装类的repaint
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
		//取到原先的颜色
		Color c=g.getColor();
		g.setColor(Color.RED);
		//指定矩形,画内切圆
		g.fillOval(x,y,30,30);
		//恢复
		g.setColor(c);
		y+=5;
	}
	public void lauchFrame(){
		this.setLocation(400,300);
		this.setSize(800,600);
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

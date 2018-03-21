package com.wang.core;

import java.awt.Color;
import java.awt.Graphics;

public class Missile {
	//速度常量
	public static final int XSPEED=10;
	public static final int YSPEED=10;
	//子弹大小常量
	public static final int WIDTH=10;
	public static final int HEIGHT=10;
	//坐标
	private int x,y;
	private Tank.Direction dir;
	public Missile(int x, int y,Tank.Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	public void draw(Graphics g){
		Color c=g.getColor();
		g.setColor(Color.black);
		g.fillOval(x, y,WIDTH,HEIGHT);
		//恢复颜色 
		g.setColor(c);
		//移动
		move();
	}
	private void move() {
		// TODO Auto-generated method stub
		switch(dir){
		case L:
			x-=XSPEED;
			break;
		case LU://左上
			x-=XSPEED;
			y-=YSPEED;
			break;
		case U:
			y-=YSPEED;
			break;
		case RU://右上
			x+=XSPEED;
			y-=YSPEED;
			break;
		case R:
			x+=XSPEED;
			break;
		case D:
			y+=YSPEED;
			break;
		case LD://左下
			x-=XSPEED;
			y+=YSPEED;
			break;
		case RD://右下
			x+=XSPEED;
			y+=YSPEED;
			break;
		}
	}
}

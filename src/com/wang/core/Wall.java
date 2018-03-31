package com.wang.core;
import java.awt.Graphics;
import java.awt.Rectangle;
public class Wall {
	//左上角点的位置，宽和高
	private int x,y,w,h;
	TankClient tc;
	public Wall(int x, int y, int w, int h, TankClient tc) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tc = tc;
	}
	//画墙
	public void draw(Graphics g){
		g.fillRect(x, y,w,h);
	}
	//获取矩形，用于碰撞检测
	public Rectangle getRect(){
		return new Rectangle(x,y,w,h);
	}
}

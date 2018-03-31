package com.wang.core;
import java.awt.Graphics;
import java.awt.Rectangle;
public class Wall {
	//���Ͻǵ��λ�ã���͸�
	private int x,y,w,h;
	TankClient tc;
	public Wall(int x, int y, int w, int h, TankClient tc) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tc = tc;
	}
	//��ǽ
	public void draw(Graphics g){
		g.fillRect(x, y,w,h);
	}
	//��ȡ���Σ�������ײ���
	public Rectangle getRect(){
		return new Rectangle(x,y,w,h);
	}
}

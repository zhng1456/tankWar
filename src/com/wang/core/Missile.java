package com.wang.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Missile {
	//�ٶȳ���
	public static final int XSPEED=10;
	public static final int YSPEED=10;
	//�ӵ���С����
	public static final int WIDTH=10;
	public static final int HEIGHT=10;
	//����
	private int x,y;
	//�ڵ�����
	private Tank.Direction dir;
	//�ڵ�������
	private boolean bLive=true;
	//tankClient
	private TankClient tc;
	public boolean isbLive() {
		return bLive;
	}
	public void setbLive(boolean bLive) {
		this.bLive = bLive;
	}
	public Missile(int x, int y,Tank.Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	public Missile(int x, int y,Tank.Direction dir,TankClient tc){
		this(x,y,dir);
		this.tc=tc;
	}
	public void draw(Graphics g){
		if(!bLive){
			tc.missiles.remove(this);
			return;
		}
		Color c=g.getColor();
		g.setColor(Color.black);
		g.fillOval(x, y,WIDTH,HEIGHT);
		//�ָ���ɫ 
		g.setColor(c);
		//�ƶ�
		move();
	}
	private void move() {
		// TODO Auto-generated method stub
		switch(dir){
		case L:
			x-=XSPEED;
			break;
		case LU://����
			x-=XSPEED;
			y-=YSPEED;
			break;
		case U:
			y-=YSPEED;
			break;
		case RU://����
			x+=XSPEED;
			y-=YSPEED;
			break;
		case R:
			x+=XSPEED;
			break;
		case D:
			y+=YSPEED;
			break;
		case LD://����
			x-=XSPEED;
			y+=YSPEED;
			break;
		case RD://����
			x+=XSPEED;
			y+=YSPEED;
			break;
		}
		//�ж��Ƿ����
		if(x<0||y<0||x>TankClient.GAME_WIDTH||y>TankClient.GAME_HEIGHT){
			bLive=false;
		}
	}
	//����ӵ�����ľ���
	public Rectangle getRect(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	public boolean hitTank(Tank t){
		//�ж�2�������Ƿ��ཻ
		if(this.getRect().intersects(t.getRect())&&t.isLive()){
			//��̹��״̬����Ϊ����
			t.setLive(false);
			//�ӵ�Ҳ���� 
			this.bLive=false;
			return true;
		}
		return false;
	}
}

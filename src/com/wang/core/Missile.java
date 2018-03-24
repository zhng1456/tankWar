package com.wang.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Missile {
	//速度常量
	public static final int XSPEED=10;
	public static final int YSPEED=10;
	//子弹大小常量
	public static final int WIDTH=10;
	public static final int HEIGHT=10;
	//坐标
	private int x,y;
	//炮弹方向
	private Tank.Direction dir;
	//炮弹的生命
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
		//判断是否出界
		if(x<0||y<0||x>TankClient.GAME_WIDTH||y>TankClient.GAME_HEIGHT){
			bLive=false;
		}
	}
	//获得子弹外面的矩形
	public Rectangle getRect(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	public boolean hitTank(Tank t){
		//判断2个矩形是否相交
		if(this.getRect().intersects(t.getRect())&&t.isLive()){
			//将坦克状态设置为死亡
			t.setLive(false);
			//子弹也死亡 
			this.bLive=false;
			return true;
		}
		return false;
	}
}

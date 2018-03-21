package com.wang.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Tank {
	//速度常量
	public static final int XSPEED=5;
	public static final int YSPEED=5;
	//坦克大小常量
	public static final int WIDTH=30;
	public static final int HEIGHT=30;
	private int x,y;
	private boolean bL=false,bU=false,bR=false,bD=false;
	//枚举类型，坦克的方向，8个方向+停止状态
	enum Direction {L,LU,U,RU,D,LD,R,RD,STOP};
	private Direction dir=Direction.STOP;
	//炮筒的方向
	private Direction ptDir=Direction.D;
	TankClient tc;
	public Tank(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Tank(int x, int y,TankClient tc){
		this.x = x;
		this.y = y;
		this.tc=tc;
	}
	//画出坦克
	public void draw(Graphics g){
		//取到原先的颜色
		Color c=g.getColor();
		g.setColor(Color.RED);
		//指定矩形,画内切圆
		g.fillOval(x,y,WIDTH,HEIGHT);
		//恢复
		g.setColor(c);
		//根据炮筒方向，画线段
		switch(ptDir){
		case L:
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x,y+Tank.HEIGHT/2);
			break;
		case LU://左上
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x,y);
			break;
		case U:
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x+Tank.WIDTH/2,y);
			break;
		case RU://右上
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x+Tank.WIDTH,y);
			break;
		case R:
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x+Tank.WIDTH,y+Tank.HEIGHT/2);
			break;
		case D:
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x+Tank.WIDTH/2,y+Tank.HEIGHT);
			break;
		case LD://左下
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x,y+Tank.HEIGHT);
			break;
		case RD://右下
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x+Tank.WIDTH,y+Tank.HEIGHT);
			break;
		}
		move();
	}
	public void move(){
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
		case STOP:
			break;
		}
		//调整炮筒方向 
		if(this.dir!=Direction.STOP){
			this.ptDir=this.dir;
		}
	}
	//移动坦克
	//按下键盘后改变状态标志，move中根据标志改变坐标
	public void keyPressed(KeyEvent e){
		//根据键位执行不同的操作
		int key = e.getKeyCode();
		//改变坐标，内部类中，可直接访问外部类的属性
		switch(key){
		//按下ctrl键
		case KeyEvent.VK_CONTROL:
			tc.m=fire();
			break;
		case KeyEvent.VK_UP:
			bU=true;
			break;
		case KeyEvent.VK_DOWN:
			bD=true;
			break;
		case KeyEvent.VK_LEFT:
			bL=true;
			break;
		case KeyEvent.VK_RIGHT:
			bR=true;
			break;
		}
		locateDirection();
	}
	void locateDirection(){
		if(bL && !bU && !bR && !bD) dir=Direction.L;//左
		else if(!bL && bU && !bR && !bD) dir=Direction.U;//上
		else if(!bL && !bU && bR && !bD) dir=Direction.R;//右
		else if(!bL && !bU && !bR && bD) dir=Direction.D;//下
		else if(bL && bU && !bR && !bD) dir=Direction.LU;//左上
		else if(!bL && bU && bR && !bD) dir=Direction.RU;//右上
		else if(bL && !bU && !bR && bD) dir=Direction.LD;//左下
		else if(!bL && !bU && bR && bD) dir=Direction.RD;//右下
		else if(!bL && !bU && !bR && !bD) dir=Direction.STOP;
	}
	//释放按键
	public void keyReleased(KeyEvent e){
		//根据键位执行不同的操作
		int key = e.getKeyCode();
		//改变坐标，内部类中，可直接访问外部类的属性
		switch(key){
		case KeyEvent.VK_UP:
			bU=false;
			break;
		case KeyEvent.VK_DOWN:
			bD=false;
			break;
		case KeyEvent.VK_LEFT:
			bL=false;
			break;
		case KeyEvent.VK_RIGHT:
			bR=false;
			break;
		}
		locateDirection();
	}
	//开火
	public Missile fire(){
		//根据坦克的坐标，方向产生子弹
		//根据坦克左上角坐标，算子弹坐标，使子弹从正中间发射
		int x=this.x+Tank.WIDTH/2-Missile.WIDTH/2;
		int y=this.y+Tank.HEIGHT/2-Missile.HEIGHT/2;
		Missile m= new Missile(x,y,ptDir);
		return m;
	}
}

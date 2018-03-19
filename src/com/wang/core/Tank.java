package com.wang.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Tank {
	//速度常量
	public static final int XSPEED=5;
	public static final int YSPEED=5;
	private int x,y;
	private boolean bL=false,bU=false,bR=false,bD=false;
	//枚举类型，8个方向+停止状态
	enum Direction {L,LU,U,RU,D,LD,R,RD,STOP};
	private Direction dir=Direction.STOP;
	public Tank(int x, int y) {
		this.x = x;
		this.y = y;
	}
	//画出坦克
	public void draw(Graphics g){
		//取到原先的颜色
		Color c=g.getColor();
		g.setColor(Color.RED);
		//指定矩形,画内切圆
		g.fillOval(x,y,30,30);
		//恢复
		g.setColor(c);
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
	}
	//移动坦克
	//按下键盘后改变状态标志，move中根据标志改变坐标
	public void keyPressed(KeyEvent e){
		//根据键位执行不同的操作
		int key = e.getKeyCode();
		//改变坐标，内部类中，可直接访问外部类的属性
		switch(key){
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
}

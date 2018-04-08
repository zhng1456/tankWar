package com.wang.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Random;

import sun.awt.image.PixelConverter.Bgrx;

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
	private Dir dir=Dir.STOP;
	//炮筒的方向
	private Dir ptDir=Dir.D;
	//用于区分敌我
	private boolean good;
	//坦克的生死
	private boolean live=true;
	//随机数产生
	private static Random r= new Random();
	//步数，用于敌方坦克自动改变方向
	private int step=r.nextInt(12)+3;
	//坦克的id号
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	TankClient tc;
	public Tank(int x, int y,boolean good) {
		this.x = x;
		this.y = y;
		this.good=good;
	}
	public Tank(int x, int y,boolean good,Dir dir,TankClient tc){
		this(x,y,good);
		this.dir=dir;
		this.tc=tc;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public Dir getDir() {
		return dir;
	}
	public void setDir(Dir dir) {
		this.dir = dir;
	}
	public boolean isGood() {
		return good;
	}
	//画出坦克
	public void draw(Graphics g){
		//死亡则直接return
		if(!live){
			if(!good){
				tc.tanks.remove(this);
			}
			return;
		}
		//取到原先的颜色
		Color c=g.getColor();
		//敌方，我方用不同的颜色
		if(good)g.setColor(Color.RED);
		else g.setColor(Color.BLUE);
		//指定矩形,画内切圆
		g.fillOval(x,y,WIDTH,HEIGHT);
		//显示tank的id
		g.drawString("id:"+id,x,y-10);
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
		if(this.dir!=Dir.STOP){
			this.ptDir=this.dir;
		}
		//防止坦克越界
		if(x<0) x=0;
		//最上方的标题栏也会占一定的位置
		if(y<30) y=30;
		if(x+Tank.WIDTH>TankClient.GAME_WIDTH) x=TankClient.GAME_WIDTH-Tank.WIDTH;
		if(y+Tank.HEIGHT>TankClient.GAME_HEIGHT) y=TankClient.GAME_HEIGHT-Tank.HEIGHT;
		//地方坦克通过随机数，自动改变方向
		if(!good){
			//将枚举类型转换为一个数组
			Dir[] dirs=Dir.values();
			if(step==0){
				step=r.nextInt(12)+3;
				int rn=r.nextInt(dirs.length);
				dir=dirs[rn];
			}
			step--;
			if(r.nextInt(40)>38){
			//敌方坦克发射子弹
			Missile m=this.fire();
			//装入子弹的集合中
			if(m!=null)
			tc.missiles.add(m);
			}
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
		if(bL && !bU && !bR && !bD) dir=Dir.L;//左
		else if(!bL && bU && !bR && !bD) dir=Dir.U;//上
		else if(!bL && !bU && bR && !bD) dir=Dir.R;//右
		else if(!bL && !bU && !bR && bD) dir=Dir.D;//下
		else if(bL && bU && !bR && !bD) dir=Dir.LU;//左上
		else if(!bL && bU && bR && !bD) dir=Dir.RU;//右上
		else if(bL && !bU && !bR && bD) dir=Dir.LD;//左下
		else if(!bL && !bU && bR && bD) dir=Dir.RD;//右下
		else if(!bL && !bU && !bR && !bD) dir=Dir.STOP;
	}
	//释放按键
	public void keyReleased(KeyEvent e){
		//根据键位执行不同的操作
		int key = e.getKeyCode();
		//改变坐标，内部类中，可直接访问外部类的属性
		switch(key){
		//抬起ctrl键则发射炮弹
		case KeyEvent.VK_CONTROL:
			Missile m=fire();
			if(m!=null)
			tc.missiles.add(m);
			break;
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
		if(!live) return null;
		//根据坦克的坐标，方向产生子弹
		//根据坦克左上角坐标，算子弹坐标，使子弹从正中间发射
		int x=this.x+Tank.WIDTH/2-Missile.WIDTH/2;
		int y=this.y+Tank.HEIGHT/2-Missile.HEIGHT/2;
		Missile m= new Missile(x,y,good,ptDir,this.tc);
		return m;
	}
	//获得坦克外面的矩形
	public Rectangle getRect(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
}

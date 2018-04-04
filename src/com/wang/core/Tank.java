package com.wang.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

public class Tank {
	//速度常量
	public static final int XSPEED=5;
	public static final int YSPEED=5;
	//坦克大小常量
	public static final int WIDTH=30;
	public static final int HEIGHT=30;
	//坐标
	private int x,y;
	//上一步的坐标
	private int oldX,oldY;
	private boolean bL=false,bU=false,bR=false,bD=false;
	//枚举类型，坦克的方向，8个方向+停止状态
	enum Direction {L,LU,U,RU,D,LD,R,RD,STOP};
	private Direction dir=Direction.STOP;
	//生命值
	private int life=100;
	//炮筒的方向
	private Direction ptDir=Direction.D;
	//用于区分敌我
	private boolean good;
	//坦克的生死
	private boolean live=true;
	//随机数产生
	private static Random r= new Random();
	//步数，用于敌方坦克自动改变方向
	private int step=r.nextInt(12)+3;
	//血条
	private BloodBar bb=new BloodBar();
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}

	TankClient tc;
	public Tank(int x, int y,boolean good) {
		this.x = x;
		this.y = y;
		this.oldX=x;
		this.oldY=y;
		this.good=good;
	}
	public Tank(int x, int y,boolean good,Direction dir,TankClient tc){
		this(x,y,good);
		this.dir=dir;
		this.tc=tc;
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
		//恢复
		g.setColor(c);
		//主战坦克画出血条
		if(good)
		bb.draw(g);
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
		//记录x，y，作为上一步的x,y
		this.oldX=x;
		this.oldY=y;
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
		//防止坦克越界
		if(x<0) x=0;
		//最上方的标题栏也会占一定的位置
		if(y<30) y=30;
		if(x+Tank.WIDTH>TankClient.GAME_WIDTH) x=TankClient.GAME_WIDTH-Tank.WIDTH;
		if(y+Tank.HEIGHT>TankClient.GAME_HEIGHT) y=TankClient.GAME_HEIGHT-Tank.HEIGHT;
		//地方坦克通过随机数，自动改变方向
		if(!good){
			//将枚举类型转换为一个数组
			Direction[] dirs=Direction.values();
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
		case KeyEvent.VK_A:
			superFire();
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
	//朝某个方向开火
	public Missile fire(Direction dir){
		if(!live) return null;
		//根据坦克的坐标，方向产生子弹
		//根据坦克左上角坐标，算子弹坐标，使子弹从正中间发射
		int x=this.x+Tank.WIDTH/2-Missile.WIDTH/2;
		int y=this.y+Tank.HEIGHT/2-Missile.HEIGHT/2;
		Missile m= new Missile(x,y,good,dir,this.tc);
		return m;
	}
	//超级炮弹，按下后朝8个方向开火
	private void superFire(){
		Direction[] dirs=Direction.values();
		for(int i=0;i<8;i++){
			tc.missiles.add(fire(dirs[i]));
		}
	}
	//获得坦克外面的矩形
	public Rectangle getRect(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	//与墙碰撞
	public boolean collideWithWall(Wall w){
		if(this.live&&this.getRect().intersects(w.getRect())){
			stay();//返回上一次的位置
			return true;
		}
		return false;
	}
	private void stay(){
		x=oldX;
		y=oldY;
	}
	//增加坦克与坦克间的碰撞检测，坦克无法越过坦克
	public boolean collideWithTank(List<Tank> tanks){
		for(int i=0;i<tanks.size();++i){
			Tank t=tanks.get(i);
			if(this!=t){//不是同一辆坦克
				if(this.live&&t.isLive()&&this.getRect().intersects(t.getRect())){
					this.stay();//返回上一次的位置
					t.stay();
					return true;
				}
			}
		}
		return false;
	}
	//内部类，坦克的血条
	private class BloodBar{
		public void draw(Graphics g){
			Color c=g.getColor();
			//画空心的方块
			g.setColor(Color.RED);
			g.drawRect(x,y-10,WIDTH,10);
			//画实心的方块
			int w=WIDTH*life/100;
			g.fillRect(x,y-10,w,10);
			g.setColor(c);
		}
	}
}

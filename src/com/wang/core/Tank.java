package com.wang.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

public class Tank {
	//�ٶȳ���
	public static final int XSPEED=5;
	public static final int YSPEED=5;
	//̹�˴�С����
	public static final int WIDTH=30;
	public static final int HEIGHT=30;
	//����
	private int x,y;
	//��һ��������
	private int oldX,oldY;
	private boolean bL=false,bU=false,bR=false,bD=false;
	//ö�����ͣ�̹�˵ķ���8������+ֹͣ״̬
	enum Direction {L,LU,U,RU,D,LD,R,RD,STOP};
	private Direction dir=Direction.STOP;
	//����ֵ
	private int life=100;
	//��Ͳ�ķ���
	private Direction ptDir=Direction.D;
	//�������ֵ���
	private boolean good;
	//̹�˵�����
	private boolean live=true;
	//���������
	private static Random r= new Random();
	//���������ڵз�̹���Զ��ı䷽��
	private int step=r.nextInt(12)+3;
	//Ѫ��
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
	//����̹��
	public void draw(Graphics g){
		//������ֱ��return
		if(!live){
			if(!good){
				tc.tanks.remove(this);
			}
			return;
		}
		//ȡ��ԭ�ȵ���ɫ
		Color c=g.getColor();
		//�з����ҷ��ò�ͬ����ɫ
		if(good)g.setColor(Color.RED);
		else g.setColor(Color.BLUE);
		//ָ������,������Բ
		g.fillOval(x,y,WIDTH,HEIGHT);
		//�ָ�
		g.setColor(c);
		//��ս̹�˻���Ѫ��
		if(good)
		bb.draw(g);
		//������Ͳ���򣬻��߶�
		switch(ptDir){
		case L:
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x,y+Tank.HEIGHT/2);
			break;
		case LU://����
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x,y);
			break;
		case U:
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x+Tank.WIDTH/2,y);
			break;
		case RU://����
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x+Tank.WIDTH,y);
			break;
		case R:
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x+Tank.WIDTH,y+Tank.HEIGHT/2);
			break;
		case D:
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x+Tank.WIDTH/2,y+Tank.HEIGHT);
			break;
		case LD://����
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x,y+Tank.HEIGHT);
			break;
		case RD://����
			g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x+Tank.WIDTH,y+Tank.HEIGHT);
			break;
		}
		move();
	}
	public void move(){
		//��¼x��y����Ϊ��һ����x,y
		this.oldX=x;
		this.oldY=y;
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
		case STOP:
			break;
		}
		//������Ͳ���� 
		if(this.dir!=Direction.STOP){
			this.ptDir=this.dir;
		}
		//��ֹ̹��Խ��
		if(x<0) x=0;
		//���Ϸ��ı�����Ҳ��ռһ����λ��
		if(y<30) y=30;
		if(x+Tank.WIDTH>TankClient.GAME_WIDTH) x=TankClient.GAME_WIDTH-Tank.WIDTH;
		if(y+Tank.HEIGHT>TankClient.GAME_HEIGHT) y=TankClient.GAME_HEIGHT-Tank.HEIGHT;
		//�ط�̹��ͨ����������Զ��ı䷽��
		if(!good){
			//��ö������ת��Ϊһ������
			Direction[] dirs=Direction.values();
			if(step==0){
				step=r.nextInt(12)+3;
				int rn=r.nextInt(dirs.length);
				dir=dirs[rn];
			}
			step--;
			if(r.nextInt(40)>38){
			//�з�̹�˷����ӵ�
			Missile m=this.fire();
			//װ���ӵ��ļ�����
			if(m!=null)
			tc.missiles.add(m);
			}
		}
		
	}
	//�ƶ�̹��
	//���¼��̺�ı�״̬��־��move�и��ݱ�־�ı�����
	public void keyPressed(KeyEvent e){
		//���ݼ�λִ�в�ͬ�Ĳ���
		int key = e.getKeyCode();
		//�ı����꣬�ڲ����У���ֱ�ӷ����ⲿ�������
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
		if(bL && !bU && !bR && !bD) dir=Direction.L;//��
		else if(!bL && bU && !bR && !bD) dir=Direction.U;//��
		else if(!bL && !bU && bR && !bD) dir=Direction.R;//��
		else if(!bL && !bU && !bR && bD) dir=Direction.D;//��
		else if(bL && bU && !bR && !bD) dir=Direction.LU;//����
		else if(!bL && bU && bR && !bD) dir=Direction.RU;//����
		else if(bL && !bU && !bR && bD) dir=Direction.LD;//����
		else if(!bL && !bU && bR && bD) dir=Direction.RD;//����
		else if(!bL && !bU && !bR && !bD) dir=Direction.STOP;
	}
	//�ͷŰ���
	public void keyReleased(KeyEvent e){
		//���ݼ�λִ�в�ͬ�Ĳ���
		int key = e.getKeyCode();
		//�ı����꣬�ڲ����У���ֱ�ӷ����ⲿ�������
		switch(key){
		//̧��ctrl�������ڵ�
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
	//����
	public Missile fire(){
		if(!live) return null;
		//����̹�˵����꣬��������ӵ�
		//����̹�����Ͻ����꣬���ӵ����꣬ʹ�ӵ������м䷢��
		int x=this.x+Tank.WIDTH/2-Missile.WIDTH/2;
		int y=this.y+Tank.HEIGHT/2-Missile.HEIGHT/2;
		Missile m= new Missile(x,y,good,ptDir,this.tc);
		return m;
	}
	//��ĳ�����򿪻�
	public Missile fire(Direction dir){
		if(!live) return null;
		//����̹�˵����꣬��������ӵ�
		//����̹�����Ͻ����꣬���ӵ����꣬ʹ�ӵ������м䷢��
		int x=this.x+Tank.WIDTH/2-Missile.WIDTH/2;
		int y=this.y+Tank.HEIGHT/2-Missile.HEIGHT/2;
		Missile m= new Missile(x,y,good,dir,this.tc);
		return m;
	}
	//�����ڵ������º�8�����򿪻�
	private void superFire(){
		Direction[] dirs=Direction.values();
		for(int i=0;i<8;i++){
			tc.missiles.add(fire(dirs[i]));
		}
	}
	//���̹������ľ���
	public Rectangle getRect(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	//��ǽ��ײ
	public boolean collideWithWall(Wall w){
		if(this.live&&this.getRect().intersects(w.getRect())){
			stay();//������һ�ε�λ��
			return true;
		}
		return false;
	}
	private void stay(){
		x=oldX;
		y=oldY;
	}
	//����̹����̹�˼����ײ��⣬̹���޷�Խ��̹��
	public boolean collideWithTank(List<Tank> tanks){
		for(int i=0;i<tanks.size();++i){
			Tank t=tanks.get(i);
			if(this!=t){//����ͬһ��̹��
				if(this.live&&t.isLive()&&this.getRect().intersects(t.getRect())){
					this.stay();//������һ�ε�λ��
					t.stay();
					return true;
				}
			}
		}
		return false;
	}
	//�ڲ��̹࣬�˵�Ѫ��
	private class BloodBar{
		public void draw(Graphics g){
			Color c=g.getColor();
			//�����ĵķ���
			g.setColor(Color.RED);
			g.drawRect(x,y-10,WIDTH,10);
			//��ʵ�ĵķ���
			int w=WIDTH*life/100;
			g.fillRect(x,y-10,w,10);
			g.setColor(c);
		}
	}
}

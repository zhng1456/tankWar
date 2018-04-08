package com.wang.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Random;

import sun.awt.image.PixelConverter.Bgrx;

public class Tank {
	//�ٶȳ���
	public static final int XSPEED=5;
	public static final int YSPEED=5;
	//̹�˴�С����
	public static final int WIDTH=30;
	public static final int HEIGHT=30;
	private int x,y;
	private boolean bL=false,bU=false,bR=false,bD=false;
	//ö�����ͣ�̹�˵ķ���8������+ֹͣ״̬
	private Dir dir=Dir.STOP;
	//��Ͳ�ķ���
	private Dir ptDir=Dir.D;
	//�������ֵ���
	private boolean good;
	//̹�˵�����
	private boolean live=true;
	//���������
	private static Random r= new Random();
	//���������ڵз�̹���Զ��ı䷽��
	private int step=r.nextInt(12)+3;
	//̹�˵�id��
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
		//��ʾtank��id
		g.drawString("id:"+id,x,y-10);
		//�ָ�
		g.setColor(c);
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
		if(this.dir!=Dir.STOP){
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
			Dir[] dirs=Dir.values();
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
		if(bL && !bU && !bR && !bD) dir=Dir.L;//��
		else if(!bL && bU && !bR && !bD) dir=Dir.U;//��
		else if(!bL && !bU && bR && !bD) dir=Dir.R;//��
		else if(!bL && !bU && !bR && bD) dir=Dir.D;//��
		else if(bL && bU && !bR && !bD) dir=Dir.LU;//����
		else if(!bL && bU && bR && !bD) dir=Dir.RU;//����
		else if(bL && !bU && !bR && bD) dir=Dir.LD;//����
		else if(!bL && !bU && bR && bD) dir=Dir.RD;//����
		else if(!bL && !bU && !bR && !bD) dir=Dir.STOP;
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
	//���̹������ľ���
	public Rectangle getRect(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
}

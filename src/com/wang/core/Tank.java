package com.wang.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

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
	enum Direction {L,LU,U,RU,D,LD,R,RD,STOP};
	private Direction dir=Direction.STOP;
	//��Ͳ�ķ���
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
	//����̹��
	public void draw(Graphics g){
		//ȡ��ԭ�ȵ���ɫ
		Color c=g.getColor();
		g.setColor(Color.RED);
		//ָ������,������Բ
		g.fillOval(x,y,WIDTH,HEIGHT);
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
		if(this.dir!=Direction.STOP){
			this.ptDir=this.dir;
		}
	}
	//�ƶ�̹��
	//���¼��̺�ı�״̬��־��move�и��ݱ�־�ı�����
	public void keyPressed(KeyEvent e){
		//���ݼ�λִ�в�ͬ�Ĳ���
		int key = e.getKeyCode();
		//�ı����꣬�ڲ����У���ֱ�ӷ����ⲿ�������
		switch(key){
		//����ctrl��
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
		//����̹�˵����꣬��������ӵ�
		//����̹�����Ͻ����꣬���ӵ����꣬ʹ�ӵ������м䷢��
		int x=this.x+Tank.WIDTH/2-Missile.WIDTH/2;
		int y=this.y+Tank.HEIGHT/2-Missile.HEIGHT/2;
		Missile m= new Missile(x,y,ptDir);
		return m;
	}
}

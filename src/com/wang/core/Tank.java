package com.wang.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Tank {
	//�ٶȳ���
	public static final int XSPEED=5;
	public static final int YSPEED=5;
	private int x,y;
	private boolean bL=false,bU=false,bR=false,bD=false;
	//ö�����ͣ�8������+ֹͣ״̬
	enum Direction {L,LU,U,RU,D,LD,R,RD,STOP};
	private Direction dir=Direction.STOP;
	public Tank(int x, int y) {
		this.x = x;
		this.y = y;
	}
	//����̹��
	public void draw(Graphics g){
		//ȡ��ԭ�ȵ���ɫ
		Color c=g.getColor();
		g.setColor(Color.RED);
		//ָ������,������Բ
		g.fillOval(x,y,30,30);
		//�ָ�
		g.setColor(c);
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

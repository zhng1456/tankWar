package com.wang.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

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
	//��ʾ�ҷ������ǵз����ӵ������ݷ�����̹��������
	//�ҷ�̹�˵��ӵ��������ҷ����з�̹�˵��ӵ����������з�
	private boolean good;
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
	public Missile(int x, int y,boolean good,Tank.Direction dir,TankClient tc){
		this(x,y,dir);
		this.good=good;
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
		//����Ҫ���ֵ���˫�����ӵ����з��ӵ��޷��Եз������˺�
		if(this.bLive&&t.isLive()&&this.getRect().intersects(t.getRect())&&t.isLive()&&this.good!=t.isGood()){
			//��̹��״̬����Ϊ����
			t.setLive(false);
			//�ӵ�Ҳ���� 
			this.bLive=false;
			//������ը
			Explode e=new Explode(x, y, tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}
	public boolean hitTanks(List<Tank> tanks){
		for(int i=0;i<tanks.size();i++){
			if(hitTank(tanks.get(i))){
				return true;
			}
		}
		return false;
	}
	//�ж��ӵ���ǽ�Ƿ���ײ
	public boolean hitWall(Wall w){
		if(this.bLive&&this.getRect().intersects(w.getRect())){
			this.bLive=false;
			return true;
		}
		return false;
	}
}

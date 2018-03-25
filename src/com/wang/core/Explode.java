package com.wang.core;

import java.awt.Color;
import java.awt.Graphics;

public class Explode {
	private int x,y;
	private boolean live=true;
	private TankClient tc;
	//圆的直径
	int[] diameter={4,7,12,18,26,32,49,30,14,6};
	int step=0;
	
	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

	public void draw(Graphics g){
		if(!live) {
			//消除爆炸，与消除子弹类似
			tc.explodes.remove(this);
			return;
		}
		if(step==diameter.length){//爆炸效果已经显示完了
			live=false;
			step=0;
			return;
		}
		Color c=g.getColor();
		g.setColor(Color.ORANGE);
		g.fillOval(x,y,diameter[step],diameter[step]);
		step++;
		g.setColor(c);
	}
}

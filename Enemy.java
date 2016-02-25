import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

public class Enemy implements Runnable {
	Rectangle player;
	public Enemy(){
		player = new Rectangle(50,50,15,15);
	}
	private Image dbImage;
	private Graphics dbg;
	
	int xDirection, yDirection;
	
	public void setXDirection(int xdir){
		xDirection = xdir;
	}
	
	public void setYDirection(int ydir){
		yDirection = ydir;
	}
	
	public void move(){
		player.x += xDirection;
		player.y += yDirection;
	}
	
	public void draw(Graphics g){
		g.setColor(Color.PINK);
		g.fillRect(player.x,player.y,player.width,player.height);
	}
	
	@Override
	public void run() {
		try{
			while(true){
				move();
				Thread.sleep(5);
			}
		}
		catch(Exception e){
			System.err.println(e.getMessage());
		}
	}
	
	
}

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import java.util.Random;


public class Main extends JFrame {
	
	//double buffering
	Image dbImage;
	Graphics dbg;
	
	//Ant
	static Colony c = new Colony();
	
	int GWIDTH = 700,GHEIGHT = 600;
	Dimension screenSize = new Dimension(GWIDTH,GHEIGHT);	
	public Main(){
		this.setTitle("VirtualAntColony");
		this.setSize(screenSize);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setBackground(Color.WHITE);
		addKeyListener(new AL());
	}
	
	
	public static void main(String[] args) {
		Main m = new Main();
		//create and start thread
		Thread Colony = new Thread(c);
		Colony.start();
		Thread Ant1 = new Thread(c.ants[0]);
		Ant1.start();
		Thread Ant2 = new Thread(c.ants[1]);
		Ant2.start();
		//Thread Ant3 = new Thread(c.ants[2]);
		//Ant3.start();
		/*Thread Ant4 = new Thread(c.ants[3]);
		Ant4.start();
		Thread Ant5 = new Thread(c.ants[4]);
		Ant5.start();
		Thread Ant6 = new Thread(c.ants[5]);
		Ant6.start();
		Thread Ant7 = new Thread(c.ants[6]);
		Ant7.start();
		Thread Ant8 = new Thread(c.ants[7]);
		Ant8.start();
		Thread Ant9 = new Thread(c.ants[8]);
		Ant9.start();
		Thread Ant10 = new Thread(c.ants[9]);
		Ant10.start();*/
		
		
	}
	
	public void initialiseThreads(){
		
	}

	public void paint(Graphics g){
		dbImage = createImage(getWidth(), getHeight());
		dbg = dbImage.getGraphics();
		draw(dbg);
		g.drawImage(dbImage, 0, 0, this);
	}
	public void draw(Graphics g){
		for(int i = 0; i < c.ants.length; i++){
			c.ants[i].draw(g);
		}
		c.enemy.draw(g);
		//b.p1.draw(g);
		g.setColor(Color.BLACK);
		repaint();
	}
	
	public class AL extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e){
			int keyCode = e.getKeyCode();
			if(keyCode == e.VK_LEFT){
				c.enemy.setXDirection(-1);
			}
			if(keyCode == e.VK_RIGHT){
				c.enemy.setXDirection(1);
			}
			if(keyCode == e.VK_UP){
				c.enemy.setYDirection(-1);
			}
			if(keyCode == e.VK_DOWN){
				c.enemy.setYDirection(1);
			}
		}
		public void keyReleased(KeyEvent e){
			int keyCode = e.getKeyCode();
			if(keyCode == e.VK_LEFT){
				c.enemy.setXDirection(0);
			}
			if(keyCode == e.VK_RIGHT){
				c.enemy.setXDirection(0);
			}
			if(keyCode == e.VK_UP){
				c.enemy.setYDirection(0);
			}
			if(keyCode == e.VK_DOWN){
				c.enemy.setYDirection(0);
			}
		}
	}
}

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.JFrame;


public class Main extends JFrame {
	
	//double buffering
	Image dbImage;
	Graphics dbg;
	
	//Ant
	static Ant b = new Ant(243, 193);
	int GWIDTH = 700,GHEIGHT = 600;
	
	Dimension screenSize = new Dimension(GWIDTH,GHEIGHT);
	
	public Main(){
		this.setTitle("VirtualAntColony");
		this.setSize(screenSize);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setBackground(Color.WHITE);
	}
	
	
	public static void main(String[] args) {
		Main m = new Main();
		
		//create and start thread
		Thread Ant = new Thread(b);
		Ant.start();
	}

	public void paint(Graphics g){
		dbImage = createImage(getWidth(), getHeight());
		dbg = dbImage.getGraphics();
		draw(dbg);
		g.drawImage(dbImage, 0, 0, this);
	}
	public void draw(Graphics g){
		b.draw(g);
		//b.p1.draw(g);
		g.setColor(Color.BLACK);
		repaint();
	}
}

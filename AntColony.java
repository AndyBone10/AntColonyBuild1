import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

//primary class for the game

public class AntColony extends Applet implements Runnable, KeyListener {
	//the main Thread becomes the game loop
	Thread gameloop;
	
	//use this as a backbuffer
	BufferedImage backbuffer;
	
	//the main drawing object for the back buffer
	Graphics2D g2d;
	
	//toggle for drawing bounding boxes
	boolean showBounds = false;
	
	//makes the ant rest
	int resting = 0;
	
	//the player's ship
	Ant ship = new Ant();
	
	//create the identity transform(0,0)
	AffineTransform identity = new AffineTransform();
	
	//create a random number generator
	Random rand = new Random();
	
	//applet init event
	public void init(){
		//create the back buffer for smooth graphics
		backbuffer = new BufferedImage(640,480, BufferedImage.TYPE_INT_RGB);
		g2d = backbuffer.createGraphics();
		
		//set up the ship
		ship.setX(320);
		ship.setY(240);
		
		//start the user input listener
		addKeyListener(this);
	}
	
	//applet update event to redraw the screen
	public void update(Graphics g){
		//start off transforms at identity
		g2d.setTransform(identity);
		
		//erase the background
		g2d.setPaint(Color.BLACK);
		g2d.fillRect(0,0,getSize().width,getSize().height);
		
		//print some status information
		g2d.setColor(Color.WHITE);
		g2d.drawString("Ship: " + Math.round(ship.getX()) + "," + Math.round(ship.getY()), 5, 10);
		g2d.drawString("Move angle: " + Math.round(ship.getMoveAngle()) + 90,5,25);
		g2d.drawString("Face angle: " + Math.round(ship.getFaceAngle()), 5, 40);
		
		//draw the game graphics
		drawShip();
		
		//repaint the applet window
		paint(g);
	}
	
	//drawShip called by applet update event
	public void drawShip(){
		g2d.setTransform(identity);
		g2d.translate(ship.getX(),ship.getY());
		g2d.rotate(Math.toRadians(ship.getFaceAngle()));
		g2d.setColor(Color.RED);
		g2d.fill(ship.getShape());
	}

	//applet window repaint event -- draw the back buffer
	public void paint(Graphics g){
		//draw the back buffer onto the applet window
		g.drawImage(backbuffer, 0, 0, this);
	}
	//thread start event - start the game loop running
	public void start(){
		//create the gameloop thread for real-time updates
		gameloop = new Thread(this);
		gameloop.start();
	}
	
	//thread run event(game loop)
	public void run(){
		//acquire the current thread
		Thread t = Thread.currentThread();
		
		//keep going as long as the thread is alive
		while(t == gameloop){
			try {
					//update the game loop
					gameUpdate();
					//target framerate is 50 fps
					Thread.sleep(10);
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
			repaint();
		}
	}
	//thread stop event
	public void stop(){
		//kill the gameloop thread
		gameloop = null;
	}
	//move and animate the objects in the game
	private void gameUpdate(){
		
		updateShip();
		
		
		//checkCollisions();
	}
	
	public void changeDirection(){
			int randomFaceAngle = rand.nextInt((360 - 0) + 1) + 0;
			ship.setFaceAngle(randomFaceAngle);
	}
	
	//Update the ship position based on velocity
	public void updateShip(){
		resting++;
		//in place of pressing up to move the ship
		ship.setMoveAngle(ship.getFaceAngle() - 90);
		ship.setVelX(calcAngleMoveX(ship.getMoveAngle()));
		ship.setVelY(calcAngleMoveY(ship.getMoveAngle()));
		
		//used in reversing the direction the ship is facing
		double faceAngle = ship.getFaceAngle();
		
		if(resting == 5){//how many steps before ant changes the way it is facing.
			resting = 0;
			changeDirection();
		}
		//update ship X's position, distance the ant moves in one step
		ship.incX(ship.getVelX() * 5);
		System.out.println(ship.getVelX());
		
		//collide with left/right edge
		if(ship.getX() < - 5){
			ship.setX(5);
			checkFaceAngle(faceAngle);	
			bounceOffEdge(faceAngle);
		}
		else if(ship.getX() > 640 + 5){
			ship.setX(640 - 5);
			checkFaceAngle(faceAngle);	
			bounceOffEdge(faceAngle);
		}		
		//update ship Y's position
		ship.incY(ship.getVelY() * 5);
		System.out.println(ship.getVelY());
		
		
		//wrap around top/bottom
		if(ship.getY() < -5){
			ship.setY(5);
			checkFaceAngle(faceAngle);	
			bounceOffEdge(faceAngle);
		}	
		else if(ship.getY() > 480 + 5){
			ship.setY(480 - 5);
			checkFaceAngle(faceAngle);	
			bounceOffEdge(faceAngle);
		}
	}
	//keeps face angle in range 0-360
	public void checkFaceAngle(double faceAngle){
		if(faceAngle > 180){
			faceAngle -= 180;
		}
		else{
			faceAngle += 180;
		}
	}
	
	public void bounceOffEdge(double faceAngle){
		ship.setFaceAngle(faceAngle);
		ship.setMoveAngle(ship.getFaceAngle() - 90);
		ship.setVelX(calcAngleMoveX(ship.getMoveAngle()) * 0.2);
		ship.setVelY(calcAngleMoveY(ship.getMoveAngle()) * 0.2);
	}
	//key listener events
	public void keyReleased(KeyEvent k){}
	public void keyTyped(KeyEvent k){}
	public void keyPressed(KeyEvent k){
		int keyCode = k.getKeyCode();
		switch(keyCode){
			case KeyEvent.VK_LEFT:
				//left arrow rotates ship left 5 degrees
				ship.incFaceAngle(-12);
				if(ship.getFaceAngle() < 0) ship.setFaceAngle(360 - 12);
				break;
			case KeyEvent.VK_RIGHT:
				//right arrow rotates ship 5 degrees
				ship.incFaceAngle(12);
				if(ship.getFaceAngle() > 360) ship.setFaceAngle(12);
				break;
			case KeyEvent.VK_UP:
				//up arrow adds thrust to ship(1/10 normal speed)
				ship.setMoveAngle(ship.getFaceAngle() - 90);
				ship.setVelX(calcAngleMoveX(ship.getMoveAngle()));
				ship.setVelY(calcAngleMoveY(ship.getMoveAngle()));
				break;
		}
	}
	
	
	
	//calculate X movement value based on direction angle
	public double calcAngleMoveX(double angle){
		return(double)(Math.cos(angle * Math.PI / 180));
	}
	
	//calculate Y movement value based on direction angle
	public double calcAngleMoveY(double angle) {
		return(double)(Math.sin(angle * Math.PI / 180));
	}
}
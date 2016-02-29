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
	int ANTS = 100;
	Ant [] ant = new Ant[ANTS];
	
	Enemy ship = new Enemy();
	
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
		for(int i = 0; i < ANTS; i++){
			ant[i] = new Ant();
			ant[i].setX(320);
			ant[i].setY(240);
			changeDirection(ant[i]);
		}
		
		ship.setX(60);
		ship.setY(60);
		
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
		//g2d.setColor(Color.WHITE);
		//g2d.drawString("Ship: " + Math.round(ant.getX()) + "," + Math.round(ant.getY()), 5, 10);
		//g2d.drawString("Move angle: " + Math.round(ant.getMoveAngle()) + 90,5,25);
		//g2d.drawString("Face angle: " + Math.round(ant.getFaceAngle()), 5, 40);
		
		//draw the game graphics
		drawShip();
		drawEnemy();
		
		//repaint the applet window
		paint(g);
	}
	
	//drawShip called by applet update event
	public void drawShip(){
		for(int i = 0; i < ANTS; i++){
			g2d.setTransform(identity);
			g2d.translate(ant[i].getX(),ant[i].getY());
			g2d.rotate(Math.toRadians(ant[i].getFaceAngle()));
			g2d.setColor(Color.RED);
			g2d.fill(ant[i].getShape());
		}
	}
	
	public void drawEnemy(){
		g2d.setTransform(identity);
		g2d.translate(ship.getX(),ship.getY());
		g2d.rotate(Math.toRadians(ship.getFaceAngle()));
		g2d.setColor(Color.PINK);
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
					Thread.sleep(100);
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
		
		updateAnt();
		updateEnemy();
		
		
		//checkCollisions();
	}
	
	public void changeDirection(Ant ant){
			int randomFaceAngle = rand.nextInt((360 - 0) + 1) + 0;
			ant.setFaceAngle(randomFaceAngle);
	}
	
	//Update the ship position based on velocity
	public void updateAnt(){
		resting++;
		for(int i = 0; i < ANTS; i++){
		
			//in place of pressing up to move the ship
			ant[i].setMoveAngle(ant[i].getFaceAngle() - 90);
			ant[i].setVelX(calcAngleMoveX(ant[i].getMoveAngle()));
			ant[i].setVelY(calcAngleMoveY(ant[i].getMoveAngle()));
		
			//used in reversing the direction the ship is facing
			double faceAngle = ant[i].getFaceAngle();
		
			if(resting == 5){//how many steps before ant changes the way it is facing.
				System.out.println(resting);
				changeDirection(ant[i]);
				if(i == ANTS - 1){
					resting = 0;
				}
			}
			//update ship X's position, distance the ant moves in one step
			ant[i].incX(ant[i].getVelX() * 5);
			//System.out.println(ant.getVelX());
		
			//collide with left/right edge
			if(ant[i].getX() < - 5){
				ant[i].setX(5);
				checkFaceAngle(faceAngle);	
				bounceOffEdge(faceAngle, ant[i]);
			}
			else if(ant[i].getX() > 640 + 5){
				ant[i].setX(640 - 5);
				checkFaceAngle(faceAngle);	
				bounceOffEdge(faceAngle, ant[i]);
			}		
			//update ship Y's position
			ant[i].incY(ant[i].getVelY() * 5);
			//System.out.println(ant.getVelY());
		
		
			//wrap around top/bottom
			if(ant[i].getY() < -5){
				ant[i].setY(5);
				checkFaceAngle(faceAngle);	
				bounceOffEdge(faceAngle,ant[i]);
			}	
			else if(ant[i].getY() > 480 + 5){
				ant[i].setY(480 - 5);
				checkFaceAngle(faceAngle);	
				bounceOffEdge(faceAngle,ant[i]);
			}
		}
	}
	
	//Update the ship position based on velocity
		public void updateEnemy(){
			//update ship X's position
			ship.incX(ship.getVelX() * 5);
			
			//wrap around left/right
			if(ship.getX() < - 5)
				ship.setX(5);
			else if(ship.getX() > 640 + 5)
				ship.setX(640 - 5);
			//update ship Y's position
			ship.incY(ship.getVelY() * 5);
			
			//wrap around top/bottom
			if(ship.getY() < -5)
				ship.setY(5);
			else if(ship.getY() > 480 + 5)
				ship.setY(480 - 5);
			
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
	
	public void bounceOffEdge(double faceAngle, Ant ant){
		ant.setFaceAngle(faceAngle);
		ant.setMoveAngle(ant.getFaceAngle() - 90);
		ant.setVelX(calcAngleMoveX(ant.getMoveAngle()) * 0.2);
		ant.setVelY(calcAngleMoveY(ant.getMoveAngle()) * 0.2);
	}
	
	//key listener events
		public void keyReleased(KeyEvent e){
				int keyCode = e.getKeyCode();
				if(keyCode == e.VK_LEFT){
					ship.setVelX(0);
					ship.setVelY(0);
				}
				if(keyCode == e.VK_RIGHT){
					ship.setVelX(0);
					ship.setVelY(0);
				}
				if(keyCode == e.VK_UP){
					ship.setVelX(0);
					ship.setVelY(0);
				}
				if(keyCode == e.VK_DOWN){
					ship.setVelX(0);
					ship.setVelY(0);
				}
			
		}
		public void keyTyped(KeyEvent k){}
		public void keyPressed(KeyEvent k){
			int keyCode = k.getKeyCode();
			switch(keyCode){
				case KeyEvent.VK_LEFT:
					//left arrow rotates ship left 5 degrees
					ship.setFaceAngle(270);
					ship.setMoveAngle(ship.getFaceAngle() - 90);
					ship.setVelX(calcAngleMoveX(ship.getMoveAngle()));
					ship.setVelY(calcAngleMoveY(ship.getMoveAngle()));
					break;
				case KeyEvent.VK_RIGHT:
					//right arrow rotates ship 5 degrees
					ship.setFaceAngle(90);
					ship.setMoveAngle(ship.getFaceAngle() - 90);
					ship.setVelX(calcAngleMoveX(ship.getMoveAngle()));
					ship.setVelY(calcAngleMoveY(ship.getMoveAngle()));
					break;
				case KeyEvent.VK_UP:
					//up arrow adds thrust to ship(1/10 normal speed)
					ship.setFaceAngle(0);
					ship.setMoveAngle(ship.getFaceAngle() - 90);
					ship.setVelX(calcAngleMoveX(ship.getMoveAngle()));
					ship.setVelY(calcAngleMoveY(ship.getMoveAngle()));
					break;
				case KeyEvent.VK_DOWN:
					//up arrow adds thrust to ship(1/10 normal speed)
					ship.setFaceAngle(180);
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
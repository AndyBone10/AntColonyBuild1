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
	boolean showBounds = true;
	
	private boolean behind = false;
	
	//makes the ant rest
	int resting = 0;
	
	//the player's ship
	int ANTS = 100;
	Ant [] ant = new Ant[ANTS];
	
	Enemy enemy = new Enemy();
	
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
		
		enemy.setX(60);
		enemy.setY(60);
		
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
		drawBounds();
		drawShip();
		drawEnemy();
		
		//repaint the applet window
		paint(g);
	}
	
	public void drawBounds(){
		
		if(showBounds){
			g2d.setColor(Color.WHITE);
			g2d.draw(enemy.getBounds());
			for(int i = 0; i < ANTS; i++){
				//g2d.draw(ant[i].getRadius());
				g2d.draw(ant[i].getDangerPheromone());
			}
		}
		
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
		g2d.translate(enemy.getX(),enemy.getY());
		g2d.rotate(Math.toRadians(enemy.getFaceAngle()));
		g2d.setColor(Color.PINK);
		g2d.fill(enemy.getShape());
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
		checkCollisions();
		updateAnt();
		updateEnemy();
		
	}
	
	public void changeDirection(Ant ant){
			int randomFaceAngle = rand.nextInt((360 - 0) + 1) + 0;
			ant.setFaceAngle(randomFaceAngle);
	}
	
	public double getDistance(Enemy e, Ant a){
		return (Math.sqrt(Math.pow((e.getX() - a.getX()), 2) + 
				Math.pow((e.getY() - a.getY()), 2)));
	}
	
	/*public void isBehind(Ant a, Enemy e){
		double distance = Math.sqrt(Math.pow((e.getX() - a.getX()), 2) + 
				Math.pow((e.getY() - e.getY()), 2));
		if(a.getY() < e.getY() && distance < 30){
			behind = true;
		}
		
	}*/
	
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
				//System.out.println(resting);
				if(getDistance(enemy, ant[i]) > 30 && ant[i].getBeenChecked() == true){
					ant[i].setBeenChecked(false);
					System.out.println(getDistance(enemy, ant[i]));
					System.out.println("unchecked");
				}
				
				if(ant[i].getBeenChecked() == false){
					changeDirection(ant[i]);
				}	
				//System.out.println(faceAngle);
				//ant[i].checkFaceAngle();
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
				//ant[i].checkFaceAngle();	
				bounceOffEdge(faceAngle, ant[i]);
			}
			else if(ant[i].getX() > 640 + 5){
				ant[i].setX(640 - 5);
				//ant[i].checkFaceAngle();	
				bounceOffEdge(faceAngle, ant[i]);
			}		
			//update ship Y's position
			ant[i].incY(ant[i].getVelY() * 5);
			//System.out.println(ant.getVelY());
		
		
			//wrap around top/bottom
			if(ant[i].getY() < -5){
				ant[i].setY(5);
				//ant[i].checkFaceAngle();	
				bounceOffEdge(faceAngle,ant[i]);
			}	
			else if(ant[i].getY() > 480 + 5){
				ant[i].setY(480 - 5);
				//ant[i].checkFaceAngle();	
				bounceOffEdge(faceAngle,ant[i]);
			}
		}
	}
	
	//Update the ship position based on velocity
		public void updateEnemy(){
			//update ship X's position
			enemy.incX(enemy.getVelX() * 5);
			
			//wrap around left/right
			if(enemy.getX() < - 5)
				enemy.setX(5);
			else if(enemy.getX() > 640 + 5)
				enemy.setX(640 - 5);
			//update ship Y's position
			enemy.incY(enemy.getVelY() * 3);
			
			//wrap around top/bottom
			if(enemy.getY() < -5)
				enemy.setY(5);
			else if(enemy.getY() > 480 + 5)
				enemy.setY(480 - 5);
			
		}
		
	//keeps face angle in range 0-360
	//public void checkFaceAngle(double faceAngle){
	//}
	
	public void bounceOffEdge(double faceAngle, Ant ant){
		ant.setFaceAngle(faceAngle);
	}
	
	public void checkCollisions(){
		//iterate through the asteroids array
		for(int m = 0; m < ANTS; m++){
			//perform the collision test
			if(ant[m].getRadius().intersects(enemy.getBounds()) && ant[m].getBeenChecked() == false){
						System.out.println("checked");
						ant[m].checkFaceAngle();
						//ant[m].setMoveAngle(ant[m].getFaceAngle() - 90);
						//ant[m].setVelX(calcAngleMoveX(ant[m].getMoveAngle()));
						//ant[m].setVelY(calcAngleMoveY(ant[m].getMoveAngle()));
						ant[m].setBeenChecked(true);
				}	
				
				
			}
				
				/*check for collision with ship
				if(ast[m].getBounds().intersects(ship.getBounds())){
					ast[m].setAlive(false);
					ship.setX(320);
					ship.setY(240);
					ship.setFaceAngle(0);
					ship.setVelX(0);
					ship.setVelY(0);
					continue;
				}*/
	}
	//key listener events
		public void keyReleased(KeyEvent e){
				int keyCode = e.getKeyCode();
				if(keyCode == e.VK_LEFT){
					enemy.setVelX(0);
					enemy.setVelY(0);
				}
				if(keyCode == e.VK_RIGHT){
					enemy.setVelX(0);
					enemy.setVelY(0);
				}
				if(keyCode == e.VK_UP){
					enemy.setVelX(0);
					enemy.setVelY(0);
				}
				if(keyCode == e.VK_DOWN){
					enemy.setVelX(0);
					enemy.setVelY(0);
				}
			
		}
		public void keyTyped(KeyEvent k){}
		public void keyPressed(KeyEvent k){
			int keyCode = k.getKeyCode();
			switch(keyCode){
				case KeyEvent.VK_LEFT:
					//left arrow rotates ship left 5 degrees
					enemy.setFaceAngle(270);
					enemy.setMoveAngle(enemy.getFaceAngle() - 90);
					enemy.setVelX(calcAngleMoveX(enemy.getMoveAngle()));
					enemy.setVelY(calcAngleMoveY(enemy.getMoveAngle()));
					break;
				case KeyEvent.VK_RIGHT:
					//right arrow rotates ship 5 degrees
					enemy.setFaceAngle(90);
					enemy.setMoveAngle(enemy.getFaceAngle() - 90);
					enemy.setVelX(calcAngleMoveX(enemy.getMoveAngle()));
					enemy.setVelY(calcAngleMoveY(enemy.getMoveAngle()));
					break;
				case KeyEvent.VK_UP:
					//up arrow adds thrust to ship(1/10 normal speed)
					enemy.setFaceAngle(0);
					enemy.setMoveAngle(enemy.getFaceAngle() - 90);
					enemy.setVelX(calcAngleMoveX(enemy.getMoveAngle()));
					enemy.setVelY(calcAngleMoveY(enemy.getMoveAngle()));
					break;
				case KeyEvent.VK_DOWN:
					//up arrow adds thrust to ship(1/10 normal speed)
					enemy.setFaceAngle(180);
					enemy.setMoveAngle(enemy.getFaceAngle() - 90);
					enemy.setVelX(calcAngleMoveX(enemy.getMoveAngle()));
					enemy.setVelY(calcAngleMoveY(enemy.getMoveAngle()));
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
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;


public class Ant implements Runnable{
	//Global variables
	int x,y, xDirection, yDirection;
	boolean resting = false;
	boolean shouldSetRandDir = true;
	Rectangle Ant;
	Rectangle target = new Rectangle(50,50,15,15);

	public Ant(int x, int y){
		this.x = x;
		this.y = y;
		//create 'Ant'
		Ant = new Rectangle(this.x, this.y, 5, 5);
	}
		public int chooseRandomDirection(){
			Random r = new Random();
			int [] randDirections = new int[3];
			randDirections[0] = 0;
			randDirections[1] = 1;
			randDirections[2] = -1;
			int randChoice = r.nextInt(3);//generates 0,1 or 2
			return randDirections[randChoice];
		}
		//In run method move in that direction and wait
		
	public void setXDirection(int xdir){
		xDirection = xdir;
	}
	
	public void setYDirection(int ydir){
		yDirection = ydir;
	}
	
	public void draw(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(Ant.x, Ant.y, Ant.width, Ant.height);
		g.setColor(Color.PINK);
		g.fillRect(target.x,target.y,target.width,target.height);
	}
	
	public void collisionWithEnemy(){
		if(Ant.intersects(target)){
			Ant.x = Ant.x;
			Ant.y = Ant.y;
		}	
	}
	
	public void move(){
		//collision();
		Ant.x += xDirection;
		Ant.y += yDirection;
		
		//Bounce Ant when edge detected
		if(Ant.x <= 0){
			setXDirection(+1);
		}	
		if(Ant.x >= 690){
			setXDirection(-1);
		}
		if(Ant.y <= 10)
			setYDirection(+1);
		if(Ant.y >= 590)
			setYDirection(-1);
	}
	
	public void findPathToTarget(){
		if(Ant.x < target.x){
			setXDirection(1);
		}
		if(Ant.x > target.x){
			setXDirection(-1);
		}
		if(Ant.y < target.y){
			setYDirection(1);
		}
		if(Ant.y > target.y){
			setYDirection(-1);
		}
	}
	
	public void moveRandom(){
		try{
			while(true){
				if(!resting){
					if(shouldSetRandDir){
					setXDirection(chooseRandomDirection());
					setYDirection(chooseRandomDirection());
					shouldSetRandDir = false;
					}
					long start = System.currentTimeMillis();
					long end = start + 1 * 1000;
					while(System.currentTimeMillis() < end){
						if((Math.sqrt(Math.pow((target.x - Ant.x), 2) + 
								Math.pow((target.y - Ant.y), 2))) < 100){
							findPathToTarget();
							collisionWithEnemy();
							move();
							System.out.println((Math.sqrt(Math.pow((target.x - Ant.x), 2) + 
									Math.pow((target.y - Ant.y), 2))));
						}
						else{
							collisionWithEnemy();
							move();
							System.out.println((Math.sqrt(Math.pow((target.x - Ant.x), 2) + 
									Math.pow((target.y - Ant.y), 2))));
						}
						Thread.sleep(8);
					}
					resting = true;
				}					
				else{
					Thread.sleep(1000); //how long will object rest for
					shouldSetRandDir = true;
					resting = false;
				}
			}
			
		}
		catch(Exception ex){
			System.err.println(ex.getMessage());
		}
	}
	
	public void run() {
		try{
			while(true){
				moveRandom();
				Thread.sleep(1);
			}
		} catch(Exception e){ System.err.println(e.getMessage());}
	}
}

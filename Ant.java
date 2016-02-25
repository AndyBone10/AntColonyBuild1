import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;


public class Ant implements Runnable{
	//Global variables
	int x,y, xDirection, yDirection;
	boolean resting = false;
	boolean shouldSetRandDir = true;
	Rectangle ant;
	Rectangle target = new Rectangle(50,50,15,15);
	int status = 0;

	public Ant(int x, int y, int status){
		this.x = x;
		this.y = y;
		this.status = status;
		System.out.println(status);
		//create 'Ant'
		ant = new Rectangle(this.x, this.y, 5, 5);
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
		g.fillRect(ant.x, ant.y, ant.width, ant.height);
	}
	
	public void collisionWithEnemy(){
		if(ant.intersects(target)){
			ant.x = ant.x;
			ant.y = ant.y;
		}	
	}
	
	public void move(){
		//collision();
		ant.x += xDirection;
		ant.y += yDirection;
		
		//Bounce Ant when edge detected
		if(ant.x <= 0){
			setXDirection(+1);
		}	
		if(ant.x >= 690){
			setXDirection(-1);
		}
		if(ant.y <= 10)
			setYDirection(+1);
		if(ant.y >= 590)
			setYDirection(-1);
	}
	
	public void findPathToTarget(){
		if(ant.x < target.x){
			setXDirection(1);
		}
		if(ant.x > target.x){
			setXDirection(-1);
		}
		if(ant.y < target.y){
			setYDirection(1);
		}
		if(ant.y > target.y){
			setYDirection(-1);
		}
	}
	
	public void antBorder(Ant a){
		if (ant.intersects(a.ant) && ant.x > a.ant.x){
			ant.x += 10;
		}
		
		if (ant.intersects(a.ant) && ant.x < a.ant.x){
			ant.x -= 10;
		}
		
		if (ant.intersects(a.ant) && ant.y > a.ant.y){
			ant.y += 10;
		}
		
		if (ant.intersects(a.ant) && ant.y < a.ant.y){
			ant.y -= 10;
		}
	}
	
	public void findPathToAnt(Ant a){
		if(ant.x < a.x){
			setXDirection(1);
		}
		if(ant.x > a.x){
			setXDirection(-1);
		}
		if(ant.y < a.y){
			setYDirection(1);
		}
		if(ant.y > a.y){
			setYDirection(-1);
		}
	}
	
	/*public void moveRandom(){
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
							move();
							System.out.println(ant.x + " " + ant.y);
							//System.out.println((Math.sqrt(Math.pow((target.x - Ant.x), 2) + 
								//	Math.pow((target.y - Ant.y), 2))));
						Thread.sleep(10);
					}
					resting = true;
				}					
				else{
					
					shouldSetRandDir = true;
					resting = false;
				}
			}
			
		}
		catch(Exception ex){
			System.err.println(ex.getMessage());
		}
	}*/
	
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
						if((Math.sqrt(Math.pow((target.x - ant.x), 2) + 
								Math.pow((target.y - ant.y), 2))) < 100 || status == 1){
							status = 1;
							findPathToTarget();
							collisionWithEnemy();
							move();
							System.out.println(ant.x + " " + ant.y);
						}
						else{
							collisionWithEnemy();
							move();
							System.out.println(ant.x + " " + ant.y);
							//System.out.println((Math.sqrt(Math.pow((target.x - Ant.x), 2) + 
								//	Math.pow((target.y - Ant.y), 2))));
						}	
						Thread.sleep(10);
					}
					resting = true;
				}					
				else{
					
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
				Thread.sleep(4);
			}
		} catch(Exception e){ System.err.println(e.getMessage());}
	}
}

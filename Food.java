import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Random;
//Food class - represents pieces of square pieces of food
public class Food extends BaseVectorShape{
	//the level of food in the source
	private int foodLevel;
	private int [] shipx = {0, -8, -8, 0};
	private int [] shipy = {0, 0, -8, -8};
	Random r = new Random();
	//random positions
	private int randX = r.nextInt(600-6)+ 6;
	private int randY = r.nextInt(400-6)+ 6;
	//boolean for checking if returned search point exists
	boolean exists;
	//bounding rectangle
	public Rectangle getBounds(){
		Rectangle r;
		r = new Rectangle((int)getX() - 9, (int)getY() - 9, 10,10);
		return r;
	}
	Food(){
		setShape(new Polygon(shipx,shipy,shipx.length));
		foodLevel = r.nextInt(100-5) + 5; //random food value
		while(true){
			if(((randX-320)^2 + (randY - 240)^2) > (50 ^ 2)) //make sure food is not drawn too close to nest
				break;
			randX = r.nextInt(600-6)+ 6;
			randY = r.nextInt(400-6)+ 6;
		}
		setX(randX);
		setY(randY);
		exists = true;
	}
	//create a food object with no values
	Food(int x){
		exists = false;
	}
	public void removeFood(){foodLevel--;}
	public int getFoodLevel(){return foodLevel;}
	public boolean checkExist(){return exists;}
}
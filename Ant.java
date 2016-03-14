import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.awt.geom.Point2D;
//Ship class - polygonal shape of the player's ship
public class Ant extends BaseVectorShape{
	//define the polygon
	private int [] antx = {-3, -1, 0, 1, 3, 0};
	private int [] anty = {3, 3, 3, 3, 3, -3};
	//define arraylist of breadcrumbs for finding nest
	public ArrayList<Point2D.Double> breadcrumbs = new ArrayList<Point2D.Double>();
	//value for food
	private boolean hasFood;
	
	boolean decrement;
	
	private boolean keepDirection;
	
	private boolean beenChecked;
	
	private boolean onWayHome;
	//value for incrementing through breadcrumbs on way home
	private int distNest;
	//the distance an ant can search
	private double searchRadius = 5.0;
	//bounding rectangle
	
	public Rectangle getBounds(){
		Rectangle r;
		r = new Rectangle((int)getX() - 6, (int)getY() - 6, 12,12);
		return r;
	}
	
	public Rectangle getRadius(){
		Rectangle r;
		r = new Rectangle((int)getX() -19 , (int)getY() - 19, 40,40);
		return r;
	}
	
	public Rectangle getDangerPheromone(){
		Rectangle r;
		r = new Rectangle((int)getX() -30 , (int)getY() - 30, 60,60);
		return r;
	}
	
	Ant(){
		setShape(new Polygon(antx,anty,antx.length));
		setAlive(true);
		distNest = 0;
		hasFood = false;
		onWayHome = false;
		beenChecked = false;
		keepDirection = false;
		decrement = true;
	}
	
	public void checkFaceAngle() {
		double faceAngle = getFaceAngle();
		if(faceAngle > 180){
			setFaceAngle(faceAngle - 180);
		}
		else{
			setFaceAngle(faceAngle + 180);
		}
	}
	
	public boolean getBeenChecked(){
		return beenChecked;
	}
	
	public void setBeenChecked(boolean b){
		this.beenChecked = b;
	}
	
	public boolean getOnWayHome(){
		return onWayHome;
	}
	public void setOnWayHome(boolean b){
		this.onWayHome = b;
	}
	
	public int getDistNest(){
		return distNest;
	}
	
	public void setDistNest(int x){
		 this.distNest = x;
	}
	
	public boolean getKeepDirection(){
		return keepDirection;
	}
	
	public void setKeepGoing(boolean b){
		this.keepDirection = b;
	}
	
	public boolean getDecrement(){
		return decrement;
	}
	
	public void setDecrement(boolean b){
		this.decrement = b;
	}
	
	//add crumbs for every scouting step
	public void putBreadcrumb(){
		breadcrumbs.add(new Point2D.Double(getX(), getY()));
		distNest++;
	}
	//set food to true if ant has found food
	public void takeFood(){ hasFood=true;}
	//check if ant has food
	public boolean foodState(){ return hasFood;}
	//get next step to home
	public Point2D.Double getNestMove(){
		if(decrement){
			distNest--;
		}
		Point2D.Double breadcrumb = breadcrumbs.get(distNest);
		if(distNest == 0){
			breadcrumbs.clear();
			onWayHome = false;
		}	
		if(distNest < 0){
			distNest = 0;
		}
		return breadcrumb;
	}
	//return the strongest foodpoint in range, if none found return a foodpoint located at (0.0,0.0)
	public FoodPoint searchFoodPoint(ArrayList<FoodPoint> foodpoints){
		FoodPoint bestPoint = new FoodPoint(0);
		int strongest = 0;
		for(int i = 0; i < foodpoints.size();i++)
		{
			double x = foodpoints.get(i).getFoodX();
			double y = foodpoints.get(i).getFoodY();
			if(isInRadius(x,y)){
				if(foodpoints.get(i).getStrength() > strongest){
					bestPoint = foodpoints.get(i);
					strongest = foodpoints.get(i).getStrength();
				}		
			}
		}
		return bestPoint;
	}
	//check for any food in radius, if none found return a food object located at (0.0,0.0)
	public Food checkFood(Food [] food)
	{
		Food bestFood = new Food(0);
		int highestFood = 0;
		for(int i = 0; i < 5; i++){
			if(isInRadius(food[i].getX(),food[i].getY())){
				if(food[i].getFoodLevel() > highestFood){
					bestFood = food[i];
					highestFood = food[i].getFoodLevel();
				}
			}
		}
		return bestFood;
	}
	public boolean isInRadius(double x,double y){
		if(((x - getX()) * (x - getX())) + ((y - getY()) * (y - getY())) < searchRadius * searchRadius)
			return true;
		else
			return false;
	}
	
}
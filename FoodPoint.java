import java.awt.geom.Point2D;
//class to represent food points and store the points for ants to check
public class FoodPoint{
	private Point2D.Double Coords;
	private int strength;
	private boolean exists;
	FoodPoint(double x,double y){
		Coords = new Point2D.Double(x,y);
		strength = 1000;
		exists = true;
	}
	FoodPoint(int x){
		exists = false;
	}
	//decrement strength
	public void decrementStr(){strength -= 10;}
	
	//increment strength
	public void incrementStr(){strength += 500;}
	//accessors
	public double getFoodX(){return Coords.x;}
	public double getFoodY(){return Coords.y;}
	public int getStrength(){return strength;}
	public boolean checkExist(){return exists;}
	
}

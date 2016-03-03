import java.awt.Polygon;
import java.awt.Rectangle;
//Ship class - polygonal shape of the player's ship
public class Ant extends BaseVectorShape{
	//define the polygon
	private int [] antx = {-3, -1, 0, 1, 3, 0};
	private int [] anty = {3, 3, 3, 3, 3, -3};
	
	private boolean beenChecked = false;
	
	public boolean getBeenChecked(){
		return beenChecked;
	}
	
	public void setBeenChecked(boolean b){
		this.beenChecked = b;
	}

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
}

import java.awt.Polygon;
import java.awt.Rectangle;
//Ship class - polygonal shape of the player's ship
public class Enemy extends BaseVectorShape{
	//define the polygon
	private int [] shipx = {0, -10, -7, 3, 7, 10};
	private int [] shipy = {-10, -2, 10, 7, 10, -2};
	
	//bounding rectangle
	public Rectangle getBounds(){
		Rectangle r;
		r = new Rectangle((int)getX() - 6, (int)getY() - 6, 12,12);
		return r;
	}
	Enemy(){
		setShape(new Polygon(shipx,shipy,shipx.length));
		setAlive(true);
	}
}
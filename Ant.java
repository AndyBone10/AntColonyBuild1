import java.awt.Polygon;
import java.awt.Rectangle;
//Ship class - polygonal shape of the player's ship
public class Ant extends BaseVectorShape{
	//define the polygon
	private int [] antx = {-3, -1, 0, 1, 3, 0};
	private int [] anty = {3, 3, 3, 3, 3, -3};
	
	//bounding rectangle
	public Rectangle getBounds(){
		Rectangle r;
		r = new Rectangle((int)getX() - 6, (int)getY() - 6, 12,12);
		return r;
	}
	Ant(){
		setShape(new Polygon(antx,anty,antx.length));
		setAlive(true);
	}
}

import java.awt.Polygon;
import java.awt.Rectangle;
public class Nest extends BaseVectorShape{
	//define the polygon
	private int foodStock;
	private int [] shipx = {0, -25, -40, -25, 0, 14};
	private int [] shipy = {0, 0, -20, -40, -40, -20};
	
	//bounding rectangle
	public Rectangle getBounds(){
		Rectangle r;
		r = new Rectangle((int)getX() - 44, (int)getY() - 46, 62,54);
		return r;
	}
	Nest(){
		setShape(new Polygon(shipx,shipy,shipx.length));
		foodStock = 0;
		setX(333);
		setY(260);
	}
	public void addStock(){foodStock++;}
	public void feed(){if(foodStock>0) foodStock--;}
}
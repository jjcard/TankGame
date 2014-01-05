package a4.gameObjects;

import java.awt.Point;
import java.util.List;

public interface ICollidable {
	public boolean collidesWith(ICollidable obj);
	/**
	 * 
	 * @param obj
	 * @return true if the object is destroyed and 
	 * should be removed
	 */
	public boolean handleCollision(ICollidable obj);
	

	/**
	 * get the size of the collision box to use
	 * for detection
	 * @return
	 */
	public int getCollisionBoxHeight();
	/**
	 * get the size of the collision box to use
	 * for detection
	 * @return
	 */	
	public int getCollisionBoxWidth();
	
	public int getCollisionRadius();
	//public String getName();
	
	public void addCollision(ICollidable obj);
	
	public void clearCollidedWith();
	
	public List<ICollidable> getCollidedWith();
	public Point getCenter();
}

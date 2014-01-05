package a4.gameObjects;

import java.awt.Color;

/**
 * a GameObject that is unmovable after creation.
 * @author User
 *
 */
public abstract class LandscapeGameObject extends GameObject {
	

	public LandscapeGameObject(String name, double x, double y, Color color) {
		super(name, x, y, color);
	}
	
	/**
	 * landscape objects are not allowed to 
	 * change their position after creation.
	 * @throws UnsupportedOperationException()
	 */
	protected void setX(double x){
		throw new UnsupportedOperationException("Not allowed to change position after creation"); 
	}
	/**
	 * landscape objects are not allowed to 
	 * change their position after creation.
	 * @throws UnsupportedOperationException()
	 */
	protected void setY(double x){
		throw new UnsupportedOperationException("Not allowed to change position after creation"); 
	}
	


}

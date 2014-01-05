package a4.gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Random;

import a4.Game;

public class Rock extends LandscapeGameObject {
	
	private static final int bloat = 4;
	private static final int widthMax = 20 + bloat;
	private static final int heightMax = 20 + bloat;

	private int width;
	private int height;
	/**
	 * brown
	 */
	private static Color defaultColor = new Color(156, 93, 82);
	public Rock(String name, double x, double y) {
		super(name, x, y, defaultColor);
		
		Random r = new Random();
		
		//random values between (1, widthMax)
		width = r.nextInt(widthMax) + 1 + bloat;
		//random values between (1, heightMax)
		height = r.nextInt(heightMax) + 1 + bloat;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public String toString(){
		return super.toString() + ", width=" + width + ", height=" + height;
	}

	@Override
	public void draw(Graphics g) {
		
		Graphics2D graphics = (Graphics2D) g;
		//save transform
		AffineTransform saveAT = graphics.getTransform();
		
		
		applyTransforms(graphics);
		
		int x = - width/2;
		int y =  - height/2;
		g.setColor(getColor());
		g.fillRect(x, y, width, height);		
		
		if (Game.debug){
			g.setColor(Color.BLACK);
			g.drawOval(x, y, this.getCollisionRadius() * 2, this.getCollisionRadius() * 2);
			//g.drawRect(x, y, getCollisionBoxWidth(), getCollisionBoxHeight());
		}
		
		
		//restore transform
		graphics.setTransform(saveAT);
	}

//	@Override
//	public boolean collidesWith(ICollidable obj) {
//		// TODO Auto-generated method stub
//		return false;
//	}

	@Override
	public boolean handleCollision(ICollidable obj) {
		return false;
	}


	public int getRadius(){
		return Math.max(width, height)/2;
	}

}

package a4.gameObjects.projectile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import a4.Game;
import a4.gameObjects.ICollidable;
import a4.gameObjects.Tank;

public class Missile extends Projectile {
	
	private static int defaultLifeTime = 50;
	
	private static Color defaultColor = Color.RED;
	
	private static final int sideLength = 12;
	private static final int halfLength = sideLength/2;
	


	public Missile(String name, double x, double y, double direction, double speed, Tank sourceTank) {
		super( name, x, y, defaultColor,  direction,  speed, defaultLifeTime, sourceTank);
		damage = 1;
		//moveToEdgeOfTank();
		
	}
	public Missile(String name, double x, double y, double direction, double speed, int lifeTime, Tank sourceTank) {
		super( name, x, y, defaultColor,  direction,  speed, lifeTime, sourceTank);
		damage = 1;
		//moveToEdgeOfTank();
		
	}
	
	


	@Override
	public void draw(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		//save transform
		AffineTransform saveAT = graphics.getTransform();
		
		
		applyTransforms(graphics);
		g.setColor(getColor());
		
		//TODO figure this out...
		
		int x1 = 0;//(int) getX();
		
		
		int y1 = 0;//(int) getY();
//		int[] xPoints = new int[]{x1, -halfLength, halfLength};
//		int[] yPoints = new int[]{halfLength, -halfLength, -halfLength};
		int[] xPoints = new int[]{-halfLength, halfLength, -halfLength};
		int[] yPoints = new int[]{halfLength, 0, -halfLength};
		g.fillPolygon(xPoints, yPoints, 3);
		
		if (Game.debug){
			g.setColor(Color.BLACK);
			g.drawOval(-halfLength, -halfLength, this.getCollisionRadius() * 2, this.getCollisionRadius() * 2);
			//g.drawRect(x, y, getCollisionBoxWidth(), getCollisionBoxHeight());
		}
		
		//restore transform
		graphics.setTransform(saveAT);
		
	}



	@Override
	public boolean handleCollision(ICollidable obj) {
		if (obj instanceof Tank || obj instanceof Missile){
			return true;
		}
		return false;
	}

	@Override
	public int getHeight() {
		return halfLength;
	}
	public int getWidth(){
		return sideLength;
	}
	public int getRadius(){
		return halfLength;
	}
	

}

package a4.gameObjects.projectile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import a4.gameObjects.ICollidable;
import a4.gameObjects.MovableGameObject;

public class Spike extends MovableGameObject {

	private int length = 2;
	private int halfLength = length/2;
	public static final Color defaultColor = Color.ORANGE;
	public Spike(String name, double x, double y,
			double direction, double speed) {
		super(name, x, y, defaultColor, direction, speed);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		Graphics2D graphics = (Graphics2D) g;
		//save transform
		AffineTransform saveAT = graphics.getTransform();
		
		applyTransforms(graphics);
		g.setColor(getColor());
		int x1 = 0;//(int) getX();
		
		
		int y1 = 0;//(int) getY();
//		int[] xPoints = new int[]{x1, -halfLength, halfLength};
//		int[] yPoints = new int[]{halfLength, -halfLength, -halfLength};
		int[] xPoints = new int[]{-halfLength, halfLength, -halfLength};
		int[] yPoints = new int[]{halfLength, 0, -halfLength};
		g.fillPolygon(xPoints, yPoints, 3);
		//restore transform
		graphics.setTransform(saveAT);

	}

	@Override
	public boolean handleCollision(ICollidable obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return length;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return length;
	}

	@Override
	public int getRadius() {
		// TODO Auto-generated method stub
		return halfLength;
	}
	
	protected void applyTransforms(Graphics2D g2d){
		g2d.transform(rotation);
		g2d.transform(scale);
		g2d.transform(translation);
		

	}

}

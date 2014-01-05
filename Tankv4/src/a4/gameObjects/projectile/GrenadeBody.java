package a4.gameObjects.projectile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import a4.Game;
import a4.gameObjects.ICollidable;
import a4.gameObjects.MovableGameObject;

public class GrenadeBody extends MovableGameObject {
	private static final Color defaultColor = Color.ORANGE;
	
	private static final int radius = 10;
	private static final int dimater = radius * 2;
	
	public GrenadeBody(String name, double x, double y,
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
		int x1 = -radius;
		
		
		int y1 = -radius;
		graphics.fillOval(x1, y1, dimater, dimater);
		
		if (Game.debug){
			g.setColor(Color.BLACK);
			g.drawOval(x1, y1, this.getCollisionRadius() * 2, this.getCollisionRadius() * 2);
			//g.drawRect(x, y, getCollisionBoxWidth(), getCollisionBoxHeight());
		}

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
		return dimater;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return dimater;
	}

	@Override
	public int getRadius() {
		// TODO Auto-generated method stub
		return radius;
	}

}

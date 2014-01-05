package a4.gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Random;

import a4.Game;

public class Tree extends LandscapeGameObject {
	private static final int bloat = 8;
	private static final int diameterMax = 16 + bloat;

	private int diameter;
	private int radius;
	
	private static Color defaultColor = Color.GREEN;
	
	
	public Tree(String name, double x, double y) {
		super( name, x, y, defaultColor);
		
		diameter = (new Random()).nextInt(diameterMax) + 1 + bloat;
		radius = diameter/2;
	}
	
	public int getDiameter(){
		return diameter;
	}
	
	public String toString(){
		return super.toString() + ", diameter=" + diameter;
	}

	@Override
	public void draw(Graphics g) {
		
		Graphics2D graphics = (Graphics2D) g;
		//save transform
		AffineTransform saveAT = graphics.getTransform();
		
		
		applyTransforms(graphics);
		
		g.setColor(getColor());
		int x = (int) ( - radius);
		int y = (int) (- radius);
		
		g.fillOval(x, y, diameter, diameter);
		
		if (Game.debug){
			g.setColor(Color.BLACK);
			g.drawOval(x,y, this.getCollisionRadius() * 2, this.getCollisionRadius() * 2);
			//g.drawRect(x, y, getCollisionBoxWidth(), getCollisionBoxHeight());
		}
		
		//restore transform
		graphics.setTransform(saveAT);
	}

	@Override
	public boolean handleCollision(ICollidable obj) {
		return false;
	}

	@Override
	public int getHeight() {
		return diameter;
	}
	public int getWidth(){
		return diameter;
	}
	public int getRadius(){
		return radius;
	}


}

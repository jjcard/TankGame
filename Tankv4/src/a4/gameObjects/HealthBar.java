package a4.gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import a4.Game;

public class HealthBar extends MovableGameObject {



	private static final Color HealthColor = Color.GREEN;
	private static final Color damageColor = Color.RED;
	
	private double health; 
	private double maxHealth;
	private int curDisplayDamage;
	private int curDisplayHealth;	
	
	public HealthBar(String name, double x, double y,
			double direction, double speed, int health, int maxHealth) {
		super(name, x, y, HealthColor, direction, speed);
		
		this.health = health;
		this.maxHealth = maxHealth;
		updateDisplayDamage();
		

	}


	public void updateHealth(int newHealth){
		this.health = newHealth;
		updateDisplayDamage();
	}
	
	private void updateDisplayDamage(){
		curDisplayHealth = (int) (health/maxHealth * 100);
		curDisplayDamage = 100- curDisplayHealth;
		
//		if (Game.debug){
//			System.out.println("curDisplayHealth=" + curDisplayHealth);
//			System.out.println("curDisplayDamage=" + curDisplayDamage);
//		}
	}
	@Override
	public void draw(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		//save transform
		AffineTransform saveAT = graphics.getTransform();
		
		
		applyTransformsOrbit(graphics);
		
		int y = -50;//(int) (-50 * scale.getScaleY());
		int x = -10;//(int) (-10* scale.getScaleX());
		
//		if (Game.debug){
//			System.out.println("HealthBar Location:" + x + ", " + y);
//		}
		graphics.setColor(HealthColor);
		graphics.fillRect(x, y, 20, 100);
		
		
		graphics.setColor(damageColor);
		graphics.fillRect(x, y, 20, curDisplayDamage);
		
		
		
		
		
		//restore transform
		graphics.setTransform(saveAT);

	}

	@Override
	public boolean handleCollision(ICollidable obj) {
		return false;
	}

	@Override
	public int getRadius() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}
	public String toString(){
		return super.toString() + ", health=" + health + ", maxHealth=" + maxHealth;
	}

}

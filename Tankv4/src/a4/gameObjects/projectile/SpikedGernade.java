package a4.gameObjects.projectile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import a4.Game;
import a4.gameObjects.ICollidable;
import a4.gameObjects.Tank;

public class SpikedGernade extends Projectile {
	


	private static final Color defaultColor = Color.CYAN;
	private static final int defaultLifeTime = 20;
	private  double flameIncrement = 0.01;
	private static final double maxFlameOffset = .13;
	private double flameOffset = 0;
	
	private GrenadeBody body;
	private Spike[] spikes;
	public SpikedGernade(String name, double x, double y, double direction,
			double speed, Tank sourceTank) {
		super(name, x, y, defaultColor, direction, speed, defaultLifeTime, sourceTank);
		damage = 2;
		
		body = new GrenadeBody(name+"-body", 0, 0, direction, speed);
		spikes = new Spike[4];
		
		Spike s0 = new Spike(name+"-spike0", 0, 0, direction, speed);
		s0.translate(0, 1); s0.scale(5, 8);
		spikes[0] = s0;
		
		Spike s1 = new Spike(name+"-spike1", 0, 0, direction, speed);
		s1.translate(0, 2); s1.rotate(-90); s1.scale(5, 5);
		spikes[1] = s1;
		
		Spike s2 = new Spike(name+"-spike2", 0, 0, direction, speed);
		s2.translate(0, 1); s2.rotate(180); s2.scale(5, 8);
		spikes[2] = s2;
		
		Spike s3 = new Spike(name+"-spike3", 0, 0, direction, speed);
		s3.translate(0, 2); s3.rotate(90); s3.scale(5, 5);
		spikes[3] = s3;
		
		
		if (Game.debug){
			System.err.println("I am BORN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		}
		
	}
	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		Graphics2D graphics = (Graphics2D) g;
		//save transform
		AffineTransform saveAT = graphics.getTransform();
		
		
		applyTransforms(graphics);
		

		//g.setColor(getColor());
		//g.drawOval(x, y, 5, 5);
		body.draw(graphics);
		for (Spike s: spikes){
			s.draw(graphics);
		}
		
		if (Game.debug){		
			int x = -getCollisionRadius();
		int y = - getCollisionRadius();
			g.setColor(Color.BLACK);
			g.drawOval(x, y, this.getCollisionRadius() * 2, this.getCollisionRadius() * 2);
			//g.drawRect(x, y, getCollisionBoxWidth(), getCollisionBoxHeight());
		}
		
		
		//restore transform
		graphics.setTransform(saveAT);
		
	}
//	public boolean collidesWith(ICollidable obj) {
//	
//		if(body.collidesWith(obj)){
//			return true;
//		}
//		for (Spike s: spikes){
//			if (s.collidesWith(obj)){
//				return true;
//			}
//		}
//		return false;
//	}
	@Override
	public boolean handleCollision(ICollidable obj) {
		// TODO Auto-generated method stub
		return obj instanceof Tank || obj instanceof Projectile;
	}
	@Override
	public int getCollisionBoxHeight() {
		// TODO Auto-generated method stub
		return body.getCollisionBoxHeight() + spikes[0].getCollisionBoxHeight();
	}
	@Override
	public int getCollisionBoxWidth() {
		// TODO Auto-generated method stub
		return body.getCollisionBoxWidth() + spikes[0].getCollisionBoxWidth();
	}
	@Override
	public int getCollisionRadius() {
		// TODO Auto-generated method stub
		return body.getCollisionRadius() + spikes[0].getCollisionRadius();
	}
	
	public void Move(int millies){
		super.Move(millies);
		
		this.rotate(1);
		flameOffset += flameIncrement;
		for (Spike s: spikes){
			s.translate(0, flameOffset);
		}
		if (Math.abs(flameOffset) >= maxFlameOffset){
			flameIncrement *= -1;
		}
		
	}
	@Override
	public int getRadius() {
		// TODO Auto-generated method stub
		return body.getCollisionRadius() + spikes[0].getCollisionRadius();
	}
	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return body.getCollisionBoxHeight() + spikes[0].getCollisionBoxHeight();
	}
	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return body.getCollisionBoxWidth() + spikes[0].getCollisionBoxWidth();
	}
	

}

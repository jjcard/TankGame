package a4.gameObjects.projectile;

import java.awt.Color;

import a4.gameObjects.ICollidable;
import a4.gameObjects.MovableGameObject;
import a4.gameObjects.Tank;

public abstract class Projectile  extends MovableGameObject{
	protected int lifeTime;
	protected int damage;
	protected int milliesCount = 0;
	private Tank sourceTank;
	public Projectile(String name, double x, double y, Color color,
			double direction, double speed, int lifeTime, Tank sourceTank) {
		super(name, x, y, color, direction, speed);
		this.lifeTime = lifeTime;
		this.sourceTank = sourceTank;
	}
	
	public int getDamage(){
		return damage;
	}
	
	public int getLifeTime(){
		return lifeTime;
	}
	public boolean hasLifeTime(){
		return lifeTime > 0;
	}
	/**
	 * if the missile is alive
	 * moves the Missile forward
	 * and reduces the lifeTime counter by 1
	 * 
	 */
	public void Move(int millies){
		if (hasLifeTime()){
			super.Move(millies);	
			 milliesCount += millies;
			 if ( milliesCount > 1000){
				lifeTime--; 
				milliesCount = 0;
			 }
		}
	}
	public Tank getSourceTank(){
		return sourceTank;
	}

	public boolean collidesWith(ICollidable obj) {
		
		
		if (super.collidesWith(obj)){
			if (obj instanceof Projectile){
				return ((Projectile)obj).getSourceTank() != sourceTank;
			}
			return sourceTank != obj;
		} else {
			return false;
		}
		
	}
	public String toString(){
		return super.toString() + ", lifetime=" + lifeTime + ", sourceTank=" + sourceTank.getName();
	}
	
	
	

	
}

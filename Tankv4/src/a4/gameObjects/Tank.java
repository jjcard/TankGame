package a4.gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

import a4.Game;
import a4.Sound;
import a4.gameObjects.projectile.Missile;
import a4.gameObjects.projectile.PlasmaWave;
import a4.gameObjects.projectile.Projectile;
import a4.gameObjects.projectile.SpikedGernade;

public class Tank extends MovableGameObject implements ISteerable, ISelectable {

	/**
	 * the speed multiple a missile gets when fired
	 * from the tank
	 */
	private static final int missileSpeedAddition = 81;
	private static final int plasmaWaveSpeedAdditions = 40;
	private static final int grenadeSpeedAddition = 30;
	private static final int defaultMissileCount = 30;
	private int missileCount = defaultMissileCount;
	
	private static final int defaultArmorStrength = 10;
	private int armorStrength = defaultArmorStrength;

	private static Color defaultColor = Color.LIGHT_GRAY;
	
	private static Color selectColor = Color.GRAY;
	private boolean blocked;
	private FiringStrategy firingStrategy;
	private boolean shouldFire = false;
	private int milliesCount = 0;
	private HealthBar healthBar;
	
	private boolean selected = false;
	private static int maxSpeed = 80;
	
	private static final int tankSide = 30;
	private static final int halfSide = tankSide/2;
	private static final int barrelLength = 12;
	private static final int barrelWidth = 8;
	private static final Sound fireMissile = new Sound("missile.wav");
	private static final Sound hitRock = new Sound("crash.wav");
	private static final Sound missileHit = new Sound("metal.wav");

	
	public Tank(String name, double x, double y, double direction, double speed) {
		super(name, x, y, defaultColor, direction, speed);
		healthBar = new HealthBar(name + "-healthBar", 0, 0, 90, 0, armorStrength, armorStrength);
		if (Game.debug){
			System.out.println(this);
			System.out.println(healthBar);
		}
		healthBar.translate(-tankSide - 45, 0);
		healthBar.scale(.3, .3);
	}
	
	
	public static int getDefaultMissileCount(){
		return defaultMissileCount;
	}
	public int getMissileCount(){
		return missileCount;
	}
	public int getArmorStrength(){
		return armorStrength;
	}
	public void setFiringStrategy(FiringStrategy firingS){
		firingStrategy = firingS;
	}
	
	public FiringStrategy getFiringStrategy(){
		return firingStrategy;
	}
	/**
	 * returns true if the firingStratagy says the tank should fire this turn
	 * @return
	 */
	public boolean shouldFire(){
		boolean toReturn = shouldFire;
		
		shouldFire = false;
		return toReturn;
	}
	
	/**
	 * reduces the armor strength by 1 to a
	 * minimum of 1 returns true if
	 * the tank still has armor
	 * @return
	 */
	public boolean hit(Projectile projectile){
		if (hasArmor()){
			reduceArmor(projectile.getDamage());
		}
		
		return hasArmor();
	}
	
	private void reduceArmor(int damage){
		armorStrength -= damage;
		healthBar.updateHealth(armorStrength);
	}
	
	public boolean hasArmor(){
		return armorStrength > 0;
	}
	public boolean canFireMissile(){
		return missileCount > 0;
	}
	/**
	 * if the Tank can fire a missile
	 * returns a new Missile with the same 
	 * coordinates as the tank, going forward 
	 * from the tanks position. Else returns
	 * null.
	 * @return
	 */
	private Missile FireMissile(){
		if (canFireMissile()){
			missileCount--;
			if (GameObject.soundOn){
				fireMissile.play();
			}
			Missile re = new Missile(getName() + "-missile" + missileCount,getX(), getY(), getDirection(), getSpeed() + missileSpeedAddition, this);
			return re;
		}
		return null;
		
	}
	public PlasmaWave FirePlasmaWave(){
		return new PlasmaWave(getName() + "-plasmaWave", getX(), getY(), getDirection(), getSpeed() + plasmaWaveSpeedAdditions, this);
	}
	public SpikedGernade FireSpikedGrenade(){
		return new SpikedGernade(getName(), getX(), getY(), getDirection(), getSpeed() + grenadeSpeedAddition, this);
	}
	public Projectile FireProjectile(ProjectileTypes type){
		if (type != null){
			switch(type){
			case MISSILE:
				return FireMissile();
			case SPIKE_GRENADE:
				return FireSpikedGrenade();
			case PLASMA_WAVE:
				return FirePlasmaWave();
			}
		}
		return null;
	}
	
	public String toString(){
		return super.toString() + ", a=" + armorStrength + ", m=" + missileCount + 
				((firingStrategy == null) ? "": (", FS=" + firingStrategy.getClass().getSimpleName()));
	}


	@Override
	public double turn(double degrees) {

		setDirection(getDirection() + degrees);
		rotation.rotate(Math.toRadians(degrees));
		blocked = false;
		return getDirection();
	}
	private double getX(){
		return translation.getTranslateX();
	}
	private double getY(){
		return translation.getTranslateY();
	}

	@Override
	public double speedUp(double speed) {
		if (!blocked){
			setSpeed(getSpeed() + speed);			
		}
		
		return getSpeed();	

	}
	public void Move(int millies){
		if (!blocked){
			super.Move(millies);
		}
		
		milliesCount += millies;
		if (firingStrategy != null && milliesCount > 1000){
			shouldFire = firingStrategy.apply();	
			milliesCount = 0;
			setSpeed(getSpeed() +1);
		}	
	}

	public void setSpeed(double speed){
		if (speed > maxSpeed){
			super.setSpeed(maxSpeed);
		} else {
			super.setSpeed(speed);	
		}
		
		
		
	}
	@Override
	public double slowDown(double speed) {
		setSpeed(getSpeed() - speed);
		return getSpeed();
	}

	public boolean isBlocked() {
		return blocked;
	}
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	public void stop(){
		setSpeed(0);
	}



	@Override
	public void draw(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		//save transform
		AffineTransform saveAT = graphics.getTransform();
		
		
		applyTransforms(graphics);
		
		int x =  - halfSide;
		int y =  - halfSide;
		
		if (selected){
			g.setColor(selectColor);
		} else {
			g.setColor(getColor());	
		}
//		if (Game.debug){
//			System.out.println("Tank translation: "+translation);
//			System.out.println("Tank " + this);
//		}
		g.fillRect(x, y, tankSide, tankSide);
		
		int barrelX = halfSide;
		int barrelY = -barrelWidth/2 ;
		
		
		
		g.fillRect(barrelX, barrelY, barrelLength, barrelWidth);
		
		
		healthBar.draw(graphics);
		if (Game.debug){
			drawBoundingBox(x, y, graphics);
		}
		
		//restore transform
		graphics.setTransform(saveAT);
		
		
		
		
	}



//	@Override
//	public boolean collidesWith(ICollidable obj) {
//		// TODO Auto-generated method stub
//		return false;
//	}



	public boolean collidesWith(ICollidable obj) {
		
		
		if (super.collidesWith(obj)){
			if (obj instanceof Projectile){
				return this != ((Projectile)obj).getSourceTank();
			} else {
//				if(blocked){
//					this.turn(90);
//					blocked = false;
//					
//					return false;
//				}
				return true;
			}
		} else {
			return false;
		}
		
	}
	@Override
	public boolean handleCollision(ICollidable obj) {
		if (obj instanceof Projectile){
			//TODO
			if (soundOn){
				missileHit.play();
			}
			return !this.hit((Projectile) obj);
		}
		if (obj instanceof LandscapeGameObject || obj instanceof Tank){
			if (!this.blocked){
				this.stop();
				this.setBlocked(true);
				if (obj instanceof Rock){
					if (soundOn){
						hitRock.play();
					}
				}				
			}

			//System.out.println("Blocked");
			//this.turn(90);
			//System.out.println("blocked");
		}
		return false;
	}



	@Override
	public int getHeight() {
		return tankSide;
	}
	
	public int getWidth(){
		return tankSide;
	}
	
	public int getRadius(){
		return halfSide;
	}
	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public boolean contains(Point p) {
		
		double px =  p.getX();
		double py =  p.getY();
		Point center = getCenter();
		double x = center.getX() - halfSide;
		double y = center.getY() - halfSide;
		
		if ( (px >= x) && (px <= (x+getCollisionBoxWidth())) 
				&& (py >= y) && (py <= (y+getCollisionBoxHeight())) ){
			if (Game.debug){
				System.out.println("mouse at " + p + " clicked on " + this);
				System.out.println("x=" + (x+getCollisionBoxWidth())+", y=" + (y+getCollisionBoxHeight()));				
			}

			return true;
			
		}
		return false;
	}
	
	public static enum ProjectileTypes{
		MISSILE, SPIKE_GRENADE, PLASMA_WAVE
	}
	

}

package a4.gameObjects;

import java.awt.Color;

public abstract class MovableGameObject extends GameObject {
	
	 
	private double direction;
	private double speed;

	public MovableGameObject(String name, double x, double y, Color color, double direction, double speed) {
		super(name, x, y, color);
		
		setDirection(direction);
		rotation.rotate(Math.toRadians(direction));
		this.speed = speed;
	}
	
	public double getDirection(){
		return direction;
	}
	public double getSpeed(){
		return speed;
	}
	/**
	 * sets the direction, using modular 360
	 * to only allow values in the range (0-360]
	 * @param direction
	 */
	protected void setDirection(double direction){
		this.direction = direction % 360;
	}
	/**
	 * sets the speed of the MovableGameObject.
	 * With a minimum of 0.
	 * @param speed
	 */
	protected void setSpeed(double speed){
		this.speed = speed > 0 ? speed: 0;
	}
	
	
	/**
	 * moves the gameObject's position
	 * by its given direction and speed.
	 */
	public void Move(int millis){
		

		
		double fractionOfSecond = millis/1000.0;
		double amount = fractionOfSecond * speed;

		MoveAmount(0, amount);
		
	}
	
	public void MoveAmount(double turn, double amount){
		
		double theta = Math.toRadians(direction + turn);
		
		double deltaX;
		if (Math.abs(theta % Math.PI) == Math.PI/2){
			//way to get around round off error in Math.cos(double)
			deltaX = 0;
			
		} else {
			deltaX = Math.cos(theta)*amount;
		}
		double deltaY = Math.sin(theta)*amount;
		
//		setX(getX() + deltaX);
//		setY(getY() + deltaY);
		
		translation.translate(deltaX, deltaY);
	}
	
	
	public String toString(){
		return super.toString() + ", dir=" + direction + ", s=" + speed;
	}

}

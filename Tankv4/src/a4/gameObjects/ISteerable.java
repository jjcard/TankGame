package a4.gameObjects;

public interface ISteerable {
	
	
	/**
	 * moves the object's position(x, y)
	 * using the direction and speed.
	 */
	public void Move(int millies);
//	public double getX();
//	public double getY();
	public double getDirection();
	public double getSpeed();
	/**
	 * turns the given number of degrees and
	 * returns the result.
	 * @param degrees
	 * @return direction
	 */
	public double turn(double degrees);
	
	/**
	 * speeds up the object by the given amount.
	 * @param speed
	 * @return
	 */
	public double speedUp(double speed);
	/**
	 * slows down the object by the given amount.
	 * minimum is still 0.
	 * @param speed
	 * @return
	 */
	public double slowDown(double speed);
	
	/**
	 * stops all motion of the object
	 */
	public void stop();
	

}

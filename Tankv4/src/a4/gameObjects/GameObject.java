package a4.gameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

import a4.Game;
import a4.view.IDrawable;

public abstract class GameObject implements IDrawable, ICollidable{

	private String name;
	private Color color;
	private List<ICollidable> collidedWith;
	
	
	protected static boolean soundOn = true;
	protected AffineTransform translation;
	protected AffineTransform rotation;
	protected AffineTransform scale;
	
	public GameObject(String name, double x, double y, Color color){
		this.name = name;
		
		this.color = color;
		collidedWith = new LinkedList<ICollidable>();
		translation = new AffineTransform();
		rotation = new AffineTransform();
		scale = new AffineTransform();
		
		translation.translate(x, y);
		
	}
	
	public static void setSound(boolean sound){
		soundOn = sound;
	}
	public void clearCollidedWith(){
		collidedWith.clear();
	}
	public void addCollision(ICollidable obj){
		collidedWith.add(obj);
	}
	public List<ICollidable> getCollidedWith(){
		return collidedWith;
	}

	public Color getColor(){
		return color;
	}

	
	public String getName(){
		return name;
	}

	

	
	public String toString(){
		return name+": loc=" +getLocationString()+ ", c=" + getColorString(); 
	}
	
	/**
	 * if the color is non-null, returns the
	 * rgb values
	 * @param color
	 * @return
	 */
	private String getColorString(){
		if (color != null){
			return "["+color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "]";	
		}
		return "";
		
	}
	/**
	 * returns the x and y, rounded up
	 * @return
	 */
	private String getLocationString(){
		return new BigDecimal(translation.getTranslateX()).setScale(2, RoundingMode.HALF_EVEN) + ", " +
				new BigDecimal(translation.getTranslateY()).setScale(2, RoundingMode.HALF_EVEN);
	}
	
	@Override
	public boolean collidesWith(ICollidable obj) {
		
		
		if (this == obj){
			return false;
		}
		if (Game.debug){
			if (obj == null){
				System.out.println("Welll...something else went wrong");
			}			
		}

//		int r1 = (int)this.getX() + this.getCollisionBoxWidth();
//		int l1 = (int)this.getX();
//		int t1 = (int)this.getY();
//		int b1 = (int) this.getY() + this.getCollisionBoxHeight();
//		
//		int r2 = (int)obj.getX() + obj.getCollisionBoxWidth();
//		int l2 = (int)obj.getX();
//		int t2 = (int)obj.getY();
//		int b2 = (int) obj.getY() + obj.getCollisionBoxHeight();
//		
//		
//		if ((r1 < l2) || (l1 > r2) || (t2 < b1) ||(t1 < b2)){
//			return false;
//		}
//		
//		return true;
		
		
		boolean result = false;
		Point thisCenter = getCenter();
		Point otherCenter = obj.getCenter();
		int thisCenterX = (int) (thisCenter.getX() + this.getCollisionRadius()); // find centers
		int thisCenterY = (int) (thisCenter.getY() + this.getCollisionRadius());
		int otherCenterX = (int) (otherCenter.getX() + obj.getCollisionRadius());
		int otherCenterY = (int) (otherCenter.getY() + obj.getCollisionRadius());
		// find dist between centers (use square, to avoid taking roots)
		int dx = thisCenterX - otherCenterX;
		int dy = thisCenterY - otherCenterY;
		int distBetweenCentersSqr = (dx*dx + dy*dy);
		// find square of sum of radii
		int thisRadius = this.getCollisionRadius();
		int otherRadius = obj.getCollisionRadius();
		int radiiSqr = (thisRadius*thisRadius + 2*thisRadius*otherRadius
		+ otherRadius*otherRadius);
		if (distBetweenCentersSqr <= radiiSqr) { 
			result = true ; 
		}
		return result ;
	}
	/**
	 * returns how much the two objects circles overlap
	 * @param obj
	 * @return
	 */
	public int getCollisonOvelap(ICollidable obj){
		Point thisCenter = getCenter();
		Point otherCenter = obj.getCenter();
		int thisCenterX = (int) (thisCenter.getX());// + this.getCollisionRadius()); // find centers
		int thisCenterY = (int) (thisCenter.getY());// + this.getCollisionRadius());
		int otherCenterX = (int) (otherCenter.getX());// + obj.getCollisionRadius());
		int otherCenterY = (int) (otherCenter.getY());// + obj.getCollisionRadius());
		// find dist between centers (use square, to avoid taking roots)
		int dx = thisCenterX - otherCenterX;
		int dy = thisCenterY - otherCenterY;
		int distBetweenCentersSqr = (dx*dx + dy*dy);
		// find square of sum of radii
		int thisRadius = this.getCollisionRadius();
		int otherRadius = obj.getCollisionRadius();
		int radiiSqr = (thisRadius*thisRadius + 2*thisRadius*otherRadius
		+ otherRadius*otherRadius);
		if (distBetweenCentersSqr <= radiiSqr) { 
			//System.out.println(radiiSqr - distBetweenCentersSqr);
			return (int) Math.sqrt(radiiSqr - distBetweenCentersSqr); 
		}
		return 0;
		
	}
	/**
	 * applies the transforms, translation last.
	 * The effect being that it rotates on its center
	 * before being moved.
	 * @param g2d
	 */
	protected void applyTransforms(Graphics2D g2d){
		g2d.transform(translation);
		g2d.transform(rotation);
		g2d.transform(scale);
		

	}
	/**
	 * applies the transforms, translation first.
	 * The effect being that the object will 
	 * orbit around a central point. 
	 * @param g2d
	 */
	protected void applyTransformsOrbit(Graphics2D g2d){
		g2d.transform(rotation);
		g2d.transform(scale);
		g2d.transform(translation);
		

	}
	public Point getCenter(){
		return getTransformedCenter();
	}
	private Point getTransformedCenter(){
		return transformPoint(new Point(0,0));
	}
	private Point transformPoint(Point p){
		AffineTransform transform = new AffineTransform();
		
		
		transform.concatenate(translation);
		transform.concatenate(rotation);
		transform.concatenate(scale);
		Point transformed = new Point();
		transform.transform(p, transformed);
		return transformed;
	}
	
	public void rotate(double degrees){
		rotation.rotate(Math.toRadians(degrees));
	}
	public void scale(double x, double y){
		scale.scale(x, y);
	}
	public void translate(double x, double y){
		translation.translate(x, y);
	}
	
	public abstract int getRadius();
	public abstract int getHeight();
	public abstract int getWidth();
	
	
	@Override
	public int getCollisionBoxHeight() {
		return (int) (scale.getScaleY() * getHeight());
	}
	public int getCollisionBoxWidth(){
		return (int) (scale.getScaleX()*getWidth());
	}
	public int getCollisionRadius(){
		return (int) (scale.getScaleX()*getRadius());
	}
	
	public void drawBoundingBox(int x, int y, Graphics2D g){
		g.setColor(Color.BLACK);
		g.drawOval(x, y, this.getCollisionRadius() * 2, this.getCollisionRadius() * 2);
		//g.drawRect(x, y, getCollisionBoxWidth(), getCollisionBoxHeight());
	}
	
	
	

	
	
	
	
	
}

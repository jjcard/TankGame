package a4.gameObjects.projectile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.Random;

import a4.Game;
import a4.gameObjects.ICollidable;
import a4.gameObjects.Tank;


public class PlasmaWave extends Projectile {
	private double tolerance = .001;
	private double maxLevel = 4;
	private static int defaultLifeTime = 30;
	private static Color defaultColor = Color.YELLOW;
	private Point q0, q1, q2, q3;
	private int height, width;
	
	public PlasmaWave(String name, double x, double y,
			double direction, double speed, Tank sourceTank) {
		super(name, x, y, defaultColor, direction, speed, defaultLifeTime, sourceTank);
		
		damage = 100;
		int maxWidth = sourceTank.getCollisionBoxWidth() *2;
		int minWidth = sourceTank.getCollisionBoxWidth()/2;
		int maxHeight = sourceTank.getCollisionBoxHeight()*4;
		int minHeight = sourceTank.getCollisionBoxHeight();
		Random r = new Random();
		int randomWidth = r.nextInt(maxWidth -minWidth) + minWidth;
		int halfRandWidth = randomWidth/2;
		int randomHeight = r.nextInt(maxHeight - minHeight) + minHeight;
		int halfRandHeight = randomHeight/2;
		
		int q1X  = r.nextInt(randomWidth) - halfRandWidth;
		int q2X = r.nextInt(randomWidth) - halfRandWidth;
		int q1Y = r.nextInt(randomHeight) - halfRandHeight;
		int q2Y = r.nextInt(randomHeight) - halfRandHeight;
		
		q0 = new Point(-halfRandWidth, halfRandHeight);
		q1 = new Point(q1X, q1Y);
		q2 = new Point(q2X, q2Y);
		q3 = new Point(halfRandWidth, -halfRandHeight);
		
		
//		if (Game.debug){
//			System.out.println("PlasmaWave Created " + x + "," + y);
//			System.out.println("randomWidth=" + randomWidth + " randomHeight=" + randomHeight);
//			System.out.println("halfRandWidth=" + halfRandWidth + " halfRandHeight=" + halfRandHeight);
//			System.out.println("q0=" + q0 + " q1=" + q1 + " q2=" + q2 + " q3=" + q3);
//		}
		height = randomHeight;
		width = randomWidth;


		
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		Graphics2D graphics = (Graphics2D) g;
		//save transform
		AffineTransform saveAT = graphics.getTransform();
		
		
		applyTransforms(graphics);
		
		int x = -width/2;
		int y =  -height/2;
		g.setColor(getColor());
		
		drawBezierCurve(q0, q1, q2, q3, graphics);
		g.setColor(Color.RED);
		Polygon p = new Polygon();
		p.addPoint((int)q0.getX(), (int)q0.getY());
		p.addPoint((int)q1.getX(), (int)q1.getY());
		p.addPoint((int)q2.getX(), (int)q2.getY());
		p.addPoint((int)q3.getX(), (int)q3.getY());
		graphics.drawPolygon(p);
		if (Game.debug){
			g.setColor(Color.BLACK);
			g.drawOval(x, y, this.getCollisionRadius() * 2, this.getCollisionRadius() * 2);
			//g.drawRect(x, y, getCollisionBoxWidth(), getCollisionBoxHeight());
		}
		
		
		//restore transform
		graphics.setTransform(saveAT);
	}

	public boolean collidesWith(ICollidable obj) {
		boolean re =super.collidesWith(obj);
		if (re && Game.debug){
			System.out.println("Plasma wave "+this +" colliding with " + obj);
		} 
		return re;
	}
	@Override
	public boolean handleCollision(ICollidable obj) {
		return obj instanceof Tank;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getRadius() {
		// TODO Auto-generated method stub
		return Math.max(width, height)/2;
	}
	
	private void drawBezierCurve(Point q0, Point q1, Point q2, Point q3, Graphics g){
		
		ControlPointVector Q = new ControlPointVector(q0, q1, q2, q3);
		
		drawBezierCurve(Q, 0, g);
		
	}
	private void drawBezierCurve(ControlPointVector q, int level, Graphics g){
		if (straightEnough(q) || level > maxLevel){
			g.drawLine(q.q0.x, q.q0.y, q.q3.x, q.q3.y);
//			if (Game.debug){
//				System.out.println("Drawing curve line, level=" + level);
//				System.out.println(q);
//			}
		} else {
			ControlPointVector left = new ControlPointVector();
			ControlPointVector right = new ControlPointVector();
			q.subDivide(left, right);
			
//			if (Game.debug){
//				System.out.println("recursive Left= " + left);
//				System.out.println("recursive right= " + right);
//			}
			drawBezierCurve(left, level + 1, g);
			drawBezierCurve(right, level + 1, g);
			
		}
	}
	private boolean straightEnough(ControlPointVector q){
		double d1 = q.q0.distance(q.q1) + q.q1.distance(q.q2) + q.q2.distance(q.q3);
		double d2 = q.q0.distance(q.q3);
		
		return Math.abs(d1 - d2) < tolerance;
	}
	
	
	private class ControlPointVector{
		private Point q0,  q1,  q2,  q3;
		ControlPointVector(Point q0, Point q1, Point q2, Point q3){
			super();
			this.q0 = q0;
			this.q1 = q1;
			this.q2 = q2;
			this.q3 = q3;
		}
		public ControlPointVector() {
			super();
		}
		private void setAllPoints(Point q0, Point q1, Point q2, Point q3){
			this.q0 = q0;
			this.q1 = q1;
			this.q2 = q2;
			this.q3 = q3;
		}
		
		public void subDivide(ControlPointVector left, ControlPointVector right){
			Point r0  = new Point(q0);
			Point r1 = dividePoint(addPoints(q0, q1), 2.0);
			Point r2 = addPoints(dividePoint(r1, 2.0), dividePoint(addPoints(q1, q2),4.0));

			
			
			Point s3 = new Point(q3);
			Point s2 = dividePoint(addPoints(q2, q3), 2.0);
			Point s1 = addPoints(dividePoint(addPoints(q1, q2), 4.0), dividePoint(s2, 2.0));

			Point r3 = dividePoint(addPoints(r2, s1), 2.0);
			Point s0  = new Point(r3);
			
			left.setAllPoints(r0, r1, r2, r3);
			right.setAllPoints(s0, s1, s2, s3);
			
//			if (Game.debug){
//				System.out.println("Building divided control:");
//				//System.out.println("q0=" + q0 + " q1="+q1 + " q2=" + q2 + " q3=" + q3);
//				System.out.println("Q= " + this);
//				//System.out.println("r0=" + r0 + " r1="+r1 + " r2=" + r2 + " r3=" + r3);
//				System.out.println("Divided LEFT= " + left);
//				//System.out.println("s0=" + s0 + " s1="+s1 + " s2=" + s2 + " s3=" + s3);
//				System.out.println("Divided RIGHT= " + right);
//			}
//			
		}
		private Point addPoints(Point a, Point b){
			double x = a.getX() + b.getX();
			double y = a.getY() + b.getY();
			return new Point((int)x, (int)y);
		}
		private Point dividePoint(Point a, double divide){
			double x = a.getX()/divide;
			double y = a.getY()/divide;
			return new Point((int)x, (int)y);
		}
		
		public String toString(){
			//for debug 
			return super.toString()+", q0=" + q0.getX() + "," + q0.getY() + " q1="+q1.getX() + "," + q1.getY()  + " q2=" + q2.getX() + "," + q2.getY()  + " q3=" + q3.getX() + "," + q3.getY() ;
		}
	}

}

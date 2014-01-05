package test;

import java.io.ObjectInputStream.GetField;
import java.util.Random;

import a4.gameObjects.Rock;
import a4.gameObjects.Tank;
import a4.gameObjects.Tree;
import a4.gameObjects.projectile.Missile;

public class Test {
	static int directionMultiple = 1;
	static int randomDegreeLimit = 360/directionMultiple;
	static Random r = new Random();
	public static void main(String[] args){
//		Tank tank = new Tank(1, 2, 3, 4);
//		System.out.println(tank);
//		
//		Missile missile = new Missile(1, 2, 3, 4);
//		System.out.println(missile);
//		
//		
//		Rock rock = new Rock(1, 2);
//		
//		while (rock.getHeight() != 20){
//			System.out.println(rock);
//			rock = new Rock(1, 2);
//		}
//		System.out.println("Final Rock: " + rock);
//		
//		Tree tree = new Tree(3, 4);
//		
//		while (tree.getDiameter() != 20){
//			tree = new Tree(3, 4);
//			System.out.println(tree);
//		}

		
		
		
		double direction = -90.0;
		System.out.println(Math.toRadians(direction) % Math.PI);
		System.out.println(Math.PI/2);
		if (Math.toRadians(direction) % Math.PI == Math.PI/2){
			System.out.println("success ya");
		}
		
		double theta = Math.toRadians(direction);
		System.out.println("Theta:"+theta);
		double cosX = Math.cos(theta);
		double speed = 1;
		double deltaX = cosX*speed;
		System.out.println("cosX:"+ cosX + " DeltaX:" + deltaX);
//		double sinY = Math.sin(theta);
//		double deltaY = sinY*speed;
//		System.out.println("sinY:" + sinY + " DeltaY:" + deltaY);
		
//		double dir = getRandomDirection();
//		int count = 0;
//		int breakCount = 100000;
//		double max = dir;
//		while (dir >= 0 && dir <= 360 && dir % directionMultiple == 0){
//			System.out.println(dir);
//			dir = getRandomDirection();
//			if (dir > max){
//				max = dir;
//			}
//			count++;
//			
//			if (count >= breakCount){
//				break;
//			}
//		}
//		
//		System.out.println(count+ " max:"+max);
	}
	
	
	private static double getRandomDirection(){
		
		return (double) (r.nextInt(randomDegreeLimit) * directionMultiple);
	}

}

package a4;

import java.awt.Point;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import a4.gameObjects.AimAndFireStrategy;
import a4.gameObjects.FiringStrategy;
import a4.gameObjects.GameObject;
import a4.gameObjects.ICollidable;
import a4.gameObjects.ISelectable;
import a4.gameObjects.MovableGameObject;
import a4.gameObjects.RapidFireStrategy;
import a4.gameObjects.Rock;
import a4.gameObjects.SmartFireStrategy;
import a4.gameObjects.Tank;
import a4.gameObjects.Tank.ProjectileTypes;
import a4.gameObjects.Tree;
import a4.gameObjects.projectile.Projectile;
import a4.view.GameWorldProxy;
import a4.view.IGameWorld;
import a4.view.IObservable;
import a4.view.IObserver;
import a4.view.ScreenPanelDimensions;

public class GameWorld implements IObservable, IGameWorld {
	/**
	 * the number a degree has to
	 * be a multiple of to be valid
	 */
	private static final int directionMultiple = 5;
	private static final int randomDegreeLimit = 360/directionMultiple;
	private static final int randomSpeedLimit = 8;
	
	private static final int speedIncrement = 4;
	private static final int turnIncrement = 5;
	
	private static final int enemyTankHitPoints = 20;
	private static final String playerName = "Player Tank";
	private int milliesCount = 0;
	
	
	private static int TotaltankCount = 0;
	
	private int timeInterval = 20;
	private int xBound;
	private int yBound;
	
	private int gameClock = 0;
	private int score = 0;
	private int highscore = 0;
	
	private static final int startingLives = 3;
	private int lives = startingLives;
	private Random r = new Random();
	
	
	private boolean gameOver = false;
	private boolean sound = true;
	private boolean play = true;
	
	
	private ScreenPanelDimensions dimensions;
	

	
	
	private Tank playerTank;
	private List<GameObject> gameObjects;
	private List<IObserver> observers;
	
	private static final int numStratagies = 3;
	private int strategyCount = 0;
	public GameWorld(int xBound, int yBound, int timeInterval) {
		//GameObject.setBounds(xBound, yBound);
		this.xBound = xBound;
		this.yBound = yBound;
		this.timeInterval = timeInterval;
		dimensions = new ScreenPanelDimensions(xBound, yBound, 0, 0, xBound, yBound);
		
		gameObjects = new LinkedList<GameObject>();
		observers = new LinkedList<IObserver>();
	}
	

	public ScreenPanelDimensions getDimensions(){
		return dimensions;
	}
	public void setDimensions(ScreenPanelDimensions dimensions){
		this.dimensions = dimensions;
	}
	public void InitializeGameWorld(int numEnemyTanks, int numRocks, int numTrees){
		
		gameObjects = new LinkedList<GameObject>();
		
		playerTank = getRandomPlayerTank(0);

		
		for (int i = 0; i < numEnemyTanks; i++){
			Tank tank = getRandomTank();
			while (collidesWithWorld(tank)){
				if (Game.debug){
					System.out.println("tank placement collision");
				}
				tank = getRandomTank();
			}
			
			gameObjects.add(tank);
		}
		
		for (int j = 0; j < numRocks; j++){
			Rock rock = getRandomRock(j);
			while (collidesWithWorld(rock)){
				if (Game.debug){
					System.out.println("rock placement collision");
				}
				rock = getRandomRock(j);
			}
			gameObjects.add(rock);
		}
		
		for (int k = 0; k < numTrees; k++){
			Tree tree = getRandomTree(k);
			while (collidesWithWorld(tree)){
				if (Game.debug){
					System.out.println("tree placement collision");
				}
				tree = getRandomTree(k);
			}
			gameObjects.add(tree);
		}
		
		gameObjects.add(playerTank);
		
		//reset counts
		lives = startingLives;
		score = 0;
		gameClock = 0;
		gameOver = false;
		TotaltankCount = 0;
		
		
		notifyObservers();
		
		
	}
	

	/**
	 * checks if the preposed gameobject collides
	 * with anything else in the game
	 * @param ob
	 * @return
	 */
	private boolean collidesWithWorld(GameObject ob){
		
		Iterator<GameObject> iter = iterator();
		
		while (iter.hasNext()){
			if (ob.collidesWith(iter.next())){
				return true;
			}
		}
		return false;
	}
	private Tank getRandomPlayerTank(int numtry){
		return new Tank(playerName, xBound/2 + numtry, yBound/2 + numtry, 90, 0.0);
	}
	private Tree getRandomTree(int i){
		return new Tree("Tree" + i, getRandomX(), getRandomY());
	}
	private Rock getRandomRock(int i){
		return new Rock("Rock" + i, +getRandomX(), getRandomY());
	}
	private Tank getRandomTank(){
		Tank re = new Tank("Tank" + (TotaltankCount++), getRandomX(), getRandomY(), getRandomDirection(), getRandomSpeed());
		re.setFiringStrategy(getNextFiringStratagy(re));
		return re;
	}
	
	
	
	/**
	 * creates a random double degree
	 * between 0 and 360(exclusive)
	 * that is a multiple of directionMultiple
	 * @return
	 */
	private double getRandomDirection(){
		
		return (double) (r.nextInt(randomDegreeLimit) * directionMultiple);
	}
	/**
	 * returns a random x value
	 * between 0(inclusive) and the xBound(exclusive)
	 * @return
	 */
	private double getRandomX(){
		return (double)r.nextInt(xBound);
	}
	/**
	 * returns a random y value
	 * between 0(inclusive) and the yBound(exclusive)
	 * @return
	 */
	private double getRandomY(){
		return (double)r.nextInt(yBound);
	}
	
	private double getRandomSpeed(){
		return (double) r.nextInt(randomSpeedLimit);
	}

	
	public void pauseGame(){
		play = false;
		
		
	}
	
	public void unpauseGame(){
		play = true;
		deselectAll();
	}

	/**
	 * moves all MovableGameObjects in the world.
	 * Checks for any missiles running out of lifeTime.
	 * If so, removes them from the world.
	 */
	public void tick(){
		
		if (play){
			Iterator<GameObject> iter = iterator();
			GameObject go;
			while (iter.hasNext()){
				go = iter.next();
				if (go instanceof MovableGameObject){
					((MovableGameObject) go).Move(timeInterval);
					if (go instanceof Projectile){
						if (!((Projectile) go).hasLifeTime()){
							//Projectile dies
							iter.remove();
						}
					} else if (go instanceof Tank){
						if(((Tank) go).shouldFire()){
							fireProjectile((Tank) go, ProjectileTypes.MISSILE);
						}
					}
				}
			}
			gameClock++;
			
			iter = iterator();
			HashSet<GameObject> destroyedList = new HashSet<GameObject>();
			HashSet<Tank> tanksHit = new HashSet<Tank>();
			while (iter.hasNext()){
				ICollidable hit = (ICollidable) iter.next();
				
				
				hit.clearCollidedWith();
				
				Iterator<GameObject> colIter = iterator();
				boolean destroyed = false;
				boolean tankAndHit = false;
				while (colIter.hasNext()){
					ICollidable hitWith = (ICollidable) colIter.next();
					if (hit.collidesWith(hitWith)){
						
						hit.addCollision(hitWith);

						destroyed = destroyed ||hit.handleCollision(hitWith);
	
						if (hit instanceof Tank){
							if (hitWith instanceof Projectile){
								if (hit != playerTank){
									increaseScore(enemyTankHitPoints);	
								}
							} else {
								//Tank has hit tree, rock, or other Tank
								tankAndHit = true;
							}
						}
					}
				}					
				if (destroyed){
						destroyedList.add((GameObject) hit);
				} else if (tankAndHit){
					//tanks collided with non-missiles
					tanksHit.add((Tank) hit);
				}
				
	
			}			
			
			removeDestroyed(destroyedList);
			
			handleTankCollisions(tanksHit);
		}

		
	}
	
	private void removeDestroyed(HashSet<GameObject> destroyedList){
		gameObjects.removeAll(destroyedList);
		
		//add a new  tank when one is destroyed
		for (GameObject dego: destroyedList){
			if (dego instanceof Tank){
				if (dego == playerTank){
					Tank player = respawnPlayerTank();
					if (player != null){
						gameObjects.add(player);	
					}
					
				} else {
					Tank tank = getRandomTank();
					while (collidesWithWorld(tank)){
						tank = getRandomTank();
					}
					gameObjects.add(tank);	
				}
			}
		}
	}
	private void handleTankCollisions(HashSet<Tank> tanksHit){
		for (Tank hitTank: tanksHit){
			for (ICollidable collided: hitTank.getCollidedWith()){
				int overlap = hitTank.getCollisonOvelap(collided);
				System.out.println((hitTank + " moving " + overlap + " from " + collided));
				hitTank.MoveAmount(180.0,hitTank.getCollisonOvelap(collided) + 1);
				
				if (Game.debug){
					if (hitTank.collidesWith(collided)){
						System.out.println("Something went WRONG!!!!!!!!!!!!!1");
						System.out.println(hitTank);
					}					
				}

				
				if (hitTank != playerTank){
					hitTank.turn(90);
				}
			}
		}
	}

	
	public void reverseSelectedTanks(){
		Iterator<GameObject> iter = iterator();
		
		GameObject go;
		
		while (iter.hasNext()){
			go = iter.next();
			if (go instanceof Tank){
				Tank t = (Tank) go;
				
				if (t.isSelected()){
					t.turn(180);
				}
			}
		}
	}
	public void playerTurn(boolean turnRight){
		if (turnRight){
			playerTank.turn(-turnIncrement);
		} else {
			playerTank.turn(turnIncrement);
		}
		
				
	}
	/**
	 * zooms the given number of times. Negative if zooming out
	 * @param units
	 */
	public void zoom(int units){
		double yChange = dimensions.getWinHeight() * .05 * units;
		double xChange = dimensions.getWinWidth() * .05 * units;
		
		dimensions.setWinHeight(dimensions.getWinHeight() + yChange);
		dimensions.setWinWidth(dimensions.getWinWidth() + xChange);
		dimensions.setWinLeft(dimensions.getWinLeft() - xChange/2);
		dimensions.setWinBottom(dimensions.getWinBottom() - yChange/2);
	}
	
	public void pan(int x, int y){
		dimensions.setWinLeft(dimensions.getWinLeft() + x);
		dimensions.setWinBottom(dimensions.getWinBottom() + y);
	}
	public void playerSpeedIncrement(){
		playerTank.speedUp(speedIncrement);
	}
	public void playerSpeedDecrement(){
		playerTank.slowDown(speedIncrement);
	}
	/**
	 * 
	 * @return true if missile fired
	 * successfully.
	 */
	public boolean playerFireMissile(){
		return fireProjectile(playerTank, ProjectileTypes.MISSILE);
	}
	public boolean playerFirePlasmaWave(){
		return fireProjectile(playerTank, ProjectileTypes.PLASMA_WAVE);
	}
	public boolean playerFireSpikeGrenade(){
		return fireProjectile(playerTank, ProjectileTypes.SPIKE_GRENADE);
	}
	
	public void switchEnemyStrategys(int millies){
		
		milliesCount += millies;
		
		
		if (milliesCount > 30000){
			milliesCount = 0;
			GameWorldIterator iter = (GameWorldIterator) iterator();
			
			Tank go;
			
			
			while (iter.hasNext()){
				go = iter.nextTank();
				if (go == null){
					//nothing to see here folks...
					break;
				}
				if (go != playerTank){
					go.setFiringStrategy(getNextFiringStratagy(go));
				}
			}			
		}

	}
	private FiringStrategy getNextFiringStratagy(Tank tank){
		
		
		strategyCount++;
		strategyCount = strategyCount % numStratagies;
		FiringStrategy fs;
		switch(strategyCount){
		case 0: fs = new RapidFireStrategy(); break;
		case 1: fs = new AimAndFireStrategy(); break;
		case 2: fs = new SmartFireStrategy(tank); break;
		default: fs = new AimAndFireStrategy(); break;
		}
		return fs;
		
	}
	/**
	 * 
	 * @param tank
	 * @return true if firing was successful.
	 */
	private boolean fireProjectile(Tank tank, Tank.ProjectileTypes type){
		
			Projectile missile = tank.FireProjectile(type);
			return addProjectile(missile);
			

	}
	
	private boolean addProjectile(Projectile missile){
		if (missile != null){
			gameObjects.add(missile);	
			return true;
		}
		return false;
		
	}
	


	/**
	 * if the player still has lives
	 * respawns the player, else sets GameOver
	 * to true and returns null.
	 * @return
	 */
	private Tank respawnPlayerTank(){
		if (lives > 0){
			lives--;
			int numTry= 0;
			playerTank = getRandomPlayerTank(numTry);

			
			while (collidesWithWorld(playerTank)){
				numTry++;
				playerTank = getRandomPlayerTank(numTry);
			}
			return playerTank;
		} else {
			gameOver();
			return null;
		}
	}
	
	/**
	 * sets gameOver to true, prints a Game over message
	 * displays the current game state and if it is a new high score.
	 * 
	 */
	private void gameOver(){
		gameOver = true;
		

	}

	private void increaseScore(int increase){
		score += increase;
		if (score > highscore){
			highscore = score;
		}
	}
	public boolean isGameOver(){
		return gameOver;
	}
	
//	/**
//	 * checks the object is inside the bounds of 
//	 * the x and y axis
//	 * @param go
//	 * @return
//	 */
//	private boolean outOfBounds(GameObject go){
//		return outOfXBounds(go) || outofYBounds(go);
//	}
//	/**
//	 * checks the object is inside the bounds of the x-axis
//	 * @param go
//	 * @return
//	 */
//	private boolean outOfXBounds(GameObject go){
//		return (go.getX() < 0 || go.getX() >= xBound);
//	}
//	/**
//	 * checks the object is inside the bounds of the y-axis
//	 * @param go
//	 * @return
//	 */
//	private boolean outofYBounds(GameObject go){
//		return (go.getY() < 0 || go.getY() >= yBound);
//	}
	public int getGameTime(){
		return gameClock;
	}
	public int getLivesLeft(){
		return lives;
	}
	public int getCurrentScore(){
		return score;
	}
	public int getHighScore(){
		return highscore;
	}
	
	public boolean isSoundOn(){
		return sound;
	}
	public void setSound(boolean soundOn){
		sound = soundOn;
	}
	




	@Override
	public void addObserver(IObserver o) {
		observers.add(o);
		
	}


	public void deselectAll(){
		Iterator<GameObject> itr = iterator();
		
		GameObject go;
		while (itr.hasNext()){
			go = itr.next();
			
			if (go instanceof ISelectable){
				 ((ISelectable)go).setSelected(false);

			}
		}	
	}
	
	public void pointSelected(Point p, boolean controlDown){
		
		Iterator<GameObject> itr = iterator();
		
		GameObject go;
		while (itr.hasNext()){
			go = itr.next();
			
			if (go instanceof ISelectable){
				ISelectable s =(ISelectable)go;
				if (s.contains(p)){
					s.setSelected(true);
				} else if (!controlDown){
					s.setSelected(false);
				}
			}
		}
		
	}
	
	@Override
	public void notifyObservers() {
		GameWorldProxy proxy = new GameWorldProxy(this);
		for (IObserver io: observers){
			io.update(proxy, timeInterval, dimensions);
		}	
		
		dimensions.clearChangeFlags();
		
	}
	
	public Iterator<GameObject> iterator(){
		return new GameWorldIterator();
	}
	
	private class GameWorldIterator implements Iterator<GameObject> {
		
		int currIndex;
		
		public GameWorldIterator(){
			currIndex = -1;
		}
		
		public boolean hasNext(){
			if (gameObjects.isEmpty()){
				return false;
			}
			if (currIndex >= gameObjects.size() - 1){
				return false;
			}
			return true;
		}
		@Override
		public GameObject next(){
			currIndex++;
			return (gameObjects.get(currIndex));
		}
		
		public <T extends GameObject> T next(Class<T> gameObjectClass){
			T returnObject = null;
			Object cur = null;
			while (hasNext()){
				cur = next();
				if (gameObjectClass.isInstance(cur)){
					returnObject = (gameObjectClass.cast(cur));
					break;
				}
			}
			
			return returnObject;
		
		}
		/**
		 * gets the Next Tank.
		 * Warning: even if (hasNext()) returns true
		 * does not guarantee the return here will be 
		 * non-null.
		 * @return
		 */
		public Tank nextTank(){
			return next(Tank.class);
		}

		@Override
		public void remove() {
			gameObjects.remove(currIndex);
		}
		
	}


	
	

}

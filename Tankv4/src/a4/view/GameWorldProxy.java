package a4.view;

import java.util.Iterator;

import a4.GameWorld;
import a4.gameObjects.GameObject;

public class GameWorldProxy implements IGameWorld, IObservable {
	
	private GameWorld realGameWorld;
	
	
	public GameWorldProxy(GameWorld gameWorld){
		realGameWorld = gameWorld;
	}

	@Override
	public int getGameTime() {
		return realGameWorld.getGameTime();
	}

	@Override
	public int getLivesLeft() {
		return realGameWorld.getLivesLeft();
	}

	@Override
	public int getHighScore() {
		return realGameWorld.getHighScore();
	}

	@Override
	public int getCurrentScore() {
		return realGameWorld.getCurrentScore();
	}
	
	@Override
	public boolean isSoundOn(){
		return realGameWorld.isSoundOn();
	}
	

	@Override
	public void addObserver(IObserver o) {
		//realGameWorld.addObserver(o);
		
	}
	@Override
	public void notifyObservers() {
		realGameWorld.notifyObservers();
		
	}

	@Override
	public Iterator<GameObject> iterator() {
		return realGameWorld.iterator();
	}

}

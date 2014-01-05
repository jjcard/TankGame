package a4.view;

import java.util.Iterator;

import a4.gameObjects.GameObject;

public interface IGameWorld extends Iterable<GameObject>{
	public int getGameTime();
	public int getLivesLeft();
	public int getHighScore();
	public int getCurrentScore();
	public boolean isSoundOn();
	
	public Iterator<GameObject> iterator();
	

}

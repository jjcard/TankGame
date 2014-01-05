package a4.gameObjects;

/**
 * Takes a turn to aim before firing
 *
 */
public class AimAndFireStrategy implements FiringStrategy {

	private boolean needToAim = true;
	@Override
	public boolean apply() {
		needToAim = !needToAim;
		return needToAim;
	}


}

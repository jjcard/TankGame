package a4.gameObjects;

public class SmartFireStrategy implements FiringStrategy {

	private Tank tank;
	private AimAndFireStrategy aimAndFire;
	private RapidFireStrategy rapidFire;
	private static int maxMissileCount = Tank.getDefaultMissileCount();
	private static int halfMaxMissile = maxMissileCount/2;
	
	public SmartFireStrategy(Tank tank){
		this.tank = tank;
		aimAndFire = new AimAndFireStrategy();
		rapidFire = new RapidFireStrategy();
	}
	@Override
	public boolean apply() {
		
		
		if (tank.canFireMissile()){
			int missileCount = tank.getMissileCount();		
			if (missileCount > halfMaxMissile){
				//have enough missiles, always fire!
				return rapidFire.apply();
			} else {
				return aimAndFire.apply();
			}
			
		}

		return false;
	}



}

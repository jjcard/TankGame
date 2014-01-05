package a4;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import a4.gameObjects.GameObject;
import a4.view.MapView;
import a4.view.ScoreView;
public class Game  extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4787196267824169088L;
	private GameWorld world;
	private MapView mapView;
	private ScoreView scoreView;
	private final int defaultScreenX = 1000;
	private final int defaultScreenY = 800;
	private final int defaultWorldX = 870;
	private final int defaultWorldY = 713;
	private final int tickerTime = 20;
	private final int startingTanks = 4;
	private final int startingRocks = 4;
	private final int startingTrees = 4;
	
	
	private Sound loopMusic;
	
	private boolean paused = false;	
	
	private List<AbstractAction> pauseDisabledCommands;

	
	private QuitCommand _quitCommand = new QuitCommand();
	private AboutCommand _aboutCommand = new AboutCommand();
	private HelpCommand _helpCommand = new HelpCommand();
	private TickCommand _tickCommand = new TickCommand();
	private SoundToggleCommand _soundCommand = new SoundToggleCommand();
	private PlayerFireCommand _playerFireCommand = new PlayerFireCommand();
	private PlayerTurnLeftCommand _turnLeftCommand = new PlayerTurnLeftCommand();
	private PlayerTurnRightCommand _turnRightCommand = new PlayerTurnRightCommand();
	private PlayerSpeedUpCommand _speedUpCommand = new PlayerSpeedUpCommand();
	private PlayerSlowDownCommand _slowCommand = new PlayerSlowDownCommand();
	private NewGameCommand _newCommand = new NewGameCommand();
	private PauseUnpauseCommand _pauseUnpauseCommand = new PauseUnpauseCommand(paused);
	private ReverseCommand _reverseCommand = new ReverseCommand();
	private FirePlasmaWave _firePlasmaWave = new FirePlasmaWave();
	private FireGrenade _fireGrenade = new FireGrenade();
	private PauseCommand _pauseCommand = new PauseCommand();
	private DebugCommand _debugCommand = new DebugCommand();
	
	private CommandPanel commandPanel;
	private boolean soundOn = true;
	public static  boolean debug = true;

	private Timer ticker = new Timer(tickerTime, _tickCommand);
	
	public Game(){
		
		
		world = new GameWorld(defaultWorldX, defaultWorldY, tickerTime);
		mapView = new MapView(world.getDimensions(), this);
		
		
		
		
		generatePauseDisabledList();
		
		scoreView = new ScoreView(world);
		
		world.addObserver(scoreView);
		world.addObserver(mapView);
		
		setSize(defaultScreenX, defaultScreenY);
		setTitle("Tank Combat");
		setLayout(new BorderLayout());
		setJMenuBar(getGameMenuBar());
		add(scoreView, BorderLayout.NORTH);
		add(mapView, BorderLayout.CENTER);
		commandPanel = new CommandPanel();
		add(commandPanel, BorderLayout.WEST);
		addKeyBindings();
		
		
		WindowListener windowListener= new WindowListener();
		this.addWindowFocusListener( windowListener);
		this.addWindowListener(windowListener);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		loopMusic = new Sound("loop");
		setVisible(true);
		
		
		intializeGameWorld();
		
		
	}
	public void zoom(int units){
		world.zoom(units);
		world.notifyObservers();
	}
	public void pan(int x, int y){
		world.pan(x, y);
		world.notifyObservers();
	}
	private void generatePauseDisabledList(){
		pauseDisabledCommands = new LinkedList<AbstractAction>();
		pauseDisabledCommands.add(_tickCommand);
		pauseDisabledCommands.add(_playerFireCommand);
		pauseDisabledCommands.add(_turnLeftCommand);
		pauseDisabledCommands.add(_turnRightCommand);
		pauseDisabledCommands.add(_speedUpCommand);
		pauseDisabledCommands.add(_slowCommand);
		pauseDisabledCommands.add(_playerFireCommand);
		pauseDisabledCommands.add(_fireGrenade);
		pauseDisabledCommands.add(_firePlasmaWave);
	}
	private JMenuBar getGameMenuBar() {
		JMenuBar bar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenuItem newItem = new JMenuItem("New");
		newItem.setAction(_newCommand);
		fileMenu.add(newItem);
		JMenuItem saveItem = new JMenuItem("Save");
		fileMenu.add(saveItem);
		JMenuItem undoItem = new JMenuItem("Undo");
		fileMenu.add(undoItem);
		JCheckBoxMenuItem soundMenu = new JCheckBoxMenuItem("Sound", true);
		soundMenu.setAction(_soundCommand);
		fileMenu.add(soundMenu);
		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.setAction(_aboutCommand);
		fileMenu.add(aboutItem);
		JCheckBoxMenuItem debugMenu = new JCheckBoxMenuItem("Debug Mode",debug);
		debugMenu.setAction(_debugCommand);
		fileMenu.add(debugMenu);
		final JMenuItem quitItem = new JMenuItem("Quit");
		quitItem.setAction(_quitCommand);
		fileMenu.add(quitItem);
		bar.add(fileMenu);
		

//		JMenu commandMenu = new JMenu("Commands");
////		JMenuItem fireEnemyMissileItem = new JMenuItem("Fire Enemy Missile");
////		fireEnemyMissileItem.setAction(_fireEnemyCommand);
////		commandMenu.add(fireEnemyMissileItem);
////		JMenuItem missileHitItem = new JMenuItem("Missile Hit:Tank");
////		missileHitItem.setAction(_tMCollisionCommand);
////		commandMenu.add(missileHitItem);
////		JMenuItem missileHitMissileItem = new JMenuItem("Missile Hit:Missile");
////		missileHitMissileItem.setAction(_mMCollisionCommand);
////		commandMenu.add(missileHitMissileItem);
//		JMenuItem tankCollideLandscapeItem = new JMenuItem("Tank hit:Landscape");
//		tankCollideLandscapeItem.setAction(_tLCollisionCommand);
//		commandMenu.add(tankCollideLandscapeItem);
//		bar.add(commandMenu);

		return bar;
	}
	
	private void addKeyBindings(){
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap(); 
        
        
       inputMap.put(KeyStroke.getKeyStroke("SPACE"), "playerFire");
       inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "playerTurnRight");
       inputMap.put(KeyStroke.getKeyStroke("LEFT"), "playerTurnLeft");
       inputMap.put(KeyStroke.getKeyStroke("UP"), "playerSpeedUp");
       inputMap.put(KeyStroke.getKeyStroke("DOWN"), "playerSlowDown");
       inputMap.put(KeyStroke.getKeyStroke('p'), "firePlasma");
       inputMap.put(KeyStroke.getKeyStroke('g'), "fireGrenade");
       
       
       actionMap.put("playerFire", _playerFireCommand);
       actionMap.put("playerTurnRight", _turnRightCommand);
       actionMap.put("playerTurnLeft", _turnLeftCommand);
       actionMap.put("playerSpeedUp", _speedUpCommand);
       actionMap.put("playerSlowDown", _slowCommand);
       actionMap.put("firePlasma", _firePlasmaWave);
       actionMap.put("fireGrenade", _fireGrenade);
	}

	/**
	 * prompts the player for the initial number
	 * of enemy tanks, rocks, and trees.
	 * @param scanner
	 */
	public void intializeGameWorld(){
		world.InitializeGameWorld(startingTanks, startingRocks, startingTrees);
		ticker.restart();
		loopMusic.loop();
		
	}

	public void quit(){
		int option = JOptionPane.showConfirmDialog(null, "Are you Sure You want to quit?", "Quit?", JOptionPane.YES_NO_OPTION);
		
		if (option == JOptionPane.YES_OPTION){
			System.exit(0);
		}
	}

	public void playerFireMissile(){
		world.playerFireMissile();
		world.notifyObservers();
	}
	public void playerFirePlasma(){
		world.playerFirePlasmaWave();
		world.notifyObservers();
	}
	public void playerFireSpikeGrenade(){
		world.playerFireSpikeGrenade();
		world.notifyObservers();
	}
	public void worldGameTick(){
		world.tick();
		world.notifyObservers();
	}
	public void playerTurnLeft(){
		world.playerTurn(false);
		world.notifyObservers();
	}
	public void playerTurnRight(){
		world.playerTurn(true);
		world.notifyObservers();
	}
	public void playerSpeedIncrement(){
		world.playerSpeedIncrement();
		world.notifyObservers();
	}
	public void playerSpeedDecrement(){
		world.playerSpeedDecrement();
		world.notifyObservers();
	}
	
	public void toggleSound(boolean soundOn){
		this.soundOn = soundOn;
		world.setSound(soundOn);
		if (soundOn && !paused){
			loopMusic.loop();
		} else {
			loopMusic.stop();
		}
		GameObject.setSound(soundOn);
		world.notifyObservers();
	}
	
	private void pauseGame(){
		paused = true;
		world.pauseGame();
		ticker.stop();
		loopMusic.stop();		
		commandPanel.pauseUnpauseButton.setText("Play");
		for (AbstractAction action: pauseDisabledCommands){
			action.setEnabled(false);
		}
		world.notifyObservers();
		

		
	}
	
	private void unpauseGame(){
		paused = false;
		
		world.unpauseGame();
		if (soundOn){
			loopMusic.loop();			
		}

			ticker.start();		
		commandPanel.pauseUnpauseButton.setText("Pause");
		for (AbstractAction action: pauseDisabledCommands){
			action.setEnabled(true);
		}
		world.notifyObservers();
		
	}
	
	public void debugModeToggle(){
		debug = !debug;
	}
	
	private void reverseSelected(){
		world.reverseSelectedTanks();
	}
	
	public void objectSelected(Point p, boolean controlDown){
		if (paused){
			world.pointSelected(p, controlDown);
			world.notifyObservers();
		}
		
	}
	private class CommandPanel extends JPanel{
		/**
		 * 
		 */
		private static final long serialVersionUID = -5174076987189135594L;
		private JButton pauseUnpauseButton;
		private JButton reverseButton;
		
		public CommandPanel(){
			
			
			super(new GridLayout(8, 1));
			
			JLabel commandLabel = new JLabel("Commands:");
									
			pauseUnpauseButton = new GameButton("Pause");
			pauseUnpauseButton.setAction(_pauseUnpauseCommand);
			
			
			reverseButton = new GameButton("Reverse");
			reverseButton.setAction(_reverseCommand);
			
			JButton HelpButton = new GameButton("Help");
			HelpButton.setMaximumSize(new Dimension(100, 100));
			HelpButton.setAction(_helpCommand);
			
			
			
			JButton quitButton = new GameButton("Quit");
			quitButton.setMaximumSize(new Dimension(100, 100));
			quitButton.setAction(_quitCommand);
			
			this.add(commandLabel);
			this.add(pauseUnpauseButton);
			this.add(reverseButton);

			this.add(HelpButton);
			this.add(quitButton);

		}
		
		public JButton getPauseUnpauseButton(){
			return pauseUnpauseButton;
		}
	}
	private class QuitCommand extends AbstractAction{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 2700463548565526397L;

		public QuitCommand(){
			super("Quit");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			printButtonPressed(e, this.getValue(NAME));
			quit();
			
		}
		
	}
	
	private class HelpCommand extends AbstractAction{
		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 2025632283544167486L;
		private static final String helpMessage = "Keyboard commands:\n" +
				"	    right arrow: turns the player right\n"+
				"	    left arrow: turns the player left\n" +
				"	    up arrow: increase the player's speed\n" +
				"	    down arrow: decrease the players speed\n" +
				"	    space: fire a missile from the player's tank\n" +
				"		'G': fire a Grenade " +
				"		'P': fire a plasma wave"+
				"Button commands:\n" +
				"	     'Help': prints this message\n" +
				"	     'Quit': confirms and then quits the game\n" +
				"Menu Commands:\n" +
				"	     'Fire Enemy Missile' : picks a random enemy tank and it fires a missile if possible\n"
				+"	     'Missile Hit Tank' : picks a random tank and collides it with a missile on the field if possible\n"+
				"	     'Missile Hit Missile' : see same button above\n";
		public HelpCommand(){
			super("Help");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			printButtonPressed(e, this.getValue(NAME));
			JOptionPane.showMessageDialog(null, helpMessage, "What the Commands do", JOptionPane.INFORMATION_MESSAGE);
			
		}
		
	}
	private class AboutCommand extends AbstractAction{

		/**
		 * 
		 */
		private static final long serialVersionUID = 7914463665577416917L;
		private static final String aboutMessage = 
				"Version: 0.4.4\n" +
				"Release Notes:\n" +
				"  - added plasma wave, hit detection still needs work\n" +
				"  - added Spike Grenade for player only\n" +
				"  - added zoom and pan functions\n" +
				"  - updated help menu\n" +
				"  - adding debug mode\n" +
				"  - added health bar for tanks";
		public AboutCommand(){
			super("About");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			printButtonPressed(e, this.getValue(NAME));
			JOptionPane.showMessageDialog(null, aboutMessage, "About", JOptionPane.INFORMATION_MESSAGE);
			
		}
		
	}
	private class TickCommand extends AbstractAction{

		/**
		 * 
		 */
		private static final long serialVersionUID = 7047720708230914257L;
		public TickCommand(){
			super("Tick");
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//printButtonPressed(arg0, this.getValue(NAME));
			worldGameTick();	
		}
	}

	private class SoundToggleCommand extends AbstractAction{

		/**
		 * 
		 */
		private static final long serialVersionUID = -1575767530435605594L;
		public SoundToggleCommand(){
			super("Sound");
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			printButtonPressed(e, this.getValue(NAME));
			
			if (e.getSource() instanceof JCheckBoxMenuItem){
				toggleSound( ((JCheckBoxMenuItem)e.getSource()).isSelected());
			}
			
			
		}
	}
	private class PlayerFireCommand extends AbstractAction{

		/**
		 * 
		 */
		private static final long serialVersionUID = -7926290275086699602L;
		public PlayerFireCommand(){
			super("Player Fire");
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			printButtonPressed(arg0, this.getValue(NAME));
			playerFireMissile();
			
		}
		
	}
	private class PlayerTurnRightCommand extends AbstractAction{

		/**
		 * 
		 */
		private static final long serialVersionUID = -8785118977748222433L;
		public PlayerTurnRightCommand(){
			super("Player Turn Right");
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			printButtonPressed(arg0, this.getValue(NAME));
			playerTurnRight();
			
		}
	}
	private class PlayerTurnLeftCommand extends AbstractAction{

		/**
		 * 
		 */
		private static final long serialVersionUID = -6930058637322864322L;
		public PlayerTurnLeftCommand(){
			super("Player Turn Left");
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			printButtonPressed(arg0, this.getValue(NAME));
			playerTurnLeft();
			
		}
	}
	private class FirePlasmaWave extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6285414982108195574L;
		public FirePlasmaWave(){
			super("Fire Plasma");
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			printButtonPressed(arg0, this.getValue(NAME));
			playerFirePlasma();
			
		}
		
	}
	private class FireGrenade extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = -3440895725484186053L;

		public FireGrenade(){
			super("Fire Grenade");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			printButtonPressed(arg0, this.getValue(NAME));
			playerFireSpikeGrenade();
			
		}
		
	}
	private class PlayerSlowDownCommand extends AbstractAction{

		/**
		 * 
		 */
		private static final long serialVersionUID = 5010617514975745608L;
		public PlayerSlowDownCommand(){
			super("Player Slow Down");
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			printButtonPressed(arg0, this.getValue(NAME));
			playerSpeedDecrement();
			
		}
	}
	private  class PlayerSpeedUpCommand extends AbstractAction{

		/**
		 * 
		 */
		private static final long serialVersionUID = -1633783757150718547L;
		public PlayerSpeedUpCommand(){
			super("Player Speed Up");
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			printButtonPressed(arg0, this.getValue(NAME));
			playerSpeedIncrement();
			
		}
	}
	
	
	private class NewGameCommand extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = -8790991966466063456L;

		public NewGameCommand(){
			super("New");
			
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			printButtonPressed(arg0, this.getValue(NAME));
			intializeGameWorld();
		}
		
		
	}
	
	private class PauseCommand extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = -573911239921200877L;

		public PauseCommand(){
			super("Pause");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (!paused){
				pauseGame();
			}
		}
	}
	private class PauseUnpauseCommand extends AbstractAction{

		/**
		 * 
		 */
		private static final long serialVersionUID = 4113199511317600758L;
		private static final String pauseName = "Pause";
		private static final String unpauseName = "Play";
		public PauseUnpauseCommand(boolean paused){
			super(paused? unpauseName: pauseName);
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (paused){
				unpauseGame();
			} else {
				pauseGame();
			}
		}
	}
	private class DebugCommand extends AbstractAction{

		public  DebugCommand(){
			super("Debug");
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			debugModeToggle();
			
		}
		
	}
	private class ReverseCommand extends AbstractAction{

		/**
		 * 
		 */
		private static final long serialVersionUID = -3257427675895955014L;
		public ReverseCommand(){
			super("Reverse");
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			reverseSelected();
			
		}
		
	}

	private void printButtonPressed(ActionEvent arg0, Object name){
		
		String toPrint = arg0.getSource().getClass().getSimpleName();
		
		toPrint += ": \""+name + "\" pressed";
		
		
		System.out.println(toPrint);
	}
	
	
	

	private class GameButton extends JButton{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 7206546639089665669L;

		public GameButton(String name){
			super(name);
			this.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "none");
		}
	}
	private class WindowListener implements WindowFocusListener, java.awt.event.WindowListener{

		@Override
		public void windowGainedFocus(WindowEvent e) {
			
		}

		@Override
		public void windowLostFocus(WindowEvent e) {
			ActionEvent event = new ActionEvent(e, 0, getName());
			_pauseCommand.actionPerformed(event);
			
		}

		@Override
		public void windowActivated(WindowEvent arg0) {
			
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
			
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			quit();
			
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
			
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
			
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
			
		}

		@Override
		public void windowOpened(WindowEvent arg0) {			
		}


		
	}
	
	
	
		
		

}

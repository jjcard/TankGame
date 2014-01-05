package a4.view;

import javax.swing.JLabel;
import javax.swing.JPanel;

import a4.GameWorld;

public class ScoreView extends JPanel implements IObserver {

	private int time;
	private int score;
	private int highScore;
	private int lives;
	private boolean soundOn;
	
	private JLabel timeLabel;
	private JLabel scoreLabel;
	private JLabel highScoreLabel;
	private JLabel livesLabel;
	private JLabel soundLabel;
	
	
	private static final String timeString = "Elapsed Time: ";
	private static final String livesString = "Lives Left: ";
	private static final String scoreString = "Score: ";
	private static final String highScoreString = "High Score: ";
	private static final String soundString = "Sound: ";
	
	private int milliesCount = 0;
	
	
	public ScoreView(GameWorld gw){
		updateValues(gw);
		
		timeLabel = new JLabel(timeString + time);
		scoreLabel = new JLabel(scoreString + score);
		highScoreLabel = new JLabel(highScoreString + highScore);
		livesLabel = new JLabel(livesString + lives);
		soundLabel = new JLabel(soundString + (soundOn ? "ON": "OFF"));
		
		this.add(timeLabel);
		this.add(scoreLabel);
		this.add(highScoreLabel);
		this.add(livesLabel);
		this.add(soundLabel);
		
	}
	@Override
	public void update(IObservable o, Object... arg) {
		
		
		if (arg[0] instanceof Integer){
			milliesCount += (int) arg[0];
			if (milliesCount > 1000){
				if (o instanceof IGameWorld){
					IGameWorld gw = (IGameWorld) o;
					milliesCount = 0;
					updateValues(gw);
					updateLabels();
					
				}				
			}
			
		}

		
		
	}
	
	private void updateValues(IGameWorld gw){
		time = gw.getGameTime();
		lives = gw.getLivesLeft();
		highScore = gw.getHighScore();
		score = gw.getCurrentScore();
		soundOn = gw.isSoundOn();
	}
	private void updateLabels(){
		timeLabel.setText(timeString + time);
		scoreLabel.setText(scoreString + score);
		highScoreLabel.setText(highScoreString + highScore);
		livesLabel.setText(livesString + lives );
		soundLabel.setText(soundString + (soundOn ? "ON": "OFF"));
	}

}

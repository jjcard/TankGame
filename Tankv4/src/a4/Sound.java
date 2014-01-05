package a4;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;

public class Sound {
	
	private static final String soundDir = "." + File.separator + "Sounds" + File.separator;
	AudioClip myClip;
	public Sound(String name){
		
		try {
			File file = new File(soundDir + name);
			if (file.exists()){
				myClip = Applet.newAudioClip(file.toURI().toURL());
			} else {
				//log
				System.out.println("File doesn't exist!");
			}
		
		} catch (MalformedURLException e) {
			//log
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void play(){
		myClip.play();
	}
	public void loop(){
		myClip.loop();
	}
	public void stop(){
		myClip.stop();
	}

}

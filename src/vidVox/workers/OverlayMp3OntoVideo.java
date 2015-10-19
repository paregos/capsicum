package vidVox.workers;

import java.io.IOException;

import javax.swing.SwingWorker;

import vidVox.guiScreens.MainPlayerScreen;
import vidVox.guiScreens.TextToMp3Screen;

public class OverlayMp3OntoVideo extends SwingWorker<Void, String>{
	//Fields used for my class.
	private String audio;
	private String originalVideo = MainPlayerScreen.mediapath;
	private String filename;
	private Boolean overlay;


	@Override
	protected Void doInBackground() throws Exception {

		//This will overlay MP3 onto video
		TextToMp3Screen.mainPlayerScreen.loadingScreen.setVisible(true);
		String cmd = "ffmpeg -y -i "+"\""+originalVideo+"\""+" -i "+"\""+audio+"\""+" -filter_complex amix -strict -2 \"/tmp/V"+filename+TextToMp3Screen.videoNumber+".mp4"+"\"";
		ProcessBuilder x = new ProcessBuilder("/bin/bash", "-c", cmd );

		try {
			Process process = x.start();
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	//Constructor used for my class.
	public OverlayMp3OntoVideo (String audio, String filename, Boolean overlay) {
		this.audio = audio;
		this.filename = filename;
		this.overlay = overlay;
	}
	//This is code executed when my video has been overlayed. It will run the TexttoMP3 screen.
	protected void done(){
		MainPlayerScreen.mediapath = "/tmp/V"+filename+TextToMp3Screen.videoNumber+".mp4";
		TextToMp3Screen.mainPlayerScreen.run();
		TextToMp3Screen.mainPlayerScreen.loadingScreen.setVisible(false);
		TextToMp3Screen.videoNumber++;
		MainPlayerScreen.createCommentaryScreen.setVisible(false);
	}

}


//ffmpeg -y -i video.mp4 -i audio.mp3 -filter_complex amix -strict -2 out.mp4

//ffmpeg -y -i /afs/ec.auckland.ac.nz/users/b/m/bmit436/unixhome/bunny1.mp4 -i /tmp/hellohoi.mp3 -filter_complex amix -strict -2 /afs/ec.auckland.ac.nz/users/b/m/bmit436/unixhome/bunny1.mp4


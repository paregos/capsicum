package vidivox.workers;


import java.io.IOException;

import javax.swing.SwingWorker;

import vidivox.guiscreens.MainPlayerScreen;
import vidivox.guiscreens.TextToMp3Screen;
import vidivox.guiscreens.panes.ControlsPane;

public class OverlayMp3OntoVideo extends SwingWorker<Void, String>{
	//Fields used for my class.
	private String command;
	private String originalVideo = MainPlayerScreen.getMediapath();
	private String filename;
	private Boolean overlay;


	@Override
	protected Void doInBackground() throws Exception {

		//This bash command will overlay MP3 onto the video that was opened. 
		TextToMp3Screen.getMainPlayerScreen().getLoadingScreen().setVisible(true);
		String cmd = command+"\"/tmp/V"+filename+TextToMp3Screen.getVideoNumber()+".mp4"+"\" -ac 2";
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
	public OverlayMp3OntoVideo (String command, String filename, Boolean overlay) {
		this.command = command;
		this.filename = filename;
		this.overlay = overlay;
	}
	//This is code executed when my video has been overlayed. It will run the TexttoMP3 screen.
	protected void done(){
		MainPlayerScreen.setMediapath("/tmp/V"+filename+TextToMp3Screen.getVideoNumber()+".mp4");
		TextToMp3Screen.getMainPlayerScreen().run();
		TextToMp3Screen.getMainPlayerScreen().getLoadingScreen().setVisible(false);
		TextToMp3Screen.setVideoNumber(TextToMp3Screen.getVideoNumber() + 1);
		MainPlayerScreen.getCreateCommentaryScreen().setVisible(false);
		if(ControlsPane.play.getText() == "play"){
			ControlsPane.play.setText("pause");
		}
	}

}


//ffmpeg -y -i video.mp4 -i audio.mp3 -filter_complex amix -strict -2 out.mp4

//ffmpeg -y -i /afs/ec.auckland.ac.nz/users/b/m/bmit436/unixhome/bunny1.mp4 -i /tmp/hellohoi.mp3 -filter_complex amix -strict -2 /afs/ec.auckland.ac.nz/users/b/m/bmit436/unixhome/bunny1.mp4


package vidVox;

import java.io.IOException;

import javax.swing.SwingWorker;

public class OverlayMp3OntoVideo extends SwingWorker<Void, String>{
	//
	private String audio;
	private String originalVideo = MainPlayerScreen.mediapath;
	private String filename;
	private Boolean overlay;


	@Override
	protected Void doInBackground() throws Exception {

		//creating the bash process which will speak the user high score in festival
		TextToMp3Screen.mainPlayerScreen.loadingScreen.setVisible(true);
		String cmd = "ffmpeg -y -i "+originalVideo+" -i "+audio+" -filter_complex amix -strict -2 /tmp/V"+filename+TextToMp3Screen.videoNumber+".mp4";
		System.out.println(cmd);
		ProcessBuilder x = new ProcessBuilder("/bin/bash", "-c", cmd );

		try {
			Process process = x.start();
			process.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public OverlayMp3OntoVideo (String audio, String filename, Boolean overlay) {
		this.audio = audio;
		this.filename = filename;
		this.overlay = overlay;
	}

	protected void done(){
		System.out.println("we done bb");
		MainPlayerScreen.mediapath = "/tmp/V"+filename+TextToMp3Screen.videoNumber+".mp4";
		System.out.println(MainPlayerScreen.mediapath);
		TextToMp3Screen.mainPlayerScreen.run();
		TextToMp3Screen.mainPlayerScreen.loadingScreen.setVisible(false);
		TextToMp3Screen.videoNumber++;
		MainPlayerScreen.createCommentaryScreen.setVisible(false);
	}

}


//ffmpeg -y -i video.mp4 -i audio.mp3 -filter_complex amix -strict -2 out.mp4

//ffmpeg -y -i /afs/ec.auckland.ac.nz/users/b/m/bmit436/unixhome/bunny1.mp4 -i /tmp/hellohoi.mp3 -filter_complex amix -strict -2 /afs/ec.auckland.ac.nz/users/b/m/bmit436/unixhome/bunny1.mp4


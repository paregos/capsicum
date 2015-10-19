package vidVox;
import java.io.IOException;
import java.lang.reflect.*;
import java.text.DateFormat.Field;
import javax.swing.SwingWorker;

public class WavToMp3 extends SwingWorker<Void, String>{
	//
	//location = location of where the mp3 will be saved
	//filename = what the file is called
	private String location;
	private String filename;
	private Boolean overlay;

	@Override
	protected Void doInBackground() throws Exception {

		//creating the bash process which will change a wav file into a mp3 file and store it in
		//the home directory

		if (!(location.endsWith(".mp3"))){
			location = location+".mp3";
		}

		String cmd = "ffmpeg -y -i /tmp/"+filename+".wav -codec:a libmp3lame -qscale:a 2 "+location;
		System.out.println(cmd);
		System.out.println(overlay);
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

	public WavToMp3 (String location, String filename, Boolean overlay){
		this.location = location;
		this.filename = filename;
		this.overlay = overlay;
	}

	protected void done(){
		//if this mp3 needs to be overlayed
		if (overlay){
			System.out.println("we here");
			OverlayMp3OntoVideo k = new OverlayMp3OntoVideo(this.location, this.filename, this.overlay);
			k.execute();
			// do the overlay 

		}
	}

}

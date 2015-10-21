package vidivox.workers;
import java.io.IOException;
import java.lang.reflect.*;
import java.text.DateFormat.Field;
import javax.swing.SwingWorker;

import vidivox.guiscreens.MainPlayerScreen;
import vidivox.guiscreens.panes.CommentaryPane;

public class WavToMp3 extends SwingWorker<Void, String>{
	//
	//location = location of where the mp3 will be saved
	//filename = what the file is called
	private String location;
	private String text,offset;
	private Boolean overlay;
	private int textNumber;

	@Override
	protected Void doInBackground() throws Exception {

		//creating the bash process which will change a wav file into a mp3 file and store it in
		//the home directory

		if (!(location.endsWith(".mp3"))){
			location = location+".mp3";
		}

		String cmd = "ffmpeg -y -i \"/tmp/iop"+textNumber+".wav\" -codec:a libmp3lame -qscale:a 2 "+"\""+location+"\"";
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

	public WavToMp3 (String location, String text, Boolean overlay, int textNumber, String offset){
		this.location = location;
		this.text = text;
		this.overlay = overlay;
		this.textNumber = textNumber;
		this.offset = offset;
	}

	protected void done(){
		//if this mp3 needs to be overlayed
		if (overlay){
			
			Object[] data = { location, text, "0",
					offset, true };
			CommentaryPane.audioOverlayTable.addRow(data);
			
			// do the overlay 

		}
	}

}

package vidVox.workers;
import java.io.IOException;
import java.lang.reflect.*;
import java.text.DateFormat.Field;
import javax.swing.SwingWorker;

public class TextToFile extends SwingWorker<Void, String>{
	//
	//text = text that needs to be spoken in the mp3
	//location = location of where the mp3 will be saved
	private String text;
	private String filename;
	private String location;
	private Boolean overlay;

	@Override
	protected Void doInBackground() throws Exception {

		text = text.trim();
		filename = text.replaceAll("\\s+","");
		location = location.replaceAll("\\s+","");

		//creating the bash process which will create a wav file from a text file
		String cmd = "echo "+"\""+text+"\""+" > \"/tmp/"+filename+"\"";
		ProcessBuilder x = new ProcessBuilder("/bin/bash", "-c", cmd );

		try {
			Process process = x.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	//text is the text which needs to be spoken
	public TextToFile (String text, String location, Boolean overlay){
		this.text = text;
		this.location = location;
		this.overlay = overlay;
	}
	//This is the done method which will turn my text to a wave file.
	protected void done(){
		TextToWav k = new TextToWav(this.location, this.filename, this.overlay);
		k.execute();

	}

}

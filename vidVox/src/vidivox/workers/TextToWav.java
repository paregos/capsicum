package vidivox.workers;
import java.io.IOException;
import java.lang.reflect.*;
import java.text.DateFormat.Field;
import javax.swing.SwingWorker;

public class TextToWav extends SwingWorker<Void, String>{
	
	//location = location of where the mp3 will be saved
	//filename = what the file is called
	private String location;
	private String text,offset;
	private Boolean overlay;
	private int textNumber;
	
	@Override
	protected Void doInBackground() throws Exception {

		//creating the bash process which will create a wav file from a text file
		//the wav file is named after the text which is spoken in it, and it is stored in /tmp/
		// e.g hello is being spoken, /tmp/hello.wav exists
		String cmd = "text2wave \"/tmp/iop"+textNumber+"\""+" -o \"/tmp/iop"+textNumber+".wav"+"\"";
		System.out.println(cmd);
		ProcessBuilder x = new ProcessBuilder("/bin/bash", "-c", cmd );
				
		try {
			Process process = x.start();
			process.waitFor();
			} catch (IOException e) {
			e.printStackTrace();
			}
		return null;
	}

	//text is the text that needs to be spoken
	public TextToWav (String location, String text, Boolean overlay, int textNumber, String offset){
		this.location = location;
		this.text = text;
		this.overlay = overlay;
		this.textNumber = textNumber;
		this.offset =offset;
	}
	
	protected void done(){
		WavToMp3 k = new WavToMp3(this.location, this.text, this.overlay, this.textNumber, this.offset);
		k.execute();
	}
}

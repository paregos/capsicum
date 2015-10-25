package vidivox.workers.filecreation;
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
	private String location,offset;
	private Boolean overlay;
	private int textNumber;
	int choice;

	@Override
	protected Void doInBackground() throws Exception {

		text = text.trim();
		filename = text.replaceAll("\\s+","");
		location = location.replaceAll("\\s+","");

		//creating the bash process which create a text file with the given text
		String cmd = "echo "+"\""+text+"\""+" > \"/tmp/iop"+textNumber+"\"";
		

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

	//text is the text which needs to be spoken
	public TextToFile (String text, String location, Boolean overlay, int textNumber, String offset, int choice){
		this.text = text;
		this.location = location;
		this.overlay = overlay;
		this.textNumber = textNumber;
		this.offset = offset;
		this.choice = choice;
	}
	//This is the done method which will turn my text to a wave file.
	protected void done(){
		TextToWav k = new TextToWav(this.location, this.text, this.overlay, this.textNumber, this.offset, this.choice);
		k.execute();

	}

}

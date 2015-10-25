package vidivox.workers.filecreation;
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
	private int choice;
	
	@Override
	protected Void doInBackground() throws Exception {

		//creating the bash process which will create a wav file from a text file
		//the wav file is named after the text which is spoken in it, and it is stored in /tmp/
		// e.g hello is being spoken, /tmp/hello.wav exists
		String cmd;
		
		//choosing the voice that the user has selected
		if (choice == 2){
		 cmd = "text2wave \"/tmp/iop"+textNumber+"\""+" -o \"/tmp/iop"+textNumber+".wav"+"\" -eval \"(voice_akl_nz_jdt_diphone)\"";
		}else if (choice == 1){
		 cmd = "text2wave \"/tmp/iop"+textNumber+"\""+" -o \"/tmp/iop"+textNumber+".wav"+"\" -eval \"(voice_rab_diphone)\"";
		}else{
		 cmd = "text2wave \"/tmp/iop"+textNumber+"\""+" -o \"/tmp/iop"+textNumber+".wav"+"\" -eval \"(voice_kal_diphone)\"";
		}
		
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
	public TextToWav (String location, String text, Boolean overlay, int textNumber, String offset, int choice){
		this.location = location;
		this.text = text;
		this.overlay = overlay;
		this.textNumber = textNumber;
		this.offset =offset;
		this.choice = choice;
	}
	
	protected void done(){
		WavToMp3 k = new WavToMp3(this.location, this.text, this.overlay, this.textNumber, this.offset, this.choice);
		k.execute();
	}
}

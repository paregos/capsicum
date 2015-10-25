package vidivox.workers;

import java.io.IOException;
import java.lang.reflect.*;
import java.text.DateFormat.Field;
import javax.swing.SwingWorker;

import vidivox.workers.filecreation.WavToMp3;
//
//uses festival and bash to preview the text entered in the text box when the button is pressed
public class PreviewTextSpeech extends SwingWorker<Void, String>{

	private String text;
	private String path;
	private int choice;

	@Override
	protected Void doInBackground() throws Exception {

		//creating the bash process which will speak the user high score in festival
		String cmd;
		if (choice == 2){
			cmd = "echo \"" + text + "\" | text2wave -o \"" +path+"\"" + " -eval \"(voice_akl_nz_jdt_diphone)\"";
			}else if (choice == 1){
				cmd = "echo \"" + text + "\" | text2wave -o \"" +path+"\"" + " -eval \"(voice_rab_diphone)\"";
			}else{
			 cmd = "echo \"" + text + "\" | text2wave -o \"" +path+"\"" + " -eval \"(voice_kal_diphone)\"";
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
	//Constructor
	public PreviewTextSpeech(String text, int choice, String path){
		this.text = text;
		this.path = path;
		this.choice = choice;
	}

	protected void done(){
		
		PlayPreview k = new PlayPreview(path);
		k.execute();
		
	}
	
}

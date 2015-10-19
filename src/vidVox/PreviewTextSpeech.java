package vidVox;

import java.io.IOException;
import java.lang.reflect.*;
import java.text.DateFormat.Field;
import javax.swing.SwingWorker;
//
//uses festival and bash to preview the text entered in the text box when the button is pressed
public class PreviewTextSpeech extends SwingWorker<Void, String>{

	private String text;

	@Override
	protected Void doInBackground() throws Exception {

		//creating the bash process which will speak the user high score in festival
		String cmd = "echo "+"\""+text+"\""+" | festival --tts";
		ProcessBuilder x = new ProcessBuilder("/bin/bash", "-c", cmd );

		try {
			Process process = x.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public PreviewTextSpeech(String text){
		this.text = text;
	}

}

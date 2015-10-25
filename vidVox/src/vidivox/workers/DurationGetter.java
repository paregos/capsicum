package vidivox.workers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.SwingWorker;

import vidivox.guiscreens.panes.CommentaryPane;

public class DurationGetter extends SwingWorker<Void, String>{

	private String location, duration;
	private String text,offset;
	private Boolean overlay;
	private int textNumber, choice;
	
	public DurationGetter(String location, String text, Boolean overlay, int textNumber, String offset, int choice){
		this.location = location;
		this.text = text;
		this.overlay = overlay;
		this.textNumber = textNumber;
		this.offset = offset;
		this.choice = choice;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
	
		
		String cmd = "ffprobe -i \"" + location + "\" -show_entries format=duration 2>&1 | grep \"duration=\"";
		ProcessBuilder x = new ProcessBuilder("/bin/bash", "-c", cmd );
		System.out.println(cmd);
		
		try {
			Process process = x.start();
			process.waitFor();
			
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
			
			String durationTemp = stdoutBuffered.readLine();
			String[] durationTemps = durationTemp.split("=");
			duration = durationTemps[1];
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	protected void done(){
		
		String voice;
		if (choice == 0){
			voice = "Default";
		} else if (choice == 1){
			voice = "Kiwi Male";
		} else if (choice == 2){
			voice = "European Male";
		} else {
			voice = "Mp3";
		}
		
		Object[] data = { location, text, duration, offset, true, voice };
		CommentaryPane.getAudioOverlayTable().addRow(data);
	
		
	}


}

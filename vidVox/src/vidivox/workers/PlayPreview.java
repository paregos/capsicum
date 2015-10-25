package vidivox.workers;

import java.io.IOException;

import javax.swing.SwingWorker;

public class PlayPreview extends SwingWorker<Void, String>{

	private String path;
	
	@Override
	protected Void doInBackground() throws Exception {

		//creating the bash process which will speak the preview of the commentary entered
		String cmd = "ffplay -nodisp -autoexit -af volume=" + "1.0" + " \"" + path+"\"";
		System.out.println(cmd);
		ProcessBuilder x = new ProcessBuilder("/bin/bash", "-c", cmd );
		
		try {
			Process process = x.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public PlayPreview (String path){
		this.path = path;
		
	}
}


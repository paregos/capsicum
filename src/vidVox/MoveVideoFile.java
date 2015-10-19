package vidVox;

import java.io.IOException;

import javax.swing.SwingWorker;

public class MoveVideoFile extends SwingWorker<Void, String>{

	private String video;
	private String location;

	@Override
	protected Void doInBackground() throws Exception {
		//
		String cmd = "cp "+video+" "+location;
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

	public MoveVideoFile (String video, String location){
		this.video = video;
		this.location = location;
	}

}

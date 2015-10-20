package vidVox.workers;

import java.io.IOException;

import javax.swing.SwingWorker;

public class MoveVideoFile extends SwingWorker<Void, String>{
	//Fields used in my class.
	private String video;
	private String location;

	//Background process which just copies and pastes a file into a certain location specified by the constructor.
	@Override
	protected Void doInBackground() throws Exception {
		//
		String cmd = "cp "+"\""+video+"\""+" "+"\""+location+"\"";
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

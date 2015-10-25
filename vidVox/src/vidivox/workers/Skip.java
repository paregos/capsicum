package vidivox.workers;


import java.util.List;

import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import vidivox.guiscreens.MainPlayerScreen;
import vidivox.guiscreens.PositionSlider;
//
//Skip class using swing worker which is used to allow fast forward and rewinding.
public class Skip extends SwingWorker<Void,Integer>{
	//Fields which are used in my class.
	public boolean skip=false;
	public int skipRate;
	MainPlayerScreen player;
	long time;
	EmbeddedMediaPlayerComponent media;

	//Constructor for the skip class.
	public Skip (EmbeddedMediaPlayerComponent mp, int skiprate, MainPlayerScreen mainplayer){
		media=mp;
		skipRate=skiprate;
		player=mainplayer;
		skip=false;
	}

	@Override
	//Method which will occur in the background.
	protected Void doInBackground() throws Exception {
		while ((skip==true)&&(isCancelled()==false)){
			Thread.sleep(100);
			//Updates the GUI.
			player.updateGUI();	
			float positionValue = player.getSlider().getPositionSlider().getValue() / 1000.0f;
			//This will check for when it is fastforwarding and reaches end of video, it will pause it
			//otherwise it will operate normally or it can rewind when it reaches end of video.
			if((positionValue > 0.99f)&&(skipRate != -1000)) {
				media.getMediaPlayer().pause();
			}else{ 
				publish();
			}

		}
		return null;
	}
	//This will skip in my video.
	protected void process(List<Integer> chunks){
		media.getMediaPlayer().skip(skipRate);


	}

}

package vidivox.guiscreens;

import vidivox.guiscreens.panes.CommentaryPane;
import vidivox.guiscreens.panes.ControlsPane;

public class ButtonManager {

	
		public ButtonManager() {
			
		}
	
	
	//enables all buttons that were previously not able to be pressed
		public void enableButtons(){
			
			//turning on the commentary pane buttons
			CommentaryPane.createCommentary1.setEnabled(true);
			CommentaryPane.mergeCommentary.setEnabled(true);
			CommentaryPane.removeCommentary.setEnabled(true);
			CommentaryPane.addCommentary1.setEnabled(true);
			CommentaryPane.fxMenu.setEnabled(true);
			
			//turning on the video control buttons
			ControlsPane.fastforward.setEnabled(true);
			ControlsPane.rewind.setEnabled(true);
			ControlsPane.play.setEnabled(true);
			ControlsPane.mute.setEnabled(true);
			
		}
	
}

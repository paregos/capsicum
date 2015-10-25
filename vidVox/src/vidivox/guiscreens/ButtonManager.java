package vidivox.guiscreens;

import vidivox.guiscreens.panes.CommentaryPane;
import vidivox.guiscreens.panes.ControlsPane;

public class ButtonManager {

	//enables all buttons that were previously not able to be pressed
		public void enableButtons(){
			
			//turning on the commentary pane buttons
			CommentaryPane.getCreateCommentary1().setEnabled(true);
			CommentaryPane.getMergeCommentary().setEnabled(true);
			CommentaryPane.getRemoveCommentary().setEnabled(true);
			CommentaryPane.getAddCommentary1().setEnabled(true);
			CommentaryPane.getFxMenu().setEnabled(true);
			
			//turning on the video control buttons
			ControlsPane.fastforward.setEnabled(true);
			ControlsPane.rewind.setEnabled(true);
			ControlsPane.play.setEnabled(true);
			ControlsPane.mute.setEnabled(true);
			
		}
}

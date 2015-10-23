package vidivox;

import java.awt.Dimension;

import vidivox.guiscreens.MainPlayerScreen;
import vidivox.guiscreens.TextToMp3Screen;

public class Main {

	
	
	/**
	 * Main Method used to start my application.
	 */
	public static void main(String[] args) {
		// Initialising all the screens which will be used in the video player.
		MainPlayerScreen frame = new MainPlayerScreen();
		frame.setBounds(125, 200, 1300, 610);
		frame.setMinimumSize(new Dimension(1300, 610));
		frame.setVisible(true);
		frame.createCommentaryScreen = new TextToMp3Screen(frame);
		frame.createCommentaryScreen.setBounds(385, 475, 650, 150);
		frame.createCommentaryScreen.setMinimumSize(new Dimension(650, 150));
		frame.loadingScreen.setBounds(510, 495, 400, 60);
		frame.loadingScreen.setMinimumSize(new Dimension(400, 60));
	}
	
	
}

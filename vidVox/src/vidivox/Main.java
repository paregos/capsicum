package vidivox;

import java.awt.Dimension;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import vidivox.guiscreens.MainPlayerScreen;
import vidivox.guiscreens.TextToMp3Screen;

public class Main {

	
	
	/**
	 * Main Method used to start my application.
	 */
	public static void main(String[] args) {
		// Initialising all the screens which will be used in the video player.
		
		
		//changing the ui of the main player and gui screens
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				UIManager.put("Slider.paintValue", false);
			} catch (Exception e) {
				e.printStackTrace();
			}
	
		//creating the frames that will be used throughout the program
		MainPlayerScreen frame = new MainPlayerScreen();
		frame.setBounds(125, 200, 1370, 610);
		frame.setMinimumSize(new Dimension(1370, 610));
		frame.setVisible(true);
		frame.setCreateCommentaryScreen(new TextToMp3Screen(frame));
		frame.getCreateCommentaryScreen().setBounds(385, 475, 650, 165);
		frame.getCreateCommentaryScreen().setMinimumSize(new Dimension(650, 165));
		frame.getLoadingScreen().setBounds(510, 495, 400, 60);
		frame.getLoadingScreen().setMinimumSize(new Dimension(400, 60));
	}
	
	
}

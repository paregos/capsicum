package vidivox.guiscreens.panes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import vidivox.guiscreens.MainPlayerScreen;
import vidivox.guiscreens.TextToMp3Screen;
import vidivox.inputoutput.OpenVideo;
import vidivox.inputoutput.SaveVideoAs;
import vidivox.workers.MoveVideoFile;

public class VideoMenuBar {

	// Menu at the top which allows users to select their appropriate options.
	
	JMenu video;
	JMenuItem openVideo, saveVideo, saveVideoAs;
	MainPlayerScreen mainScreen;
	
	public VideoMenuBar(MainPlayerScreen mainScreen){
		this.mainScreen = mainScreen;
	}
	
	public void setMenu(JMenuBar menuBar){
	// adds a menu to the menu bar
			video = new JMenu("Video");
			menuBar.add(video);

			// open video button
			openVideo = new JMenuItem("Open Video...");
			video.add(openVideo);

			// save video button
			saveVideo = new JMenuItem("Save Video...");
			video.add(saveVideo);

			// save video as button
			saveVideoAs = new JMenuItem("Save Video as...");
			video.add(saveVideoAs);
			
			setUpListeners();
	}
	
	public void setUpListeners() {
	// This will allow you to choose a file and play it.
			openVideo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Pauses the current video being played if any.
					MainPlayerScreen.mediaPlayerComponent.getMediaPlayer().setPause(true);
					// Check if the user grabbed a file.
					boolean openfile = OpenVideo.grabFile(mainScreen);
					if (openfile) {
						mainScreen.mediapath = OpenVideo.mediaPath;
						if (mainScreen.ff == true) {
							mainScreen.ffswing.cancel(true);
						}
						if (mainScreen.rw == true) {
							mainScreen.rwswing.cancel(true);
						}
						mainScreen.ff = false;
						mainScreen.rw = false;
						ControlsPane.play.setText("pause");
						mainScreen.run();
					}
					if (ControlsPane.play.getText().equals("pause")) {
						// If user decided to cancel the operation, it will continue
						// playing the video if it is being played.
						mainScreen.mediaPlayerComponent.getMediaPlayer().play();
					}
				}
			});

			// Allows the user to save video.
			saveVideo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (MainPlayerScreen.mediapath == null) {
						JOptionPane.showMessageDialog(null,
								"Error please open a video before trying to save.");
					} else {
						MoveVideoFile k = new MoveVideoFile(mainScreen.mediapath,
								TextToMp3Screen.originalVideo);
						k.execute();
					}
				}
			});

			// Allows the user to save Video as.
			saveVideoAs.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (MainPlayerScreen.mediapath == null) {
						JOptionPane.showMessageDialog(null,
								"Error please open a video before trying to save.");
					} else {
					// Uses the method saveVideoAs which will allow you to save the
					// video into a location the user wants.
					SaveVideoAs.saveVideoAs();
					}
				}
			});
	}
}

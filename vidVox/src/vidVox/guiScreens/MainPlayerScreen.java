package vidVox.guiScreens;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.table.DefaultTableModel;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import vidVox.OpenVideo;
import vidVox.SaveVideoAs;
import vidVox.guiScreens.panes.CommentaryPane;
import vidVox.guiScreens.panes.ControlsPane;
import vidVox.guiScreens.panes.EffectsPane;
import vidVox.workers.MoveVideoFile;
import vidVox.workers.OverlayMp3OntoVideo;
import vidVox.workers.Skip;

public class MainPlayerScreen extends JFrame {
	// Fields which are used within this class and package.
	MainPlayerScreen mainplayer = this;
	public static JPanel topPane, bottomPane,leftPane, effectsPane, rightPane;
	public static EmbeddedMediaPlayerComponent mediaPlayerComponent;
	public static Skip ffswing, rwswing;
	public static String mediapath;
	public static LoadingScreen loadingScreen = new LoadingScreen();
	public static AddCommentaryScreen addCommentaryScreen;
	private boolean pressedWhilePlaying = false;
	public static TextToMp3Screen createCommentaryScreen;
	public static int currentVolume;
	public static boolean ff = false, rw = false, refresh = false;
	public long totaltime;
	private ChangeListener listener;
	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

	// Set up the GridBag Layout for my screen.
	GridBagLayout gbl_topPane;
	GridBagLayout gbl_leftPane;
	GridBagLayout gbl_bottomPane;
	GridBagLayout gbl_rightPane;
	GridBagLayout gbl_effects;
	GridBagConstraints c;

	// Buttons, sliders and labels which are used in my GUI for users to click.

	public JSlider positionSlider;
	public static JLabel endLabel;
	public static JLabel timeLabel = new JLabel("00:00:00");
	

	// Menu at the top which allows users to select their appropriate options.
	JMenuBar menuBar;
	JMenu video, audio;
	JMenuItem openVideo, saveVideo, saveVideoAs, addCommentary,
			createCommentary;

	/**
	 * Main Method used to start my application. Also used some code from
	 * https:/
	 * /github.com/caprica/vlcj/blob/master/src/test/java/uk/co/caprica/vlcj
	 * /test/basic/PlayerControlsPanel.java for additional features.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Initialising all the screens which will be used in the video player.
		MainPlayerScreen frame = new MainPlayerScreen();
		frame.setBounds(125, 200, 1400, 610);
		frame.setMinimumSize(new Dimension(1400, 610));
		frame.setVisible(true);
		createCommentaryScreen = new TextToMp3Screen(frame);
		createCommentaryScreen.setBounds(385, 475, 650, 100);
		createCommentaryScreen.setMinimumSize(new Dimension(650, 100));
		loadingScreen.setBounds(510, 495, 400, 60);
		loadingScreen.setMinimumSize(new Dimension(400, 60));
		addCommentaryScreen = new AddCommentaryScreen();
		addCommentaryScreen.setBounds(285, 475, 850, 100);
		addCommentaryScreen.setMinimumSize(new Dimension(650, 100));
	}

	/**
	 * Constructor for my class.
	 */
	public MainPlayerScreen() {

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (!(mediapath == TextToMp3Screen.originalVideo)) {
					Object[] options = { "Save", "Save as..",
							"Exit without saving" };
					int choice = JOptionPane.showOptionDialog(null,
							"Save changes to your video before closing?",
							"Warning video has changes which are not saved",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[0]);
					if (choice == 0) {
						if (MainPlayerScreen.mediapath == null) {
							JOptionPane
									.showMessageDialog(null,
											"error please open a video before trying to save");
						} else {
							MoveVideoFile k = new MoveVideoFile(mediapath,
									TextToMp3Screen.originalVideo);
							k.execute();
						}
					} else if (choice == 1) {
						// save video as
						SaveVideoAs.saveVideoAs();
					} else {
						// closing without saving
					}
				}
			}
		});

		// making the main initial layout
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 820, 490);
		c = new GridBagConstraints();
		// Runs the methods in this class which establish the buttons and their
		// appropriate action.
		setUpLayout();
		setUpListeners();
	}

	// This will run the video from the media path provided.
	public void run() {
		// This will run the video.
		mediaPlayerComponent.getMediaPlayer().playMedia(mediapath);
		// This will get the current time of the video.
		final long time = mediaPlayerComponent.getMediaPlayer().getTime();
		// This will get the position of the video.
		final int position = (int) (mediaPlayerComponent.getMediaPlayer()
				.getPosition() * 1000.0f);
		// This will get the total length of the video.
		final long duration = mediaPlayerComponent.getMediaPlayer().getLength();

		// This will be used to update the GUI.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (mediaPlayerComponent.getMediaPlayer().isPlaying()) {
					updateTime(time);
					updatePosition(position);
					updateDuration(duration);
				}
			}
		});
		// This will execute the code at a fixed rate.
		executorService.scheduleAtFixedRate(new UpdateRunnable(
				mediaPlayerComponent), 0, 100, TimeUnit.MILLISECONDS);
	}

	// This will get the total length of the video.
	public void updateDuration(long millis) {
		String s = String.format(
				"%02d:%02d:%02d",
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
								.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(millis)));
		endLabel.setText(s);
	}

	// This class will set the video based on the position of the slider.
	void setSliderBasedPosition() {
		// Check if it is playable.
		if (!mediaPlayerComponent.getMediaPlayer().isSeekable()) {
			return;
		}

		float positionValue = positionSlider.getValue() / 1000.0f;
		// Makes sure it wont freeze.
		if (positionValue > 0.99f) {
			positionValue = 0.99f;
		}
		// Set position of position slider.
		mediaPlayerComponent.getMediaPlayer().setPosition(positionValue);
	}

	// Update the UI as it plays.
	public void updateGUI() {
		if (!mediaPlayerComponent.getMediaPlayer().isPlaying()) {
			if (!pressedWhilePlaying) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// Don't care if unblocked early
				}
				// Pauses the video to make sure it is paused if dragged while
				// paused during position slider.
				mediaPlayerComponent.getMediaPlayer().setPause(true);
			}
		}
		// Gets the current time and position and updates them in the GUI.
		long time = mediaPlayerComponent.getMediaPlayer().getTime();
		int position = (int) (mediaPlayerComponent.getMediaPlayer()
				.getPosition() * 1000.0f);
		updateTime(time);
		updatePosition(position);
	}

	// This function will update the time based on where it is in the video.
	private void updateTime(long millis) {
		String s = String.format(
				"%02d:%02d:%02d",
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
								.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(millis)));
		timeLabel.setText(s);
		CommentaryPane.addCommentary1.setText("Select Mp3 Commentary to add at " + s);
	}

	// Updates the position of the position slider based on value given.
	private void updatePosition(int value) {
		positionSlider.setValue(value);
	}

	// Class which is used to update the GUI.
	private final class UpdateRunnable implements Runnable {

		private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

		private UpdateRunnable(EmbeddedMediaPlayerComponent mediaPlayer) {
			this.mediaPlayerComponent = mediaPlayer;
		}

		// This is a method which will take in the current time, position and
		// length of the video and updates them.
		@Override
		public void run() {
			final long time = mediaPlayerComponent.getMediaPlayer().getTime();
			final int position = (int) (mediaPlayerComponent.getMediaPlayer()
					.getPosition() * 1000.0f);
			final long duration = mediaPlayerComponent.getMediaPlayer()
					.getLength();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (mediaPlayerComponent.getMediaPlayer().isPlaying()) {
						// Updates time position and duration.
						updateTime(time);
						updatePosition(position);
						updateDuration(duration);
					}
				}
			});
		}
	}

	private void setUpLayout() {
		// creating the content pane which will store all of the video
		// components
		gbl_topPane = new GridBagLayout();
		topPane = new JPanel(gbl_topPane);
		// contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(topPane);

		// creating the content pane which will store all of the control components
		gbl_bottomPane = new GridBagLayout();
		bottomPane = new ControlsPane(this);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 4;
		c.insets = new Insets(0, 0, 5, 0);
		c.anchor = GridBagConstraints.SOUTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		topPane.add(bottomPane, c);

		// creating the content pane which will store all of the control
		gbl_rightPane = new GridBagLayout();
		rightPane = new CommentaryPane();
		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.gridheight = 3;
		c.gridwidth = 4;
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.BOTH;
		topPane.add(rightPane, c);

		// creating the effects pane which will store all of the effects
		gbl_effects = new GridBagLayout();
		effectsPane = new EffectsPane(mediaPlayerComponent);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 1;
		c.weighty = 1;
		c.gridheight = 1;
		c.gridwidth = 8;
		c.fill = GridBagConstraints.HORIZONTAL;
		topPane.add(effectsPane, c);
		effectsPane.setVisible(false);

		// creating the content pane which will store all of the control
		gbl_leftPane = new GridBagLayout();
		leftPane = new JPanel(gbl_leftPane);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 99;
		c.gridwidth = 4;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		topPane.add(leftPane, c);

		// Adding a Jlabel which will be the starting time of the video
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(0, 10, 10, 10);
		topPane.add(timeLabel, c);

		// Adding a Jlabel which will be the ending time of the video
		endLabel = new JLabel("00:00:00");
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(0, 10, 10, 10);
		topPane.add(endLabel, c);

		// Adding the position Slider which will change as the video progresses
		positionSlider = new JSlider();
		positionSlider.setMinimum(0);
		positionSlider.setMaximum(1000);
		positionSlider.setValue(0);
		// positionSlider.setToolTipText("Position");
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 10, 10, 10);
		c.fill = GridBagConstraints.HORIZONTAL;
		topPane.add(positionSlider, c);

		// Creates a Menu bar for the frame
		menuBar = new JMenuBar();
		c = new GridBagConstraints();
		setJMenuBar(menuBar);
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);

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

		// audio menu tab
		audio = new JMenu("Audio");
		menuBar.add(audio);

		// create commentary button
		createCommentary = new JMenuItem("Create Commentary");
		audio.add(createCommentary);

		// add commentary button
		addCommentary = new JMenuItem("Add Commentary");
		audio.add(addCommentary);

		// Adding in the video area where a mp4 can be played
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		//mediaPlayerComponent.setPreferredSize(new Dimension(1300, 480));
		ff = false;
		rw = false;
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 10, 10);
		leftPane.add(mediaPlayerComponent, c);

	}

	public void setUpListeners() {
		
		// This will allow you to choose a file and play it.
		openVideo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Pauses the current video being played if any.
				mediaPlayerComponent.getMediaPlayer().setPause(true);
				// Check if the user grabbed a file.
				boolean openfile = OpenVideo.grabFile();
				if (openfile) {
					mediapath = OpenVideo.mediaPath;
					if (ff == true) {
						ffswing.cancel(true);
					}
					if (rw == true) {
						rwswing.cancel(true);
					}
					ff = false;
					rw = false;
					ControlsPane.play.setText("pause");
					run();
				}
				if (ControlsPane.play.getText().equals("pause")) {
					// If user decided to cancel the operation, it will continue
					// playing the video if it is being played.
					mediaPlayerComponent.getMediaPlayer().play();
				}
			}
		});

		// Allows the user to create commentary.
		createCommentary.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				createCommentaryScreen.setVisible(true);
			}
		});

		// Allows the user to add commentary.
		addCommentary.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (MainPlayerScreen.mediapath == null) {
					JOptionPane
							.showMessageDialog(null,
									"Error please open a video before trying to add commentary");
				} else {
					addCommentaryScreen.setVisible(true);
				}
			}
		});

		// Allows the user to save video.
		saveVideo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (MainPlayerScreen.mediapath == null) {
					JOptionPane.showMessageDialog(null,
							"Error please open a video before trying to sav e.");
				} else {
					MoveVideoFile k = new MoveVideoFile(mediapath,
							TextToMp3Screen.originalVideo);
					k.execute();
				}
			}
		});

		// Allows the user to save Video as.
		saveVideoAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Uses the method saveVideoAs which will allow you to save the
				// video into a location the user wants.
				SaveVideoAs.saveVideoAs();
			}
		});

		// This is a change listener used to look at the changes when the video
		// is being played and mainly used to
		// update the the time of the video when the position slider is being
		// dragged.
		listener = new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				if (refresh) {
					setSliderBasedPosition();
					updateGUI();
				}
			}
		};
		positionSlider.addChangeListener(listener);
		positionSlider.addMouseListener(new MouseAdapter() {
			// Checks for when the mouse is being pressed and updates the
			// position and time appropriately.
			@Override
			public void mousePressed(MouseEvent e) {
				refresh = true;
				if (mediaPlayerComponent.getMediaPlayer().isPlaying()) {
					pressedWhilePlaying = true;
				} else {
					pressedWhilePlaying = false;
				}
				// This will allow you to go to a certain point in the video by
				// clicking on the position slider and then updates
				// the video based on where you clicked.
				JSlider sourceSlider = (JSlider) e.getSource();
				BasicSliderUI ui = (BasicSliderUI) sourceSlider.getUI();
				int value = ui.valueForXPosition(e.getX());
				positionSlider.setValue(value);
				setSliderBasedPosition();
			}

			// Checks for when the mouse has been released and gets the point
			// where it has been released.
			@Override
			public void mouseReleased(MouseEvent e) {
				// Updates the slider and the time.
				refresh = false;
				setSliderBasedPosition();
				updateGUI();
			}
		});
		// Checks for when the video is finished and if it is, it will stop the
		// video and user will need to play video again.
		mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(
				new MediaPlayerEventAdapter() {
					@Override
					public void finished(MediaPlayer mediaPlayer) {
						mediaPlayerComponent.getMediaPlayer().stop();
						ControlsPane.play.setText("play");
					}
				});

	}
}
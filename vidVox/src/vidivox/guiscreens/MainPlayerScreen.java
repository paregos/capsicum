package vidivox.guiscreens;

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
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.table.DefaultTableModel;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import vidivox.guiscreens.panes.CommentaryPane;
import vidivox.guiscreens.panes.ControlsPane;
import vidivox.guiscreens.panes.EffectsPane;
import vidivox.guiscreens.panes.VideoMenuBar;
import vidivox.inputoutput.OpenVideo;
import vidivox.inputoutput.SaveVideoAs;
import vidivox.workers.MoveVideoFile;
import vidivox.workers.OverlayMp3OntoVideo;
import vidivox.workers.Skip;

public class MainPlayerScreen extends JFrame {
	// Fields which are used within this class and package.
	private static JPanel topPane, bottomPane,leftPane, effectsPane, rightPane;
	public static EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private static Skip ffswing, rwswing;
	private static String mediapath;
	private PositionSlider slider;
	private static LoadingScreen loadingScreen = new LoadingScreen();
	private boolean pressedWhilePlaying = false;
	private static TextToMp3Screen createCommentaryScreen;
	private static int currentVolume;
	private static boolean ff = false, rw = false;
	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

	// Set up the GridBag Layout for my screen.
	private GridBagLayout gbl_topPane, gbl_effects, gbl_leftPane, gbl_bottomPane, gbl_rightPane;
	private GridBagConstraints c;

	// Buttons, sliders and labels which are used in my GUI for users to click.
	private static JLabel endLabel, timeLabel = new JLabel("00:00:00");
	
	// Menu at the top which allows users to select their appropriate options.
	private JMenuBar menuBar;

	/*
	 *  The following methods used a number of segments of code from the following for additional features.
	 * https://github.com/caprica/vlcj/blob/master/src/test/java/uk/co/caprica/vlcj/test/basic/PlayerControlsPanel.java
	 */
	
	/**
	 * Constructor for the main gui screen.
	 */
	public MainPlayerScreen() {
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (!(getMediapath() == TextToMp3Screen.originalVideo)) {
					Object[] options = { "Save", "Save as..","Exit without saving" };
					int choice = JOptionPane.showOptionDialog(null,"Save changes to your video before closing?","Warning video has changes which are not saved",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null, options,options[0]);
					if (choice == 0) {
						if (MainPlayerScreen.getMediapath() == null) {
							JOptionPane.showMessageDialog(null,"error please open a video before trying to save");
						} else {
							MoveVideoFile k = new MoveVideoFile(getMediapath(),TextToMp3Screen.originalVideo);
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
		setBounds(100, 100, 1370, 610);
		c = new GridBagConstraints();
		// Runs the methods in this class which establish the buttons and their
		// appropriate action.
		setUpLayout();
		setUpListeners();
		
	}

	// This will run the video from the media path provided.
	public void run() {
		// This will run the video.
		mediaPlayerComponent.getMediaPlayer().playMedia(getMediapath());
		// This will get the current time of the video.
		final long time = mediaPlayerComponent.getMediaPlayer().getTime();
		// This will get the position of the video.
		final int position = (int) (mediaPlayerComponent.getMediaPlayer()
				.getPosition() * 1000.0f);
		// This will get the total length of the video.
		final long duration = mediaPlayerComponent.getMediaPlayer().getLength();

		System.out.println("hello");
		
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

	// Update the UI as it plays.
	public void updateGUI() {
		if (!mediaPlayerComponent.getMediaPlayer().isPlaying()) {
			if (!isPressedWhilePlaying()) {
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
		int position = (int) (mediaPlayerComponent.getMediaPlayer().getPosition() * 1000.0f);
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
		getTimeLabel().setText(s);
		CommentaryPane.getAddCommentary1().setText("Select Mp3 Commentary to add at " + s);
	}

	// Updates the position of the position slider based on value given.
	private void updatePosition(int value) {
		slider.getPositionSlider().setValue(value);
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
		// creating the content pane which will store all of the videocomponents
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
		c.weightx = 10;
		c.weighty = 99;
		c.gridwidth = 4;
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
		topPane.add(getTimeLabel(), c);

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
		slider = new PositionSlider(topPane, this);		

		// Creates a Menu bar for the frame
		menuBar = new JMenuBar();
		c = new GridBagConstraints();
		setJMenuBar(menuBar);
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		VideoMenuBar videomenu = new VideoMenuBar(this);
		videomenu.setMenu(menuBar);

		// Adding in the video area where a mp4 can be played
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		mediaPlayerComponent.setPreferredSize(new Dimension(600, 480));
		setFf(false);
		setRw(false);
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

		// Checks for when the video is finished and if it is, it will stop the
		// video and user will need to play video again.
		mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(
				new MediaPlayerEventAdapter() {
					@Override
					public void finished(MediaPlayer mediaPlayer) {
						mediaPlayerComponent.getMediaPlayer().stop();
						ControlsPane.play.setText("Play");
					}
				});
	}

	public static JPanel getEffectsPane() {return effectsPane;}

	public static Skip getFfswing() {return ffswing;}

	public static void setFfswing(Skip ffswing) {MainPlayerScreen.ffswing = ffswing;}

	public static Skip getRwswing() {return rwswing;}

	public static void setRwswing(Skip rwswing) {MainPlayerScreen.rwswing = rwswing;}

	public static String getMediapath() {return mediapath;}

	public static void setMediapath(String mediapath) {MainPlayerScreen.mediapath = mediapath;}

	public static LoadingScreen getLoadingScreen() {return loadingScreen;}

	public static TextToMp3Screen getCreateCommentaryScreen() {return createCommentaryScreen;}

	public static void setCreateCommentaryScreen(TextToMp3Screen createCommentaryScreen) {MainPlayerScreen.createCommentaryScreen = createCommentaryScreen;}

	public static int getCurrentVolume() {return currentVolume;}

	public static void setCurrentVolume(int currentVolume) {MainPlayerScreen.currentVolume = currentVolume;}

	public static boolean isRw() {return rw;}

	public static void setRw(boolean rw) {MainPlayerScreen.rw = rw;}

	public static boolean isFf() {return ff;}

	public static void setFf(boolean ff) {MainPlayerScreen.ff = ff;}

	public static JLabel getTimeLabel() {return timeLabel;}

	public boolean isPressedWhilePlaying() {return pressedWhilePlaying;}

	public void setPressedWhilePlaying(boolean pressedWhilePlaying) {this.pressedWhilePlaying = pressedWhilePlaying;}

	public PositionSlider getSlider() {return slider;}
	
}
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
import vidVox.MoveVideoFile;
import vidVox.OpenVideo;
import vidVox.SaveVideoAs;
import vidVox.workers.OverlayMp3OntoVideo;
import vidVox.workers.Skip;


public class MainPlayerScreen extends JFrame {
	// Fields which are used within this class and package.
	MainPlayerScreen mainplayer = this;
	private JPanel topPane, bottomPane, rightPane, leftPane;
	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	Skip ffswing, rwswing;
	public static String mediapath;
	public static LoadingScreen loadingScreen = new LoadingScreen();
	public static AddCommentaryScreen addCommentaryScreen;
	private boolean pressedWhilePlaying = false;
	public static TextToMp3Screen createCommentaryScreen;
	private int currentVolume;
	private boolean ff = false, rw = false, refresh = false;
	public long totaltime;
	private ChangeListener listener;
	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	DefaultTableModel audioOverlayTable = null;

	// Set up the GridBag Layout for my screen.
	GridBagLayout gbl_topPane;
	GridBagLayout gbl_leftPane;
	GridBagLayout gbl_bottomPane;
	GridBagLayout gbl_rightPane;
	GridBagConstraints c;

	// Buttons, sliders and labels which are used in my GUI for users to click.
	JButton fastforward, rewind, mute, play, createCommentary1, addCommentary1, mergeCommentary;
	JSlider volume;
	public JSlider positionSlider;
	JLabel volumeLabel, timeLabel, endLabel;
	public JTable table2;

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
		frame.setBounds(300, 200, 1300, 610);
		frame.setMinimumSize(new Dimension(1300, 610));
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
		addCommentary1.setText("Select Mp3 Commentary to add at "+s);
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

		// creating the content pane which will store all of the control
		// components
		gbl_bottomPane = new GridBagLayout();
		bottomPane = new JPanel(gbl_bottomPane);
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
		// components
		gbl_rightPane = new GridBagLayout();
		rightPane = new JPanel(gbl_rightPane);
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.gridheight = 2;
		c.gridwidth = 4;
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.BOTH;
		topPane.add(rightPane, c);

		// creating the content pane which will store all of the control
		// components
		gbl_leftPane = new GridBagLayout();
		leftPane = new JPanel(gbl_leftPane);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 99;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		topPane.add(leftPane, c);

		// Adding a Jlabel which will be the starting time of the video
		timeLabel = new JLabel("00:00:00");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(0, 10, 10, 10);
		// c.fill = GridBagConstraints.HORIZONTAL;
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
		// c.fill = GridBagConstraints.HORIZONTAL;
		topPane.add(endLabel, c);

		// JButton which Creates Commentary
		createCommentary1 = new JButton("Create Commentary");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(10, 10, 0, 10);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		rightPane.add(createCommentary1, c);

		// JButton which add selected Commentary to the video
		mergeCommentary = new JButton("Merge selected commentary to video");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(0, 10, 5, 10);
		c.anchor = GridBagConstraints.SOUTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		rightPane.add(mergeCommentary, c);

		// JButton which adds Commentary to the list
		addCommentary1 = new JButton("Select Mp3 Commentary to add at "+timeLabel.getText());
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(0, 10, 5, 10);
		c.anchor = GridBagConstraints.SOUTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		rightPane.add(addCommentary1, c);

		// Creating a table which will hold all of the information relating to
		// commentaries being added
		String[] audioOverlayOptions = { "Commentary File Name", "Duration",
				"Time inserted", "Include?" };
		audioOverlayTable = new DefaultTableModel(audioOverlayOptions, 0) {
			// Code from
			// http://stackoverflow.com/questions/18099717/how-to-add-jcheckbox-in-jtable
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				Class columnType = String.class;
				switch (columnIndex) {
				case 0:
					columnType = String.class;
					break;
				case 1:
					columnType = String.class;
					break;
				case 2:
					columnType = String.class;
					break;
				case 3:
					columnType = Boolean.class;
					break;
				}
				return columnType;
			}
		};
		table2 = new JTable(audioOverlayTable);
		table2.setModel(audioOverlayTable);


		JScrollPane scrollPane = new JScrollPane();
		// scrollPane.setBounds(20, 75, 400, 400);
		scrollPane.setViewportView(table2);
		// scrollPane.setMinimumSize( scrollPane.getPreferredSize() );
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 4;
		c.gridheight = 3;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(15, 10, 5, 10);
		c.fill = GridBagConstraints.BOTH;
		rightPane.add(scrollPane, c);

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
		mediaPlayerComponent.setPreferredSize(new Dimension(600, 480));
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

		// JButton which fast forwards through the video
		fastforward = new JButton(">>");
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 1;
		c.insets = new Insets(0, 5, 0, 10);
		// c.anchor = GridBagConstraints.EAST;
		bottomPane.add(fastforward, c);

		// JButton which fast forwards through the video
		rewind = new JButton("<<");
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 1;
		c.insets = new Insets(0, 10, 0, 5);
		// c.anchor = GridBagConstraints.WEST;
		bottomPane.add(rewind, c);

		// JButton which Plays the video
		play = new JButton("pause");
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 1;
		c.insets = new Insets(0, 5, 0, 5);
		bottomPane.add(play, c);

		// Volume label
		volumeLabel = new JLabel("Volume");
		c = new GridBagConstraints();
		c.gridx = 5;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 1;
		c.insets = new Insets(0, 20, 0, 10);
		c.anchor = GridBagConstraints.WEST;
		bottomPane.add(volumeLabel, c);

		// JButton which mutes the audio of the video
		mute = new JButton("Mute");
		c = new GridBagConstraints();
		c.gridx = 7;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 1;
		c.insets = new Insets(0, 10, 0, 10);
		c.anchor = GridBagConstraints.EAST;
		bottomPane.add(mute, c);

		// JSlider which controls the volume of the video
		volume = new JSlider();
		c = new GridBagConstraints();
		c.gridx = 6;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 1;
		c.insets = new Insets(0, 10, 0, 10);
		bottomPane.add(volume, c);

		// adding empty jlabels to the control panel to space out the buttons
		// nicely
		JLabel one = new JLabel();
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		bottomPane.add(one, c);
		JLabel two = new JLabel();
		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		bottomPane.add(two, c);
		
//		JLabel three = new JLabel();
//		c = new GridBagConstraints();
//		c.gridx = 3;
//		c.gridy = 1;
//		c.weightx = 1;
//		c.weighty = 1;
//		rightPane.add(three, c);
//		JLabel four = new JLabel();
//		c = new GridBagConstraints();
//		c.gridx = 5;
//		c.gridy = 1;
//		c.weightx = 1;
//		c.weighty = 1;
//		topPane.add(four, c);
//		JLabel five = new JLabel();
//		c = new GridBagConstraints();
//		c.gridx = 4;
//		c.gridy = 1;
//		c.weightx = 1;
//		c.weighty = 1;
//		topPane.add(five, c);
	}

	public void setUpListeners() {
		// Adds an action listener when the play button is clicked.
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Check if fast forwarding or rewind is on when the play/pause
				// button is clicked
				// and will cancel it.
				if (ff == true) {
					ffswing.cancel(true);
					ff = false;
				}
				if (rw == true) {
					rwswing.cancel(true);
					rw = false;
				}
				// Pauses or plays the video depending if it is playing or
				// paused respectively and also
				// changes the text of the button to add a more user friendly
				// experience.
				if (play.getText().equals("play")) {
					play.setText("pause");
					mediaPlayerComponent.getMediaPlayer().play();

				} else {
					play.setText("play");
					mediaPlayerComponent.getMediaPlayer().setPause(true);
				}
			}
		});
		// Added action listener for the rewind(<<) button when it is clicked.
		rewind.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Press rewind button while fastforwarding and not rewinding
				// will result in fast
				// forward being canceled and rewind being used.
				if ((ff == true) && (rw == false)) {
					rwswing = new Skip(mediaPlayerComponent, -1000, mainplayer);
					ffswing.cancel(true);
					rwswing.skip = true;
					rwswing.execute();
					rw = true;
					ff = false;
					// Press rewind button while not rewinding or fastforwarding
					// will result in rewind
					// just being executed.
				} else if ((rw == false) && (ff == false)) {
					rwswing = new Skip(mediaPlayerComponent, -1000, mainplayer);
					rwswing.skip = true;
					rwswing.execute();
					rw = true;
					// Press rewind button while rewinding and not fast
					// forwarding will cause rewind
					// to be canceled.
				} else if ((rw == true) && (ff == false)) {
					rwswing.cancel(true);
					rw = false;
					// Will pause the component if rewind is canceled and it was
					// paused when rewinding.
					if (play.getText().equals("play")) {
						mediaPlayerComponent.getMediaPlayer().setPause(true);
					}
					// Last case where you press the rewind button whilst its
					// rewinding and fastforwarding
					// which cant occur but act as a backup code in base of
					// bugs.
				} else {
					rwswing.cancel(true);
					rw = false;
					ffswing.cancel(true);
					ff = false;
					// Sets the play button to an appropriate button.
					if (play.getText().equals("play")) {
						mediaPlayerComponent.getMediaPlayer().setPause(true);
					}
				}
			}
		});
		// Added action listener for the fastforward (>>) button when it is
		// clicked.
		fastforward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Checks for when fastforward button is clicked when
				// fastforward is off and rewind
				// is on. It will cause the rewind function to cancel and
				// execute the fastforward.
				if ((ff == false) && (rw == true)) {
					ffswing = new Skip(mediaPlayerComponent, 1000, mainplayer);
					rwswing.cancel(true);
					ffswing.skip = true;
					ffswing.execute();
					ff = true;
					rw = false;
					// Checks whether fastforward button is clicked when
					// fastforward is off and rewind
					// is off. It will cause fastforward to execute.
				} else if ((ff == false) && (rw == false)) {
					ffswing = new Skip(mediaPlayerComponent, 1000, mainplayer);
					ffswing.skip = true;
					ffswing.execute();
					ff = true;
					// Checks whether fastforward button is clicked when
					// fastforward is on and rewind
					// is off. It will cause fastforward to stop.
				} else if ((ff == true) && (rw == false)) {
					// ffswing.skip=false;
					ffswing.cancel(true);
					ff = false;
					if (play.getText().equals("play")) {
						mediaPlayerComponent.getMediaPlayer().setPause(true);
					}
					// Last case where you press the fastforward button whilst
					// its rewinding and fastforwarding
					// which cant occur but act as a backup code in base of
					// bugs.
				} else {
					rwswing.cancel(true);
					rw = false;
					ffswing.cancel(true);
					ff = false;
					if (play.getText().equals("play")) {
						mediaPlayerComponent.getMediaPlayer().setPause(true);
					}
				}
			}
		});
		// This will allow you to identify when the volume bar is being changed
		// and then set the value to the position
		// of the slider to the correct volume.
		volume.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				mediaPlayerComponent.getMediaPlayer().setVolume(
						source.getValue());
			}
		});
		// This will allow you to click anywhere on the volume slider and this
		// will update the volume bar
		volume.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JSlider sourceSlider = (JSlider) e.getSource();
				BasicSliderUI ui = (BasicSliderUI) sourceSlider.getUI();
				int value = ui.valueForXPosition(e.getX());
				volume.setValue(value);
			}
		});
		// This will allow you to mute the volume or unmute it back to the
		// previous value.
		mute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Checks if volume is not already muted and if not, mute it.
				if (volume.getValue() != 0) {
					currentVolume = volume.getValue();
					volume.setValue(0);
					// mediaPlayerComponent.getMediaPlayer().mute();
					// If muted already, and clicked mute again, it will refer
					// back to the previous value it was just before being
					// muted.
				} else {
					volume.setValue(currentVolume);
				}
			}
		});
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
					play.setText("pause");
					run();
				}
				if (play.getText().equals("pause")) {
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
							"Error please open a video before trying to save.");
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
						play.setText("play");
					}
				});
		
		//==============Right pane action listeners=======================
		
				// Opens a file chooser and lets the user select a commentary to add to the table
					addCommentary1.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JFileChooser ourFileSelector= new JFileChooser();
						File ourFile;
						String mediaPath = null;

						FileFilter filter = new FileNameExtensionFilter("MP3 FILES","mp3");
						ourFileSelector.resetChoosableFileFilters();
						ourFileSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
						ourFileSelector.setFileFilter(filter);

						ourFileSelector.showOpenDialog(null);
						if (!(ourFileSelector.getSelectedFile() == null)){
							ourFile=ourFileSelector.getSelectedFile();
							mediaPath=ourFile.getAbsolutePath();
							Object[] data = { mediaPath, "0", timeLabel.getText(), true };
							audioOverlayTable.addRow(data);
						}
					}
				});
					// Merges all selected commentary on to the current video
					mergeCommentary.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
						
							int numberOfAudio = 1;
							String command = "ffmpeg -y -i \""+TextToMp3Screen.originalVideo+"\" ";
							
								//	 + " -map 0:0 -map 1:0 -map 2:0 -c:v copy -async 1 -filter_complex amix=inputs=3 out.avi"
									
							for (int i = 0; i < table2.getRowCount(); i++){
								if ((boolean) table2.getValueAt(i, 3)){

									String timeToAdd = (String)table2.getValueAt(i, 2);
									String[] timeValues = timeToAdd.split(":");
									int timeInSeconds = Integer.parseInt(timeValues[0])*3600 + Integer.parseInt(timeValues[1])*60 + Integer.parseInt(timeValues[2]);

									command = command+"-itsoffset "+timeInSeconds+" -i "+(String)table2.getValueAt(i, 0)+" ";
									numberOfAudio++;
								}
							}
							
							command = command + "-map 0:v:0 ";
							for(int k = 1; k<numberOfAudio; k++){
								command = command + "-map "+k+":0 ";
							}
							command = command + "-c:v copy -async 1 -filter_complex amix=inputs="+numberOfAudio+" ";
							
							OverlayMp3OntoVideo k = new OverlayMp3OntoVideo(command, "kkona", true);
							k.execute();
							
						}
					});
	}
}
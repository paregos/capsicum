package vidVox.guiScreens.panes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;

import vidVox.guiScreens.MainPlayerScreen;
import vidVox.workers.Skip;

public class ControlsPane extends JPanel{
	
	public static JButton fastforward, rewind, play, mute;
	GridBagConstraints c;
	private JLabel volumeLabel;
	private JSlider volume;
	private MainPlayerScreen mainplayer;

	public ControlsPane (MainPlayerScreen mainplayer){
		this.mainplayer = mainplayer;
		setUpLayout();
		setUpListeners();
	}

	public void setUpLayout() {
		this.setLayout(new GridBagLayout());
		
		// JButton which fast forwards through the video
				fastforward = new JButton(">>");
				c = new GridBagConstraints();
				c.gridx = 3;
				c.gridy = 0;
				c.weightx = 0;
				c.weighty = 1;
				c.insets = new Insets(0, 5, 0, 10);
				// c.anchor = GridBagConstraints.EAST;
				this.add(fastforward, c);

				// JButton which fast forwards through the video
				rewind = new JButton("<<");
				c = new GridBagConstraints();
				c.gridx = 1;
				c.gridy = 0;
				c.weightx = 0;
				c.weighty = 1;
				c.insets = new Insets(0, 10, 0, 5);
				// c.anchor = GridBagConstraints.WEST;
				this.add(rewind, c);

				// JButton which Plays the video
				play = new JButton("pause");
				c = new GridBagConstraints();
				c.gridx = 2;
				c.gridy = 0;
				c.weightx = 0;
				c.weighty = 1;
				c.insets = new Insets(0, 5, 0, 5);
				this.add(play, c);

				// Volume label
				volumeLabel = new JLabel("Volume");
				c = new GridBagConstraints();
				c.gridx = 5;
				c.gridy = 0;
				c.weightx = 0;
				c.weighty = 1;
				c.insets = new Insets(0, 20, 0, 10);
				c.anchor = GridBagConstraints.WEST;
				this.add(volumeLabel, c);

				// JButton which mutes the audio of the video
				mute = new JButton("Mute");
				c = new GridBagConstraints();
				c.gridx = 7;
				c.gridy = 0;
				c.weightx = 0;
				c.weighty = 1;
				c.insets = new Insets(0, 10, 0, 10);
				c.anchor = GridBagConstraints.EAST;
				this.add(mute, c);

				// JSlider which controls the volume of the video
				volume = new JSlider();
				c = new GridBagConstraints();
				c.gridx = 6;
				c.gridy = 0;
				c.weightx = 0;
				c.weighty = 1;
				c.insets = new Insets(0, 10, 0, 10);
				this.add(volume, c);

				// adding empty jlabels to the control panel to space out the buttons
				// nicely
				JLabel one = new JLabel();
				c = new GridBagConstraints();
				c.gridx = 0;
				c.gridy = 0;
				c.weightx = 1;
				c.weighty = 1;
				this.add(one, c);
				JLabel two = new JLabel();
				c = new GridBagConstraints();
				c.gridx = 4;
				c.gridy = 0;
				c.weightx = 1;
				c.weighty = 1;
				this.add(two, c);
		
	}
	
	public void setUpListeners() {
		// Adds an action listener when the play button is clicked.
				play.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// Check if fast forwarding or rewind is on when the play/pause
						// button is clicked
						// and will cancel it.
						if (MainPlayerScreen.ff == true) {
							MainPlayerScreen.ffswing.cancel(true);
							MainPlayerScreen.ff = false;
						}
						if (MainPlayerScreen.rw == true) {
							MainPlayerScreen.rwswing.cancel(true);
							MainPlayerScreen.rw = false;
						}
						// Pauses or plays the video depending if it is playing or
						// paused respectively and also
						// changes the text of the button to add a more user friendly
						// experience.
						if (play.getText().equals("play")) {
							play.setText("pause");
							MainPlayerScreen.mediaPlayerComponent.getMediaPlayer().play();

						} else {
							play.setText("play");
							MainPlayerScreen.mediaPlayerComponent.getMediaPlayer().setPause(true);
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
						if ((MainPlayerScreen.ff == true) && (MainPlayerScreen.rw == false)) {
							MainPlayerScreen.rwswing = new Skip(MainPlayerScreen.mediaPlayerComponent, -1000, mainplayer);
							MainPlayerScreen.ffswing.cancel(true);
							MainPlayerScreen.rwswing.skip = true;
							MainPlayerScreen.rwswing.execute();
							MainPlayerScreen.rw = true;
							MainPlayerScreen.ff = false;
							// Press rewind button while not rewinding or fastforwarding
							// will result in rewind
							// just being executed.
						} else if ((MainPlayerScreen.rw == false) && (MainPlayerScreen.ff == false)) {
							MainPlayerScreen.rwswing = new Skip(MainPlayerScreen.mediaPlayerComponent, -1000, mainplayer);
							MainPlayerScreen.rwswing.skip = true;
							MainPlayerScreen.rwswing.execute();
							MainPlayerScreen.	rw = true;
							// Press rewind button while rewinding and not fast
							// forwarding will cause rewind
							// to be canceled.
						} else if ((MainPlayerScreen.rw == true) && (MainPlayerScreen.ff == false)) {
							MainPlayerScreen.	rwswing.cancel(true);
							MainPlayerScreen.rw = false;
							// Will pause the component if rewind is canceled and it was
							// paused when rewinding.
							if (play.getText().equals("play")) {
								MainPlayerScreen.mediaPlayerComponent.getMediaPlayer().setPause(true);
							}
							// Last case where you press the rewind button whilst its
							// rewinding and fastforwarding
							// which cant occur but act as a backup code in base of
							// bugs.
						} else {
							MainPlayerScreen.rwswing.cancel(true);
							MainPlayerScreen.rw = false;
							MainPlayerScreen.ffswing.cancel(true);
							MainPlayerScreen.ff = false;
							// Sets the play button to an appropriate button.
							if (play.getText().equals("play")) {
								MainPlayerScreen.mediaPlayerComponent.getMediaPlayer().setPause(true);
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
						if ((MainPlayerScreen.ff == false) && (MainPlayerScreen.rw == true)) {
							MainPlayerScreen.ffswing = new Skip(MainPlayerScreen.mediaPlayerComponent, 1000, mainplayer);
							MainPlayerScreen.rwswing.cancel(true);
							MainPlayerScreen.ffswing.skip = true;
							MainPlayerScreen.ffswing.execute();
							MainPlayerScreen.ff = true;
							MainPlayerScreen.rw = false;
							// Checks whether fastforward button is clicked when
							// fastforward is off and rewind
							// is off. It will cause fastforward to execute.
						} else if ((MainPlayerScreen.ff == false) && (MainPlayerScreen.rw == false)) {
							MainPlayerScreen.ffswing = new Skip(MainPlayerScreen.mediaPlayerComponent, 1000, mainplayer);
							MainPlayerScreen.ffswing.skip = true;
							MainPlayerScreen.ffswing.execute();
							MainPlayerScreen.ff = true;
							// Checks whether fastforward button is clicked when
							// fastforward is on and rewind
							// is off. It will cause fastforward to stop.
						} else if ((MainPlayerScreen.ff == true) && (MainPlayerScreen.rw == false)) {
							// ffswing.skip=false;
							MainPlayerScreen.ffswing.cancel(true);
							MainPlayerScreen.ff = false;
							if (play.getText().equals("play")) {
								MainPlayerScreen.mediaPlayerComponent.getMediaPlayer().setPause(true);
							}
							// Last case where you press the fastforward button whilst
							// its rewinding and fastforwarding
							// which cant occur but act as a backup code in base of
							// bugs.
						} else {
							MainPlayerScreen.rwswing.cancel(true);
							MainPlayerScreen.rw = false;
							MainPlayerScreen.ffswing.cancel(true);
							MainPlayerScreen.ff = false;
							if (play.getText().equals("play")) {
								MainPlayerScreen.mediaPlayerComponent.getMediaPlayer().setPause(true);
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
						MainPlayerScreen.mediaPlayerComponent.getMediaPlayer().setVolume(
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
							MainPlayerScreen.currentVolume = volume.getValue();
							volume.setValue(0);
							mute.setText("Unmute");
							// mediaPlayerComponent.getMediaPlayer().mute();
							// If muted already, and clicked mute again, it will refer
							// back to the previous value it was just before being
							// muted.
						} else {
							volume.setValue(MainPlayerScreen.currentVolume);
							mute.setText("Mute");
						}
					}
				});
		
	}
}

package vidivox.guiscreens;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;

public class PositionSlider {

	private JSlider positionSlider;
	private ChangeListener listener;
	private boolean refresh;
	private MainPlayerScreen mainscreen;
	
	public PositionSlider(JPanel topPane, MainPlayerScreen mainscreen){
		
		this.mainscreen = mainscreen;
		//creating the slider that will control the position of the video
		setPositionSlider(new JSlider());
		getPositionSlider().setMinimum(0);
		getPositionSlider().setMaximum(1000);
		getPositionSlider().setValue(0);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 10, 10, 10);
		c.fill = GridBagConstraints.HORIZONTAL;
		topPane.add(getPositionSlider(), c);
		
		setUpListeners();
	}
	
	
	// This class will set the video based on the position of the slider.
	public void setSliderBasedPosition() {
		// Check if it is playable.
		if (!MainPlayerScreen.mediaPlayerComponent.getMediaPlayer().isSeekable()) {
			return;
		}
		float positionValue = getPositionSlider().getValue() / 1000.0f;
		// Makes sure it wont freeze.
		if (positionValue > 0.99f) {
			positionValue = 0.99f;
		}
		// Set position of position slider.
		MainPlayerScreen.mediaPlayerComponent.getMediaPlayer().setPosition(positionValue);
	}
	
	
	public void setUpListeners() {
	// This is a change listener used to look at the changes when the video
	// is being played and mainly used to
	// update the the time of the video when the position slider is being
	// dragged.
	listener = new ChangeListener() {
		public void stateChanged(ChangeEvent event) {
			if (refresh) {
				setSliderBasedPosition();
				mainscreen.updateGUI();
			}
		}
	};
	getPositionSlider().addChangeListener(listener);
	getPositionSlider().addMouseListener(new MouseAdapter() {
		// Checks for when the mouse is being pressed and updates the
		// position and time appropriately.
		@Override
		public void mousePressed(MouseEvent e) {
			refresh = true;
			if (MainPlayerScreen.mediaPlayerComponent.getMediaPlayer().isPlaying()) {
				mainscreen.setPressedWhilePlaying(true);
			} else {
				mainscreen.setPressedWhilePlaying(false);
			}
			// This will allow you to go to a certain point in the video by
			// clicking on the position slider and then updates
			// the video based on where you clicked.
			JSlider sourceSlider = (JSlider) e.getSource();
			BasicSliderUI ui = (BasicSliderUI) sourceSlider.getUI();
			int value = ui.valueForXPosition(e.getX());
			getPositionSlider().setValue(value);
			setSliderBasedPosition();
		}

		// Checks for when the mouse has been released and gets the point
		// where it has been released.
		@Override
		public void mouseReleased(MouseEvent e) {
			// Updates the slider and the time.
			refresh = false;
			setSliderBasedPosition();
			mainscreen.updateGUI();
		}
	});
	}


	public JSlider getPositionSlider() {return positionSlider;}


	public void setPositionSlider(JSlider positionSlider) {this.positionSlider = positionSlider;}
	
}

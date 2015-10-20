package vidVox.guiScreens.panes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.binding.LibVlcConst;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import vidVox.guiScreens.MainPlayerScreen;

public class EffectsPane extends JPanel {

	EmbeddedMediaPlayerComponent mediaPlayerComponent;
	JSlider hueSlider, brightnessSlider, gammaSlider, saturationSlider;
	JLabel hueLabel, brightnessLabel, gammaLabel, saturationLabel;
	GridBagConstraints c;
	JSeparator separator;

	public EffectsPane(EmbeddedMediaPlayerComponent mediaPlayerComponent) {

		this.mediaPlayerComponent = mediaPlayerComponent;
		setUpLayout();
		setUpListeners();
	}

	public void setUpLayout() {

		this.setLayout(new GridBagLayout());

		// JSeperator which adds Commentary to the list
		separator = new JSeparator();
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(5, 10, 10, 5);
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(separator, c);

		// creating a JLabel that will show the hue levels
		hueLabel = new JLabel("Hue levels");
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.insets = new Insets(5, 10, 10, 5);
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(hueLabel, c);

		// creating a slider that will set the hue values of the video
		hueSlider = new JSlider();
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		hueSlider.setValue(100);
		hueSlider.setMinimum(Math.round(LibVlcConst.MIN_HUE));
		hueSlider.setMaximum(Math.round(LibVlcConst.MAX_HUE - 60));
		hueSlider.setMajorTickSpacing(100);
		hueSlider.setMinorTickSpacing(10);
		hueSlider.setPaintTicks(true);
		hueSlider.setPaintLabels(true);
		c.insets = new Insets(5, 10, 10, 5);
		c.anchor = GridBagConstraints.SOUTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(hueSlider, c);

		// creating a JLabel that will show the brightness levels
		brightnessLabel = new JLabel("Brightness levels");
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.insets = new Insets(5, 10, 10, 5);
		c.anchor = GridBagConstraints.SOUTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(brightnessLabel, c);

		// creating a slider that will set the brightness values of the video
		brightnessSlider = new JSlider();
		c.gridx = 1;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		brightnessSlider.setValue(100);
		brightnessSlider.setMinimum(Math.round(LibVlcConst.MIN_BRIGHTNESS * 100.0f));
		brightnessSlider.setMaximum(Math.round(LibVlcConst.MAX_BRIGHTNESS * 100.0f));
		brightnessSlider.setMajorTickSpacing(50);
		brightnessSlider.setMinorTickSpacing(5);
		brightnessSlider.setPaintTicks(true);
		brightnessSlider.setPaintLabels(true);
		c.insets = new Insets(5, 10, 10, 5);
		c.anchor = GridBagConstraints.SOUTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(brightnessSlider, c);

		// creating a JLabel that will show the gamma levels
		gammaLabel = new JLabel("Gamma levels");
		c.gridx = 2;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.insets = new Insets(5, 10, 10, 5);
		c.anchor = GridBagConstraints.SOUTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(gammaLabel, c);

		// creating a slider that will set the gamma values of the video
		gammaSlider = new JSlider();
		c.gridx = 2;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		gammaSlider.setValue(100);
		gammaSlider.setMinimum(Math.round(LibVlcConst.MIN_GAMMA * 100 - 1));
		gammaSlider.setMaximum(Math.round(LibVlcConst.MAX_GAMMA * 100 + 1));
		gammaSlider.setMajorTickSpacing(200);
		gammaSlider.setMinorTickSpacing(100);
		gammaSlider.setPaintTicks(true);
		gammaSlider.setPaintLabels(true);
		c.insets = new Insets(5, 10, 10, 5);
		c.anchor = GridBagConstraints.SOUTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(gammaSlider, c);
		
		// creating a JLabel that will show the gamma levels
		saturationLabel = new JLabel("Saturation levels");
		c.gridx = 3;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.insets = new Insets(5, 10, 10, 5);
		c.anchor = GridBagConstraints.SOUTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(saturationLabel, c);

		// creating a slider that will set the gamma values of the video
		saturationSlider = new JSlider();
		c.gridx = 3;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		saturationSlider.setValue(100);
		saturationSlider.setMinimum(Math.round(LibVlcConst.MIN_SATURATION * 100));
		saturationSlider.setMaximum(Math.round(LibVlcConst.MAX_SATURATION * 100));
		saturationSlider.setMajorTickSpacing(100);
		saturationSlider.setMinorTickSpacing(10);
		saturationSlider.setPaintTicks(true);
		saturationSlider.setPaintLabels(true);
		c.insets = new Insets(5, 10, 10, 5);
		c.anchor = GridBagConstraints.SOUTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(saturationSlider, c);
	}

	public void setUpListeners() {

		// Action listener for the hue of the video
		hueSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {

				mediaPlayerComponent = MainPlayerScreen.mediaPlayerComponent;
				// Disabling and then enabling to fix problem where it wouldn't
				// work
				mediaPlayerComponent.getMediaPlayer().setAdjustVideo(false);
				mediaPlayerComponent.getMediaPlayer().setAdjustVideo(true);

				// Getting the source and setting the brightness to users choice
				JSlider source = (JSlider) e.getSource();
				mediaPlayerComponent.getMediaPlayer().setHue(source.getValue());
				System.out.println(mediaPlayerComponent.getMediaPlayer().getHue());
			}
		});

		// Action listener for the brightness of the video
		brightnessSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				mediaPlayerComponent = MainPlayerScreen.mediaPlayerComponent;
				// Disabling and then enabling to fix problem where it wouldn't
				// work
				mediaPlayerComponent.getMediaPlayer().setAdjustVideo(false);
				mediaPlayerComponent.getMediaPlayer().setAdjustVideo(true);

				// Getting the source and setting the brightness to users choice
				JSlider source = (JSlider) e.getSource();
				mediaPlayerComponent.getMediaPlayer().setBrightness(source.getValue() / 100.0f);
			}
		});
		
		// Action listener for the gamma slider of the video
		gammaSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				mediaPlayerComponent = MainPlayerScreen.mediaPlayerComponent;
				// Disabling and then enabling to fix problem where it wouldn't
				// work
				mediaPlayerComponent.getMediaPlayer().setAdjustVideo(false);
				mediaPlayerComponent.getMediaPlayer().setAdjustVideo(true);

				// Getting the source and setting the brightness to users choice
				JSlider source = (JSlider) e.getSource();
				mediaPlayerComponent.getMediaPlayer().setGamma(source.getValue() / 100);
			}
		});

		// Action listener for the saturation slider of the video
		saturationSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				mediaPlayerComponent = MainPlayerScreen.mediaPlayerComponent;
				// Disabling and then enabling to fix problem where it wouldn't
				// work
				mediaPlayerComponent.getMediaPlayer().setAdjustVideo(false);
				mediaPlayerComponent.getMediaPlayer().setAdjustVideo(true);

				// Getting the source and setting the brightness to users choice
				JSlider source = (JSlider) e.getSource();
				mediaPlayerComponent.getMediaPlayer().setSaturation(source.getValue() / 100);
			}
		});
		

	}

}

package vidVox.guiScreens;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;



public class LoadingScreen extends JFrame{
	//
	private JPanel pane;

	public LoadingScreen(){

		//making the main initial layout for the loading screen
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(300, 300, 400, 100);
		setTitle("Adding commentary please wait");
		GridBagConstraints c = new GridBagConstraints();

		//creating the content pane which will store all of the loading screen components
		GridBagLayout gbl_Pane = new GridBagLayout();
		pane = new JPanel(gbl_Pane);
		setContentPane(pane);

		//creating a jslider which will show progress
		JProgressBar progessBar = new JProgressBar();
		c = new GridBagConstraints();
		progessBar.setIndeterminate(true);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(10,10,10,10);
		c.fill = GridBagConstraints.HORIZONTAL;
		pane.add(progessBar, c);

	}


}

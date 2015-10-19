package vidVox.guiScreens;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import vidVox.workers.OverlayMp3OntoVideo;

public class AddCommentaryScreen extends JFrame{

	public static JFileChooser ourFileSelector= new JFileChooser();
	private JPanel pane;
	private JTextField textfield;

	public AddCommentaryScreen (){

		//making the main initial layout for the AddCommentaryScreen
		setBounds(450, 450, 850, 100);
		setTitle("Enter text between 1-75 characters");
		GridBagConstraints c = new GridBagConstraints();
		setTitle("Choose a mp3 file to overlay onto the current video");

		//creating the content pane which will store all of the addcommentaryScreen components
		GridBagLayout gbl_Pane = new GridBagLayout();
		pane = new JPanel(gbl_Pane);
		setContentPane(pane);


		//creating a Jbutton which will add commentary to the start of the video
		JButton selectMp3 = new JButton("Select Mp3 Commentary..");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.insets = new Insets(0,5,0,10);
		pane.add(selectMp3, c);

		//creating a label which will show the currently opened file;
		textfield = new JTextField("N/A");
		textfield.setEditable(false);
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 30;
		c.weighty = 0;
		c.insets = new Insets(10,10,10,10);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		pane.add(textfield, c);

		//creating a label which will show the currently opened file;
		JLabel label1 = new JLabel("Current File: ");
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(10,10,10,10);
		pane.add(label1, c);

		//creating a Jbutton which will add commentary to the start of the video
		JButton start = new JButton("Add at start of video");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(0,5,0,10);
		pane.add(start, c);

		//Action listener which will allow you to choose a file for adding mp3.
		selectMp3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
					textfield.setText(mediaPath);	
				}
			}
		});
		//Action listener for adding video at the start.
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Checks if commentary file is empty.
				if (!(textfield.getText().equals("N/A"))){
					OverlayMp3OntoVideo k = new OverlayMp3OntoVideo(textfield.getText(), "kkona", true);
					k.execute();
				}else{
					JOptionPane.showMessageDialog(null, "error please select a mp3 before adding commentary please");
				}
			}
		});


	}


}

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

import vidVox.workers.PreviewTextSpeech;
import vidVox.workers.TextToFile;


public class TextToMp3Screen extends JFrame {
	//
	private JPanel pane;
	private JTextField textbox;
	public static int videoNumber =0;
	public static String originalVideo;
	public static MainPlayerScreen mainPlayerScreen;


	public TextToMp3Screen (MainPlayerScreen mainPlayerScreen) {

		//linking the main player screen with this one
		this.mainPlayerScreen = mainPlayerScreen;

		//making the main initial layout for the textToMp3Screen
		setBounds(300, 300, 650, 100);
		setTitle("Enter text between 1-75 characters");
		GridBagConstraints c = new GridBagConstraints();		

		//creating the content pane which will store all of the TextToMp3Screen components
		GridBagLayout gbl_Pane = new GridBagLayout();
		pane = new JPanel(gbl_Pane);
		setContentPane(pane);

		//creating the text box which will store the text that will be either made into a mp3 or overlayed onto the video
		textbox = new JTextField();
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10,10,10,10);
		c.fill = GridBagConstraints.HORIZONTAL;
		pane.add(textbox, c);

		//creating a Jbutton which will let the user preview the textToMp3Sound
		JButton preview = new JButton("Preview mp3");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(0,5,0,10);
		pane.add(preview, c);

		//creating a Jbutton which will overlay the text entered at the beginning of the video
		JButton overlay = new JButton("Add commentary to current video");
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(0,5,0,10);
		pane.add(overlay, c);

		//creating a Jbutton which will save the text entered into a mp3 file
		JButton saveToMp3 = new JButton("Save text to Mp3");
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(0,5,0,10);
		pane.add(saveToMp3, c);

		//creating a label which will tell the user to enter text 
		JLabel enterText = new JLabel("Enter text");
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.insets = new Insets(0,5,0,10);
		pane.add(enterText, c);


		preview.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (textbox.getText().length() > 75 || textbox.getText().length() < 1){
					JOptionPane.showMessageDialog(null, "Error please enter between 1-75 characters");
				}else if (textbox.getText().trim().equals("")){
					JOptionPane.showMessageDialog(null, "Error please enter some characters, not only spaces");

				}else{
					PreviewTextSpeech k = new PreviewTextSpeech(textbox.getText());
					k.execute();
				}
			}
		});

		saveToMp3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ((textbox.getText().length() > 75) || (textbox.getText().length() < 1)){
					JOptionPane.showMessageDialog(null, "Error please enter between 1-75 characters");
				}else if (textbox.getText().trim().equals("")){
					JOptionPane.showMessageDialog(null, "Error please enter some characters, not only spaces");
				}else{

					String mediaPath="~/";
					File ourFile;
					FileFilter filter = new FileNameExtensionFilter("MP3 File","mp3");
					SaveDialogScreen.ourFileSelector.resetChoosableFileFilters();
					//opening the save file explorer, user gets to choose where to save the file   
					SaveDialogScreen.ourFileSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
					SaveDialogScreen.ourFileSelector.setFileFilter(filter);
					SaveDialogScreen.ourFileSelector.showSaveDialog(null);
					if (!(SaveDialogScreen.ourFileSelector.getSelectedFile() == null)){
						ourFile=SaveDialogScreen.ourFileSelector.getSelectedFile();
						mediaPath=ourFile.getAbsolutePath();

						//creates the mp3 file at the location
						TextToFile k = new TextToFile(textbox.getText(), mediaPath, false);
						k.execute();
					}
				}
			}
		});
		
		overlay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (textbox.getText().length() > 75 || textbox.getText().length() < 1){

					JOptionPane.showMessageDialog(null, "Error please enter between 1-75 characters");

				}else if (MainPlayerScreen.mediapath == null){
					JOptionPane.showMessageDialog(null, "Error open a video before adding commentary please");
				}else if (textbox.getText().trim().equals("")){
					JOptionPane.showMessageDialog(null, "Error please enter some characters, not only spaces");
				}else{

					//creates a mp3 file, places it in tmp and overlays the audio
					String path = "/tmp/"+textbox.getText();
					TextToFile k = new TextToFile(textbox.getText(), path, true);
					k.execute();
				}
			}
		});


	}

}

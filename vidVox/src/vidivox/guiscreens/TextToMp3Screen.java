package vidivox.guiscreens;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import vidivox.workers.PreviewTextSpeech;
import vidivox.workers.TextToFile;


public class TextToMp3Screen extends JFrame {
	//
	private JPanel pane,timerPane ;
	private JTextField textbox, textbox1, textbox2, textbox3;
	private JComboBox voiceChooser;
	private GridBagLayout gbl_timer;
	public static int videoNumber =0;
	private String offset;
	public static String originalVideo;
	public static MainPlayerScreen mainPlayerScreen;
	private int textNumber =0;
	

	public TextToMp3Screen (MainPlayerScreen mainPlayerScreen) {

		//linking the main player screen with this one
		this.mainPlayerScreen = mainPlayerScreen;

		//making the main initial layout for the textToMp3Screen
		setBounds(300, 300, 650, 150);
		setTitle("Enter text between 1-75 characters");
		GridBagConstraints c = new GridBagConstraints();		

		//creating the content pane which will store all of the TextToMp3Screen components
		GridBagLayout gbl_Pane = new GridBagLayout();
		pane = new JPanel(gbl_Pane);
		setContentPane(pane);

		// creating the content pane which will store the timer chooser
		gbl_timer = new GridBagLayout();
		timerPane = new JPanel(gbl_timer);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		//c.anchor = GridBagConstraints.WEST;
		//c.fill = GridBagConstraints.BOTH;
		pane.add(timerPane, c);
		
		//creating the text box which will store the text that will be either made into a mp3 or overlayed onto the video
		textbox = new JTextField();
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10,10,10,10);
		c.fill = GridBagConstraints.HORIZONTAL;
		pane.add(textbox, c);

		//creating a Jbutton which will let the user preview the textToMp3Sound
		JButton preview = new JButton("Preview mp3");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(0,5,10,10);
		pane.add(preview, c);

		//creating a Jbutton which will overlay the text entered at the beginning of the video
		JButton overlay = new JButton("Add text to commentary list");
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 2;
		c.weightx = 1;
		c.gridwidth = 2;
		c.weighty = 0;
		c.insets = new Insets(0,5,10,10);
		pane.add(overlay, c);

		//creating a Jbutton which will save the text entered into a mp3 file
		JButton saveToMp3 = new JButton("Save text to Mp3");
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(0,5,10,10);
		pane.add(saveToMp3, c);

		//creating a label which will tell the user to enter text 
		JLabel enterText = new JLabel("Enter text");
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.insets = new Insets(0,5,0,10);
		pane.add(enterText, c);
		
		//creating a label which will tell the user to select a time to enter at.
		JLabel insert = new JLabel("Insert at HH:MM:SS");
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.insets = new Insets(0,5,0,10);
		pane.add(insert, c);
		
		//creating a label which will tell the user to select a time to enter at.
		JLabel voice = new JLabel("Select a voice ");
		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.insets = new Insets(0,5,0,10);
		pane.add(voice, c);

		//creating the text box which will store the hours
		textbox1 = new JTextField("00");
		textbox1.setPreferredSize(new Dimension(42, 25));
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(10,2,10,2);
		timerPane.add(textbox1, c);
		
		// creating the text box which will store the minutes
		textbox2 = new JTextField("00");
		textbox2.setPreferredSize(new Dimension(42, 25));
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(10, 2, 10, 2);
		timerPane.add(textbox2, c);

		// creating the text box which will store the time seconds
		textbox3 = new JTextField("00");
		textbox3.setPreferredSize(new Dimension(42, 25));
		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(10, 2, 10, 2);
		timerPane.add(textbox3, c);
		
		//creating a label which will split the timer options
		JLabel col = new JLabel(":");
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.insets = new Insets(0,5,0,10);
		timerPane.add(col, c);
		
		// creating a label which will split the timer options
		JLabel col1 = new JLabel(":");
		c.gridx = 3;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.insets = new Insets(0, 5, 0, 10);
		timerPane.add(col1, c);
		
		// creating the text box which will store the time at which the audio is
		// added to the video
		voiceChooser = new JComboBox();
		voiceChooser.addItem("Default");
		voiceChooser.addItem("Male");
		voiceChooser.addItem("Female");
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 10, 10, 10);
		c.fill = GridBagConstraints.HORIZONTAL;
		pane.add(voiceChooser, c);
		

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
						TextToFile k = new TextToFile(textbox.getText(), mediaPath, false, textNumber, "00:00:00");
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
					offset = textbox1.getText()+":"+textbox2.getText()+":"+textbox3.getText();
					//creates a mp3 file, places it in tmp and overlays the audio
					String path = "/tmp/iop"+textNumber;
					TextToFile k = new TextToFile(textbox.getText(), path, true, textNumber, offset);
					k.execute();
					textNumber++;
				}
			}
		});


	}

}

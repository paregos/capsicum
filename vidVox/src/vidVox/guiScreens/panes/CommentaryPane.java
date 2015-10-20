package vidVox.guiScreens.panes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import vidVox.guiScreens.MainPlayerScreen;
import vidVox.guiScreens.TextToMp3Screen;
import vidVox.workers.OverlayMp3OntoVideo;

public class CommentaryPane extends JPanel {

	public static JButton createCommentary1, removeCommentary, mergeCommentary, addCommentary1;
	private GridBagConstraints c;
	DefaultTableModel audioOverlayTable = null;
	public JTable table2;
	
	public CommentaryPane(){
		setUpLayout();
		setUpListeners();
	}
	
	
	public void setUpLayout() {
		
		this.setLayout(new GridBagLayout());
		
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
		this.add(createCommentary1, c);

		// JButton which add selected Commentary to the video
		mergeCommentary = new JButton("Merge selected commentary to video");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(0, 10, 5, 10);
		c.anchor = GridBagConstraints.SOUTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(mergeCommentary, c);
		
		// JButton which add selected Commentary to the video
		removeCommentary = new JButton("Remove selected commentary from list");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(0, 10, 5, 10);
		c.anchor = GridBagConstraints.SOUTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(removeCommentary, c);

		// JButton which adds Commentary to the list
		addCommentary1 = new JButton("Select Mp3 Commentary to add at "+ MainPlayerScreen.timeLabel.getText());
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(0, 10, 5, 10);
		c.anchor = GridBagConstraints.SOUTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(addCommentary1, c);

		// Creating a table which will hold all of the information relating to
		// commentaries being added
		String[] audioOverlayOptions = { "Full name", "Commentary File Name",
				"Duration", "Time inserted", "Include?" };
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
					columnType = String.class;
					break;
				case 4:
					columnType = Boolean.class;
					break;
				}
				return columnType;
			}
		};
		table2 = new JTable(audioOverlayTable);
		table2.setModel(audioOverlayTable);
		table2.removeColumn(table2.getColumnModel().getColumn(0));

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
		this.add(scrollPane, c);
	}
	
	public void setUpListeners() {
		
		// Opens a file chooser and lets the user select a commentary to add to
		// the table
		addCommentary1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser ourFileSelector = new JFileChooser();
				File ourFile;
				String mediaPath = null;

				FileFilter filter = new FileNameExtensionFilter("MP3 FILES",
						"mp3");
				ourFileSelector.resetChoosableFileFilters();
				ourFileSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
				ourFileSelector.setFileFilter(filter);

				ourFileSelector.showOpenDialog(null);
				if (!(ourFileSelector.getSelectedFile() == null)) {
					ourFile = ourFileSelector.getSelectedFile();
					mediaPath = ourFile.getAbsolutePath();
					Object[] data = { mediaPath, ourFile.getName(), "0",
							MainPlayerScreen.timeLabel.getText(), true };
					audioOverlayTable.addRow(data);
				}
			}
		});
		// Merges all selected commentary on to the current video
		mergeCommentary.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				int numberOfAudio = 1;
				String command = "ffmpeg -y -i \""+ TextToMp3Screen.originalVideo + "\" ";

				for (int i = 0; i < table2.getRowCount(); i++) {
					if ((boolean) table2.getValueAt(i, 3)) {

						String timeToAdd = (String) table2.getValueAt(i, 2);
						String[] timeValues = timeToAdd.split(":");
						int timeInSeconds = Integer.parseInt(timeValues[0])
								* 3600 + Integer.parseInt(timeValues[1]) * 60
								+ Integer.parseInt(timeValues[2]);

						command = command + "-itsoffset " + timeInSeconds
								+ " -i "
								+ (String) table2.getModel().getValueAt(i, 0)
								+ " ";
						numberOfAudio++;
					}
				}

				command = command + "-map 0:v:0 ";
				for (int k = 1; k < numberOfAudio; k++) {
					command = command + "-map " + k + ":0 ";
				}
				command = command
						+ "-c:v copy -async 1 -filter_complex amix=inputs="
						+ numberOfAudio + " ";

				OverlayMp3OntoVideo k = new OverlayMp3OntoVideo(command,
						"kkona", true);
				k.execute();

			}
		});
	}
	
}
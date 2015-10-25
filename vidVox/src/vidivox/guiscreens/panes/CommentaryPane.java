package vidivox.guiscreens.panes;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import vidivox.guiscreens.MainPlayerScreen;
import vidivox.guiscreens.TextToMp3Screen;
import vidivox.workers.DurationGetter;
import vidivox.workers.OverlayMp3OntoVideo;

public class CommentaryPane extends JPanel {

	private static JButton createCommentary1, removeCommentary, mergeCommentary, addCommentary1, fxMenu;
	private GridBagConstraints c;
	private static DefaultTableModel audioOverlayTable = null;
	private JTable table2;
	private JSeparator separator;
	
	public CommentaryPane(){
		setUpLayout();
		setUpListeners();
	}
	
	
	public void setUpLayout() {
		
		this.setLayout(new GridBagLayout());
		
		// JButton which Creates Commentary
		createCommentary1 = (new JButton("Create Commentary"));
		getCreateCommentary1().setForeground(Color.blue);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(10, 10, 0, 10);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(getCreateCommentary1(), c);
		getCreateCommentary1().setEnabled(false);

		// JButton which add selected Commentary to the video
		mergeCommentary = (new JButton("Merge selected commentary to video"));
		getMergeCommentary().setForeground(Color.blue);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 4;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(0, 10, 5, 10);
		c.anchor = GridBagConstraints.SOUTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(getMergeCommentary(), c);
		getMergeCommentary().setEnabled(false);
		
		// JButton which add selected Commentary to the video
		removeCommentary = (new JButton("Remove selected commentary from list"));
		getRemoveCommentary().setForeground(Color.blue);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(0, 10, 5, 10);
		c.anchor = GridBagConstraints.SOUTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(getRemoveCommentary(), c);
		getRemoveCommentary().setEnabled(false);
		
		// JButton which adds Commentary to the list
		addCommentary1 = (new JButton("Select Mp3 Commentary to add at "+ MainPlayerScreen.getTimeLabel().getText()));
		getAddCommentary1().setForeground(Color.blue);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(0, 10, 5, 10);
		c.anchor = GridBagConstraints.SOUTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(getAddCommentary1(), c);
		getAddCommentary1().setEnabled(false);
		
		// JButton which opens the special fx menu
		fxMenu = (new JButton("Open special video effects tab"));
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 4;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(0, 10, 5, 10);
		c.anchor = GridBagConstraints.SOUTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(getFxMenu(), c);
		getFxMenu().setEnabled(false);
		
		// JSeperator which adds Commentary to the list
		separator = new JSeparator();
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 4;
		c.weightx = 0;
		c.weighty = 0;
		c.insets = new Insets(5, 10, 10, 5);
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(separator, c);

		// Creating a table which will hold all of the information relating to
		// commentaries being added
		String[] audioOverlayOptions = { "Full name", "Commentary",
				"Duration", "Time inserted", "Include?", "Voice Type" };
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
				case 5:
					columnType = String.class;
					break;
				}
				return columnType;
			}
			// removing edit functionality from all of the columns within my commentary table
			public boolean isCellEditable(int row, int column) {
				switch (column){
				case 4:
					return true;
				}
		        return false;
		    }
			
		};
		
		//removing a column fromn my table which will store data
		table2 = new JTable(getAudioOverlayTable());
		table2.setModel(getAudioOverlayTable());
		table2.removeColumn(table2.getColumnModel().getColumn(0));

		//creating a scroll pane which will wrap around my commentary table 
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(table2);
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
		
		// Action listener for fxMenu button, it will open or close the menu
		getFxMenu().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (MainPlayerScreen.getEffectsPane().isVisible()){
					MainPlayerScreen.getEffectsPane().setVisible(false);
					getFxMenu().setText("Open special video effects tab");
				}else{
					MainPlayerScreen.getEffectsPane().setVisible(true);
					getFxMenu().setText("Close special video effects tab");
				}
				repaint();
			}
		});
		
		// Action listener for the gamma slider of the video
		getRemoveCommentary().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		
			int rowsSeclected = table2.getSelectedRows().length;
			for (int i = 0; i < rowsSeclected; i++){
				getAudioOverlayTable().removeRow(table2.getSelectedRow());
			}
			
			}
		});
		
		// Opens a file chooser and lets the user select a commentary to add to
		// the table
		getAddCommentary1().addActionListener(new ActionListener() {
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
					
					DurationGetter k = new DurationGetter(mediaPath, ourFile.getName(), true, 50, MainPlayerScreen.getTimeLabel().getText(), 10);
					k.execute();
				}
			}
		});
		// Merges all selected commentary on to the current video
		getMergeCommentary().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (table2.getModel().getRowCount() != 0){
				int numberOfAudio = 1;
				String command = "ffmpeg -y -i \""+ TextToMp3Screen.originalVideo + "\" ";

				for (int i = 0; i < table2.getRowCount(); i++) {
					if ((boolean) table2.getValueAt(i, 3)) {

						String timeToAdd = (String) table2.getValueAt(i, 2);
						String[] timeValues = timeToAdd.split(":");
						int timeInSeconds = Integer.parseInt(timeValues[0])* 3600 + Integer.parseInt(timeValues[1]) * 60+ Integer.parseInt(timeValues[2]);

						command = command + "-itsoffset " + timeInSeconds+ " -i \""+ (String) table2.getModel().getValueAt(i, 0)+ "\" ";
						numberOfAudio++;
					}
				}

				command = command + "-map 0:v:0 ";
				for (int k = 1; k < numberOfAudio; k++) {
					command = command + "-map " + k + ":0 ";
				}
				command = command+ "-c:v copy -async 1 -filter_complex amix=inputs="+ numberOfAudio + " ";
				OverlayMp3OntoVideo k = new OverlayMp3OntoVideo(command, "kkona", true);
				k.execute();

			}
			}
		});
		
		// Allows the user to create commentary.
		getCreateCommentary1().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				MainPlayerScreen.getCreateCommentaryScreen().setVisible(true);
			}
		});
	}

//getters for fields of Commentary panel
	public static DefaultTableModel getAudioOverlayTable() {return audioOverlayTable;}

	public static JButton getAddCommentary1() {return addCommentary1;}

	public static JButton getCreateCommentary1() {return createCommentary1;}

	public static JButton getMergeCommentary() {return mergeCommentary;}

	public static JButton getRemoveCommentary() {return removeCommentary;}

	public static JButton getFxMenu() {return fxMenu;}


	
}

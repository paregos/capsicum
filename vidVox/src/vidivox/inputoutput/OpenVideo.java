package vidivox.inputoutput;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import vidivox.guiscreens.ButtonManager;
import vidivox.guiscreens.MainPlayerScreen;
import vidivox.guiscreens.TextToMp3Screen;

public class OpenVideo {
	//This will choose my file and also a variable for my media path.
	private static JFileChooser ourFileSelector = new JFileChooser();
	private static String mediaPath = "";

	public static boolean grabFile( MainPlayerScreen mainPlayerScreen) {
		//
		//File we want to choose.
		File ourFile;

		FileFilter filter = new FileNameExtensionFilter("MP4 & AVI","mp4","avi");
		getOurFileSelector().resetChoosableFileFilters();
		getOurFileSelector().setFileSelectionMode(JFileChooser.FILES_ONLY);
		getOurFileSelector().setFileFilter(filter);
		//This will open up a dialog which lets us choose our file and also check that the user has chosen the open option.
		int checkFile = getOurFileSelector().showOpenDialog(null);
		if (checkFile==0){
			//Get selected file.
			ourFile=getOurFileSelector().getSelectedFile();
			mediaPath=ourFile.getAbsolutePath();
			if(!ourFile.exists() ){
				JOptionPane.showMessageDialog(null, "Please select a valid mp4 or avi video"); 
				return false;
				} else{
			TextToMp3Screen.originalVideo = mediaPath;
			mainPlayerScreen.setTitle(ourFile.getName()+" - Vidivox");
				}
			
			//enables buttons that were disabled. 
			ButtonManager btn = new ButtonManager();
			btn.enableButtons();
			
			return true;
		} else {
			//Returns false if they chose to cancel.
			return false;
		}

	}

	public static JFileChooser getOurFileSelector() {return ourFileSelector;}

	public static String getMediaPath() {
		return mediaPath;
	}

}

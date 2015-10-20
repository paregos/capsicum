package vidivox;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import vidivox.guiscreens.TextToMp3Screen;

public class OpenVideo {
	//This will choose my file and also a variable for my media path.
	public static JFileChooser ourFileSelector= new JFileChooser();
	public static String mediaPath="";

	public static boolean grabFile() {
		//
		//File we want to choose.
		File ourFile;

		FileFilter filter = new FileNameExtensionFilter("MP4 & AVI","mp4","avi");
		ourFileSelector.resetChoosableFileFilters();
		ourFileSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
		ourFileSelector.setFileFilter(filter);
		//This will open up a dialog which lets us choose our file and also check that the user has chosen the open option.
		int checkFile = ourFileSelector.showOpenDialog(null);
		if (checkFile==0){
			//Get selected file.
			ourFile=ourFileSelector.getSelectedFile();
			mediaPath=ourFile.getAbsolutePath();
			TextToMp3Screen.originalVideo = mediaPath;
			return true;
		} else {
			//Returns false if they chose to cancel.
			return false;
		}

	}
}

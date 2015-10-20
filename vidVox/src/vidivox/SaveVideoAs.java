package vidivox;


import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import vidivox.guiscreens.MainPlayerScreen;
import vidivox.guiscreens.SaveDialogScreen;
import vidivox.workers.MoveVideoFile;

public class SaveVideoAs {

	public static void saveVideoAs(){
		//
		//applying a mp4 filter
		String mediaPath1;
		File ourFile1;

		//opening the save file explorer, user gets to choose where to save the file   
		if (MainPlayerScreen.mediapath == null){
			JOptionPane.showMessageDialog(null, "error please open a video before trying to save");
		}else{
			//adding a file filter for saving as mp4 and avi
			FileFilter filter = new FileNameExtensionFilter("MP4 & AVI","mp4","avi");
			SaveDialogScreen.ourFileSelector.resetChoosableFileFilters();
			SaveDialogScreen.ourFileSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
			SaveDialogScreen.ourFileSelector.setSelectedFile(new File(OpenVideo.ourFileSelector.getSelectedFile().getName()));	
			SaveDialogScreen.ourFileSelector.setFileFilter(filter);
			SaveDialogScreen.ourFileSelector.showSaveDialog(null);    
			if (!(SaveDialogScreen.ourFileSelector.getSelectedFile() == null)){
				ourFile1=SaveDialogScreen.ourFileSelector.getSelectedFile();
				mediaPath1=ourFile1.getAbsolutePath();

				if ((!(mediaPath1.endsWith(".mp4"))) && (!(mediaPath1.endsWith(".avi")))) {
					mediaPath1 = mediaPath1+".mp4";
				}

				MoveVideoFile k = new MoveVideoFile(MainPlayerScreen.mediapath, mediaPath1);
				k.execute();
			}
		}
	}

}

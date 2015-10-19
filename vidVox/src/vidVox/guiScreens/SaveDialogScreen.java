package vidVox.guiScreens;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class SaveDialogScreen {
	//
	//a file chooser that will ask the user before it overwrites the file
	public static JFileChooser ourFileSelector= new JFileChooser(){
		public void approveSelection() {
			//if the file selected exists already
			if (getSelectedFile().isFile()) {
				Object[] options = { "OK", "Cancel" };
				int choice = JOptionPane.showOptionDialog(null, 
						"Do you want to overwrite the file?", 
						"File already exists", 
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.QUESTION_MESSAGE, 
						null, 
						options, 
						options[0]);
				if(choice == 0){
					super.approveSelection(); 
				}
				return;
			} else
				super.approveSelection();
		}
	};


}

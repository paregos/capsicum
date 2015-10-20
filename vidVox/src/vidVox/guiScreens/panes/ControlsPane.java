package vidVox.guiScreens.panes;

import java.awt.GridBagLayout;

import javax.swing.JPanel;

public class ControlsPane extends JPanel{
	
	public ControlsPane (){
		
		setUpLayout();
		setUpListeners();
	}

	public void setUpLayout() {
		this.setLayout(new GridBagLayout());
		
	}
	
	public void setUpListeners() {
		
	}
}

package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class ButtonPanel extends JPanel {
	private JButton chooseFile;
	private File file;
	private MainW mainW;
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public ButtonPanel (MainW mainW) {
		this.mainW = mainW; 
		
		this.setLayout(new BorderLayout ());
		
		chooseFile = new JButton("Choose File");
		chooseFile.setSize(mainW.getSize().width, mainW.getSize().height/6);
		chooseFile.setBackground(new Color(180, 180, 180));
		ButtonListener b = new ButtonListener();
		b.setButtonPanel(this);
		chooseFile.addActionListener(b);
		this.add(chooseFile);
		
	}
	
	public void fileSelected() {
		mainW.setFile(file);
		
	}	
	
	
	private class ButtonListener implements ActionListener {
		private ButtonPanel buttonPanel;
		
		public void setButtonPanel(ButtonPanel buttonPanel) {
			this.buttonPanel = buttonPanel;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File(""));
			fc.setDialogTitle("Open");
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			
			fc.setCurrentDirectory(fc.getFileSystemView().getParentDirectory(new File("C:\\")));
			
			fc.getActionMap().get("viewTypeDetails").actionPerformed(null);
						
			int returnVal = fc.showOpenDialog(buttonPanel);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            buttonPanel.setFile(fc.getSelectedFile());	  
	            buttonPanel.fileSelected ();
	        }
		}
		
	}

	
}

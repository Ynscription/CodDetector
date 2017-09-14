package View;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JFrame;

import org.mozilla.universalchardet.UniversalDetector;


@SuppressWarnings("serial")
public class MainW extends JFrame{

	private Container mainPanel;
	private OutputPanel outputPanel;
	
	private File file;
		
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
		doTheThing ();
		
	}

	public MainW () throws HeadlessException{
		//Titulo de la ventana
		super ("CodDetector");
		
		//Calculamos la dimension de la ventana de acuerdo a la pantalla
		Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
		int taskBarSize = scnMax.bottom;
		Dimension screenDimension = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
		screenDimension.height = (screenDimension.height - taskBarSize)/2;
		screenDimension.width = (screenDimension.width)/2;
		
		//Establecemos parametros
		this.setSize(screenDimension);
		this.setResizable(true);
		this.setLocation(screenDimension.width/2, screenDimension.height/2);
		
		
		
		//dividimos el contenedor
		mainPanel = this.getContentPane();
		mainPanel.setLayout(new BorderLayout());
		
		
		
		
		//Añadimos componentes
		outputPanel = new OutputPanel(this);
		mainPanel.add (outputPanel,BorderLayout.CENTER);
		
		
		//Mostramos la ventana
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		
	}
	
	private void doTheThing() {
		UniversalDetector detector = new UniversalDetector(null);
		try {
			FileInputStream fis = new FileInputStream(file);
			
			int nread;
			int count = 1;
			byte[] buf = new byte[4096];
			
			outputPanel.updateInput(file.getAbsolutePath());
			outputPanel.updateOutput("Detecting", 1);
			
			while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
			  detector.handleData(buf, 0, nread);
			  
			  
			  if (count % 100 == 0) {
				  String newOutput = "Detecting";
				  for (int i = 0; i < count % 100; i++) {
					  newOutput = newOutput + ".";
				  }
				  
				  outputPanel.updateOutput(newOutput , 1);
				  
				  if (count / 100 == 3) {
					  count = 1;
				  }
			  }
			}
			detector.dataEnd();
			fis.close();
			
			String encoding = detector.getDetectedCharset();
			if (encoding != null) {
				outputPanel.updateOutput(encoding, 0);
			} else {
				outputPanel.updateOutput("No encoding detected.", -1);
			}
			
		}catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
}
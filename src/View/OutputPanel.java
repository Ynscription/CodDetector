package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

@SuppressWarnings("serial")
public class OutputPanel extends JPanel {
	
	private JTextPane input;
	private JTextPane output;
	private MainW mainW;
	private ButtonPanel buttonPanel;
	
	public OutputPanel (MainW mainW) {
		this.mainW = mainW;
		
		this.setBorder(BorderFactory.createLineBorder(new Color(168, 168, 168)));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		TransferHandler th = new PathTransferHandler();
		
		input = new JTextPane();
		input.setBorder(BorderFactory.createLineBorder(new Color(168, 168, 168)));
		input.setBackground(new Color(215, 215, 215));
		input.setEditable(false);
		input.setDragEnabled(true);
		input.setTransferHandler(th);
		//input.setLineWrap(true);
		//input.setWrapStyleWord(true);
		//input.setTabSize(2);
		setPaneText(input, "No file selected", Color.BLACK);
		
		
		output = new JTextPane();
		output.setBorder(BorderFactory.createLineBorder(new Color(168, 168, 168)));
		output.setBackground(new Color(215, 215, 215));
		output.setEditable(false);
		output.setDragEnabled(true);
		output.setTransferHandler(th);
		//output.setLineWrap(true);
		//output.setWrapStyleWord(true);
		//output.setTabSize(4);
		setPaneText(output, "No file selected", Color.BLACK);
		
		input.setAlignmentX(Component.CENTER_ALIGNMENT);
		output.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		
		buttonPanel = new ButtonPanel(mainW);
		
		
		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new BorderLayout());
		upperPanel.add(input, BorderLayout.CENTER);
		upperPanel.add(buttonPanel, BorderLayout.WEST);
		
		
		this.add(upperPanel);
		this.add(output);
		
	}
	
	public void updateInput (String newInput) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				input.setText ("File open:" + System.lineSeparator() + "	" + newInput);
				
			}
		});
	}
	
	public void updateOutput (String newOutput, int result) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				if (result == -1) {
					setPaneText(output, newOutput, Color.red);
				}else if (result > 0){
					setPaneText(output, newOutput, Color.blue);
				}else if (result == 0) {
					setPaneText(output, "Detected Codification:" + System.lineSeparator() + "	", Color.blue);
					appendToPane(output, newOutput, new Color (24, 156, 24));
					
				}
				
				
			}
		});
	}
	
	private void setPaneText(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.FontSize, 16);
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        tp.setCaretPosition(0);
        tp.setCharacterAttributes(aset, false);
        tp.setText(msg);
    }
	
	
	private void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.FontSize, 16);
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        try {
			tp.getStyledDocument().insertString(len, msg, aset);
		} catch (BadLocationException e) {
			System.err.println("Eat a dick!");
		}
    }
	
	private class PathTransferHandler extends TransferHandler{
		
		@Override
		public boolean canImport (TransferSupport support) {
			if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
		        return false;
		    }
		    return true;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public boolean importData(TransferSupport support) {
		    if (!canImport(support)) {
		        return false;
		    }

		    // Fetch the Transferable and its data
		    Transferable t = support.getTransferable();
		    List<File> dropppedFiles;
		    try {
				dropppedFiles= (List<File>)t.getTransferData(DataFlavor.javaFileListFlavor);
		    	if (dropppedFiles.size() > 1) {
		    		JOptionPane.showMessageDialog(null, "Please drop 1 file at a time");
		    		return false;
		    	}
		    }catch (ClassCastException | UnsupportedFlavorException | IOException e){
		    	return false;
		    }

		    // Insert the data at this location
		    mainW.setFile(dropppedFiles.get(0));

		    return true;
		}
		
		
	}
	
	
	
}

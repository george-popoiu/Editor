import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import javax.imageio.stream.FileImageInputStream;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class Editor extends JFrame implements ActionListener, DocumentListener {

	public static void main(String[] args) {
		new Editor();
	}
	
	private JEditorPane textPane;
	private JMenuBar menu;
	private JMenuItem copy, paste, cut;
	private boolean changed = false;
	private File file;
	
	public Editor() {
		super("Editor");
		textPane = new JEditorPane();
		add(new JScrollPane(textPane), BorderLayout.CENTER);
		textPane.getDocument().addDocumentListener(this);
		
		menu = new JMenuBar();
		setJMenuBar(menu);
		buildMenu();		
		
		setSize(500, 500);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void buildMenu() {		
		buildFileMenu();
		buildEditMenu();
	}
	
	private void buildFileMenu() {
		JMenu file = new JMenu("File");
		file.setMnemonic('F');
		menu.add(file);
		JMenuItem n = new JMenuItem("New");
		n.setMnemonic('N');
		n.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		n.addActionListener(this);
		file.add(n);
		JMenuItem open = new JMenuItem("Open");
		file.add(open);
		open.addActionListener(this);
		open.setMnemonic('O');
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		JMenuItem save = new JMenuItem("Save");
		file.add(save);
		save.setMnemonic('S');
		save.addActionListener(this);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		JMenuItem quit = new JMenuItem("Quit");
		file.add(quit);
		quit.addActionListener(this);
		quit.setMnemonic('Q');
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
	}
	
	private void buildEditMenu() {		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if( action.equals("Quit") ) {
			System.exit(0);
		}
		else if(action.equals("Open")) {			
			loadFile();
		}
		else if(action.equals("Save")) {
			saveFile();
		}
		else if(action.equals("New")) {
			newFile();
		}
	}
	
	private void newFile() {
		if(changed) saveFile();		
		file = null;
		textPane.setText("");
		changed = false;
	}
	
	private void loadFile() {
		JFileChooser dialog = new JFileChooser(System.getProperty("user.home"));
		dialog.setMultiSelectionEnabled(false);
		try {
			int result = dialog.showOpenDialog(this);
			if( result==JFileChooser.CANCEL_OPTION ) return;
			if( result==JFileChooser.APPROVE_OPTION ) {
				if(changed) saveFile();
				file = dialog.getSelectedFile();				
				textPane.setText(readFile(file));
				changed = false;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private String readFile(File file) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));	
			String line;
			while( (line = reader.readLine()) != null) {
				result.append(line+"\n");
			}
			reader.close();			
		} 
		catch (IOException e) {		
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Cannot read file !", "Error !", 
					JOptionPane.ERROR_MESSAGE);			
		}
		return result.toString();
	}
	
	private void saveFile() {		
		if(changed) {
			int ans = JOptionPane.showConfirmDialog(null, 
					"The file has changed. You want to save it?", 
					"Save file", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if(ans==JOptionPane.NO_OPTION ) return;
		}
		if( file==null ) {
			saveAs();
			return;
		}
		String text = textPane.getText();
		System.out.println(text);
		try {
			if(!file.canWrite()) throw new Exception("Cannot write file!");
			PrintWriter writer = new PrintWriter(file);			
			writer.write(text);
			writer.close();
			changed = false;
		} 
		catch (Exception e) {		
			e.printStackTrace();			
		}
	}
	
	private void saveAs() {
		JFileChooser dialog = new JFileChooser(System.getProperty("user.home"));
		int result = dialog.showSaveDialog(this);
		if( result != JFileChooser.APPROVE_OPTION ) return;
		file = dialog.getSelectedFile();
		try {
			PrintWriter writer = new PrintWriter(file);
			writer.write(textPane.getText());
			writer.close();
			changed = false;
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		changed = true;
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		changed = true;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		changed = true;
	}

}

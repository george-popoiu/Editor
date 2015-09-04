package editor;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class Editor extends JFrame implements ActionListener, DocumentListener {

	public static void main(String[] args) {
		new Editor();
	}
	
	public JEditorPane textPane;
	private final JMenuBar menu;
        private final JPanel toolbars;
        private final JToolBar fileToolbar;
        private final JToolBar editToolbar;
	private JMenuItem copy, paste, cut;
	public boolean changed = false;
	private File file;
        
        private String applicationTitle = "Basic Java Text Editor";
        
        //setup icons - File Menu
        private final ImageIcon newIcon = new ImageIcon("icons/new.png");
        private final ImageIcon openIcon = new ImageIcon("icons/open.png");
        private final ImageIcon saveIcon = new ImageIcon("icons/save.png");
        private final ImageIcon saveAsIcon = new ImageIcon("icons/save_as.png");
        private final ImageIcon quitIcon = new ImageIcon("icons/quit.png");
        
        //setup icons - Edit Menu
        private final ImageIcon cutIcon = new ImageIcon("icons/cut.png");
        private final ImageIcon copyIcon = new ImageIcon("icons/copy.png");
        private final ImageIcon pasteIcon = new ImageIcon("icons/paste.png");
        private final ImageIcon findIcon = new ImageIcon("icons/find.png");
        private final ImageIcon selectAllIcon = new ImageIcon("icons/select_all.png");
        
        //setup icons - Helo Menu
        private final ImageIcon aboutIcon = new ImageIcon("icons/about.png");
	
	public Editor() {
		super("Editor");
		textPane = new JEditorPane();
		add(new JScrollPane(textPane), BorderLayout.CENTER);
		textPane.getDocument().addDocumentListener(this);
		
		menu = new JMenuBar();
		setJMenuBar(menu);
		buildMenu();
                
                fileToolbar = new JToolBar();
                buildFileToolBar();
                
                editToolbar = new JToolBar();
                buildEditToolBar();
		
                //add both toolbars to a pannel
                toolbars = new JPanel( new GridLayout(0, 1) );
                toolbars.add(fileToolbar);
                toolbars.add(editToolbar);
                add(toolbars, BorderLayout.NORTH);
                
		setSize(500, 500);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void buildMenu() {		
		buildFileMenu();
		buildEditMenu();
                buildHelpMenu();
	}
	
	private void buildFileMenu() {
       
		JMenu file = new JMenu("File");
		file.setMnemonic('F');
		menu.add(file);
		JMenuItem n = new JMenuItem("New", newIcon);
		n.setMnemonic('N');
		n.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		n.addActionListener(this);
		file.add(n);
		JMenuItem open = new JMenuItem("Open", openIcon);
		file.add(open);
		open.addActionListener(this);
		open.setMnemonic('O');
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		JMenuItem save = new JMenuItem("Save", saveIcon);
		file.add(save);
		save.setMnemonic('S');
		save.addActionListener(this);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		JMenuItem saveas = new JMenuItem("Save as...", saveAsIcon);
		saveas.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		file.add(saveas);
		saveas.addActionListener(this);		
		JMenuItem quit = new JMenuItem("Quit", quitIcon);
		file.add(quit);
		quit.addActionListener(this);
		quit.setMnemonic('Q');
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
	}
	
	private void buildEditMenu() {

		JMenu edit = new JMenu("Edit");
		menu.add(edit);
		edit.setMnemonic('E');		
		//cut
		cut = new JMenuItem("Cut", cutIcon);
		cut.addActionListener(this);
		cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
		cut.setMnemonic('T');
		edit.add(cut);
		//copy
		copy = new JMenuItem("Copy", copyIcon);
		copy.addActionListener(this);
		copy.setMnemonic('C');
		copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
		edit.add(copy);
		//paste
		paste = new JMenuItem("Paste", pasteIcon);
		paste.setMnemonic('P');
		paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
		edit.add(paste);
		paste.addActionListener(this);		
		//find
		JMenuItem find = new JMenuItem("Find", findIcon);
		find.setMnemonic('F');
		find.addActionListener(this);
		edit.add(find);
		find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
		//select all
		JMenuItem sall = new JMenuItem("Select All", selectAllIcon);
		sall.setMnemonic('A');
		sall.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
		sall.addActionListener(this);
		edit.add(sall);
	}
        
        private void buildHelpMenu() {
            
            JMenu help = new JMenu("Help");
            menu.add(help);
            help.setMnemonic('h');		

            //cut
            JMenuItem about = new JMenuItem("About", aboutIcon);
            about.addActionListener(this);
            about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
            about.setMnemonic('A');
            help.add(about);
        }
        
        private void buildFileToolBar() {
                
                JButton newButton = new JButton(newIcon);
                newButton.setActionCommand("New");
                newButton.setToolTipText("New");
                newButton.addActionListener(this);
                fileToolbar.add(newButton);
                fileToolbar.addSeparator();
                
                JButton openButton = new JButton(openIcon);
                openButton.setActionCommand("Open");
                openButton.setToolTipText("Open");
                openButton.addActionListener(this);
                fileToolbar.add(openButton);
                fileToolbar.addSeparator();
                
                JButton saveButton = new JButton(saveIcon);
                saveButton.setActionCommand("Save");
                saveButton.setToolTipText("Save");
                saveButton.addActionListener(this);
                fileToolbar.add(saveButton);
                fileToolbar.addSeparator();
                
                JButton saveAsButton = new JButton(saveAsIcon);
                saveAsButton.setActionCommand("Save as...");
                saveAsButton.setToolTipText("Save as...");
                saveAsButton.addActionListener(this);
                fileToolbar.add(saveAsButton);
                fileToolbar.addSeparator();
                
                JButton quitButton = new JButton(quitIcon);
                quitButton.setActionCommand("Quit");
                quitButton.setToolTipText("Quit");
                quitButton.addActionListener(this);
                fileToolbar.add(quitButton);
        }
        
        private void buildEditToolBar() {

                JButton cutButton = new JButton(cutIcon);
                cutButton.setActionCommand("Cut");
                cutButton.setToolTipText("Cut");
                cutButton.addActionListener(this);
                editToolbar.add(cutButton);
                editToolbar.addSeparator();
                
                JButton copyButton = new JButton(copyIcon);
                copyButton.setActionCommand("Copy");
                copyButton.setToolTipText("Copy");
                copyButton.addActionListener(this);
                editToolbar.add(copyButton);
                editToolbar.addSeparator();
                
                JButton pasteButton = new JButton(pasteIcon);
                pasteButton.setActionCommand("Paste");
                pasteButton.setToolTipText("Paste");
                pasteButton.addActionListener(this);
                editToolbar.add(pasteButton);
                editToolbar.addSeparator();
                
                JButton findButton = new JButton(findIcon);
                findButton.setActionCommand("Find");
                findButton.setToolTipText("Find");
                findButton.addActionListener(this);
                editToolbar.add(findButton);
                editToolbar.addSeparator();
                
                JButton selectAllButton = new JButton(selectAllIcon);
                selectAllButton.setActionCommand("Select All");
                selectAllButton.setToolTipText("Select All");
                selectAllButton.addActionListener(this);
                editToolbar.add(selectAllButton);
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
		else if(action.equals("Save as...")) {
			saveAs("Save as...");
		}
		else if(action.equals("Select All")) {
			textPane.selectAll();			
		}	
		else if(action.equals("Copy")) {
			textPane.copy();
		}
		else if(action.equals("Cut")) {
			textPane.cut();
		}
		else if( action.equals("Paste") ) {
			textPane.paste();			
		}
		else if(action.equals("Find")) {
			FindDialog find = new FindDialog(this, true);
			find.showDialog();
		}
                else if(action.equals("About")) {
			JOptionPane.showMessageDialog(this, getAboutText(), applicationTitle + " - About", JOptionPane.INFORMATION_MESSAGE, aboutIcon);
                }
	}
        
        private String getAboutText() {
                String aboutText; 
                aboutText = "Base Source From: https://github.com/george-popoiu/Editor\n";
                aboutText += "Improved by Achintha Gunasekara : https://github.com/achinthagunasekara/Editor\n";
                aboutText += "\n\nAchintha Gunasekara\n";
                aboutText += "contact@achinthagunasekara.com\n";
                aboutText += "http://www.achinthagunasekara.com";
                return aboutText;
        }
	
	private void newFile() {
		if(changed) saveFile();		
		file = null;
		textPane.setText("");
		changed = false;
		setTitle("Editor");
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
				setTitle("Editor - " + file.getName());
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
			saveAs("Save");
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
	
	private void saveAs(String dialogTitle) {
		JFileChooser dialog = new JFileChooser(System.getProperty("user.home"));
		dialog.setDialogTitle(dialogTitle);
		int result = dialog.showSaveDialog(this);
		if( result != JFileChooser.APPROVE_OPTION ) return;
		file = dialog.getSelectedFile();
		try {
			PrintWriter writer = new PrintWriter(file);
			writer.write(textPane.getText());
			writer.close();
			changed = false;
			setTitle("Editor - " + file.getName());
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

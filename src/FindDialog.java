import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class FindDialog extends JDialog implements ActionListener{	
	
	Editor parent;
	JLabel label;
	JTextField textField;
	JCheckBox caseSensitive;
	JButton find, close;
	
	public FindDialog(Editor parent, boolean modal) {
		super(parent, modal);
		this.parent = parent;
		initComponents();
		setTitle("Find");
		setLocationRelativeTo(parent);
		pack();		
	}
	
	public void showDialog() {
		setVisible(true);
	}
	
	private void initComponents() {
		setLayout(new GridLayout(3, 1));
		JPanel panel1 = new JPanel();
		label = new JLabel("Find : ");
		label.setDisplayedMnemonic('F');
		panel1.add(label);
		textField = new JTextField(15);
		panel1.add(textField);
		label.setLabelFor(textField);
		add(panel1);
		JPanel panel2 = new JPanel();
		caseSensitive = new JCheckBox("Case sensitive");
		panel2.add(caseSensitive);
		add(panel2);
		JPanel panel3 = new JPanel();
		find = new JButton("Find");
		close = new JButton("Close");
		find.addActionListener(this);
		close.addActionListener(this);
		panel3.add(find);
		panel3.add(close);
		add(panel3);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("Find")) {
			
		}
		else if(cmd.equals("Close")) {
			setVisible(false);
			dispose();
		}
	}
	
}

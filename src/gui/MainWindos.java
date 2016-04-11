package gui;

import core.*;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenu;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

public class MainWindos extends JDialog {
	static JComboBox list;
	static JTextPane consoleResults;
	static JPanel buttonPane;
	
	int funct;
	static int mapperID;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			MainWindos dialog = new MainWindos();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public MainWindos() {
		setResizable(false);
		JButton okButton;
		setBounds(100, 100, 450, 357);
		getContentPane().setLayout(null);
		{
			buttonPane = new JPanel();
			buttonPane.setBounds(0, 274, 434, 33);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						System.exit(0);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		
			JLabel lblNewLabel = new JLabel("Select Role:");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel.setBounds(103, 21, 223, 29);
			getContentPane().add(lblNewLabel);
		
			consoleResults = new JTextPane();
			consoleResults.setBounds(10, 134, 424, 129);
			getContentPane().add(consoleResults);
			
			String[] choices = {"Mapper", "Reducer", "Client"};
			list = new JComboBox(choices);
			list.setBounds(147, 61, 126, 20);
			
			getContentPane().add(list);
			
			JRadioButton mapper1 = new JRadioButton("Mapper 1");
			buttonGroup.add(mapper1);
			mapper1.setBounds(6, 104, 109, 23);
			mapper1.setVisible(false);
			getContentPane().add(mapper1);
			
			JRadioButton mapper2 = new JRadioButton("Mapper 2");
			buttonGroup.add(mapper2);
			mapper2.setBounds(164, 104, 109, 23);
			mapper2.setVisible(false);
			getContentPane().add(mapper2);
			
			JRadioButton mapper3 = new JRadioButton("Mapper 3");
			buttonGroup.add(mapper3);
			mapper3.setBounds(319, 104, 109, 23);
			mapper3.setVisible(false);
			getContentPane().add(mapper3);
			
			JLabel label = new JLabel("Select Mapper");
			label.setVisible(false);
			label.setFont(new Font("Tahoma", Font.ITALIC, 14));
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setBounds(103, 86, 223, 20);
			getContentPane().add(label);
			list.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String choice = list.getSelectedItem().toString();
					if (choice.equals("Client")) {
						funct = 1;
					}
					else if (choice.equals("Mapper")) {
						funct = 2;
						label.setVisible(true);
						mapper1.setVisible(true);
						mapper2.setVisible(true);
						mapper3.setVisible(true);
					}
					else if (choice.equals("Reducer")) {
						funct = 3;
					}
				}
				});
			
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (mapper1.isSelected()) {
						mapperID = 1;
					}
					else if (mapper2.isSelected()) {
						mapperID = 2;
					}
					else if (mapper3.isSelected()) {
						mapperID = 3;
					}
					label.setVisible(false);
					mapper1.setVisible(false);
					mapper2.setVisible(false);
					mapper3.setVisible(false);
					okButton.setVisible(false);
					if (funct == 1) {
						ClientGUI client = new ClientGUI();
						client.setVisible(true);
					}
					else {
						guiThread(funct, mapperID, consoleResults);
					}
				}
			});
			
		}
	}
	
	private static void guiThread(int funct, int id, JTextPane console) {
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				rolesMsgs(funct);
				MasterGUI master = new MasterGUI(funct, id);				
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}
	
	private static void rolesMsgs(int funct) {
		if (funct == 1) {
			consoleResults.setText("A new window will open for client side...");
		}
		else if (funct == 2)
			consoleResults.setText("Mapper "+ mapperID +" has initialized and waits for requests...");
		else 
			consoleResults.setText("Reducer has initialized and waits for intermediate data...");
		}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}

package gui;

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

public class MainWindos extends JDialog {
	static JComboBox list;
	private JTextField input;
	int funct, mapperID;
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
		JButton okButton;
		setBounds(100, 100, 450, 357);
		getContentPane().setLayout(null);
		{
			JPanel buttonPane = new JPanel();
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
		
			JTextPane consoleResults = new JTextPane();
			consoleResults.setBounds(10, 90, 424, 129);
			getContentPane().add(consoleResults);
			
			input = new JTextField();
			input.setBounds(10, 243, 112, 20);
			getContentPane().add(input);
			input.setColumns(10);
			
			JLabel label = new JLabel("Enter Input Here:");
			input.setVisible(false);
			label.setVisible(false);
			label.setBounds(10, 230, 112, 14);
			getContentPane().add(label);
			
			String[] choices = {"Mapper", "Reducer", "Client"};
			list = new JComboBox(choices);
			list.setBounds(147, 61, 126, 20);
			
			getContentPane().add(list);
			
			JButton setInput = new JButton("Go");
			setInput.setBounds(132, 240, 89, 23);
			setInput.setVisible(false);
			getContentPane().add(setInput);
			list.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String choice = list.getSelectedItem().toString();
					
					if (choice.equals("Mapper")) {
						funct = 2;
						label.setVisible(true);
						input.setVisible(true);
						setInput.setVisible(true);;
						String msg = "Select mapper from 1 to 3\n>";
						consoleResults.setText(msg);
						setInput.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								mapperID = Integer.parseInt(input.getText());		
								consoleResults.setText(msg+mapperID);
							}
						});						
					}
					if (choice.equals("Reudcer")) {
						String msg = "Running on local port "+reducer.getLocalPort()+" and waiting for connections..";
						consoleResults.setText(msg);
					}
				}
				});
			
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					guiThread(funct, mapperID, consoleResults);
				}
			});
			
		}
	}
	
	private static void guiThread(int funct, int id, JTextPane console) {
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				MasterGUI master = new MasterGUI(funct, id, console);				
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
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

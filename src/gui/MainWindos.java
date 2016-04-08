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

public class MainWindos extends JDialog {
	static JComboBox list;
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
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 205, 434, 33);
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
		
			String[] choices = {"Mapper", "Reducer", "Client"};
			list = new JComboBox(choices);
			list.setBounds(147, 61, 126, 20);
			
			getContentPane().add(list);
			
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					guiThread();
				}
			});
		
		
			JMenuBar menuBar = new JMenuBar();
			setJMenuBar(menuBar);
			{
				JMenuItem mntmNewMenuItem = new JMenuItem("File");
				menuBar.add(mntmNewMenuItem);
			}
			{
				JMenuItem mntmAbout = new JMenuItem("About");
				menuBar.add(mntmAbout);
			}
			
		}
	}
	
	private static void guiThread() {
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				String choice = list.getSelectedItem().toString();
				
				if (choice.equals("Mapper")) {
					MasterGUI master = new MasterGUI(2);
				}
				else if (choice.equals("Reducer")) {
					MasterGUI master = new MasterGUI(3);
				}
				else if (choice.equals("Client")) {
					MasterGUI master = new MasterGUI(1);
				}
				
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

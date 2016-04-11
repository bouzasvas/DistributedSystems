package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JSeparator;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class ClientGUI extends JDialog {

	private static final JPanel window = new JPanel();
	private static JTextField minLongText;
	private static JTextField maxLongText;
	private static JTextField minLatText;
	private static JTextField maxLatText;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private static JTextField minDateText;
	private static JTextField maxDateText;
	private static JTextPane results;
	private static JButton okButton;
	private static JTextField topK;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ClientGUI dialog = new ClientGUI();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ClientGUI() {
		setResizable(false);
		setBounds(100, 100, 647, 372);
		getContentPane().setLayout(new BorderLayout());
		window.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(window, BorderLayout.CENTER);
		window.setLayout(null);
		
		JLabel welcomeText = new JLabel("Client Side");
		welcomeText.setFont(new Font("Tahoma", Font.PLAIN, 20));
		welcomeText.setBounds(10, 11, 114, 28);
		window.add(welcomeText);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 50, 641, 2);
		window.add(separator);
		
		JLabel defaultLabel = new JLabel("Default Values?");
		defaultLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		defaultLabel.setBounds(10, 63, 114, 14);
		window.add(defaultLabel);
		
		JRadioButton yes = new JRadioButton("Yes");
		buttonGroup.add(yes);
		yes.setBounds(6, 83, 73, 23);
		window.add(yes);
		
		JRadioButton no = new JRadioButton("No");
		buttonGroup.add(no);
		no.setBounds(81, 83, 55, 23);
		window.add(no);
		
		
		
		minLongText = new JTextField();
		minLongText.setBounds(10, 111, 151, 20);
		window.add(minLongText);
		minLongText.setColumns(10);
		
		maxLongText = new JTextField();
		maxLongText.setBounds(10, 133, 151, 20);
		window.add(maxLongText);
		maxLongText.setColumns(10);
		
		minLatText = new JTextField();
		minLatText.setBounds(10, 155, 151, 20);
		window.add(minLatText);
		minLatText.setColumns(10);
		
		maxLatText = new JTextField();
		maxLatText.setBounds(10, 178, 151, 20);
		window.add(maxLatText);
		maxLatText.setColumns(10);
		
		JLabel minLong = new JLabel("Min Longitude");
		minLong.setFont(new Font("Tahoma", Font.PLAIN, 11));
		minLong.setBounds(171, 114, 73, 14);
		window.add(minLong);
		
		JLabel maxLong = new JLabel("Max Longitude");
		maxLong.setFont(new Font("Tahoma", Font.PLAIN, 11));
		maxLong.setBounds(171, 136, 73, 14);
		window.add(maxLong);
		
		JLabel minLat = new JLabel("Min Latitude");
		minLat.setFont(new Font("Tahoma", Font.PLAIN, 11));
		minLat.setBounds(171, 158, 73, 14);
		window.add(minLat);
		
		JLabel maxLat = new JLabel("Max Latitude");
		maxLat.setFont(new Font("Tahoma", Font.PLAIN, 11));
		maxLat.setBounds(171, 181, 73, 14);
		window.add(maxLat);
		
		minDateText = new JTextField();
		minDateText.setBounds(10, 208, 151, 20);
		window.add(minDateText);
		minDateText.setColumns(10);
		
		JLabel minDate = new JLabel("From Date");
		minDate.setBounds(171, 211, 73, 14);
		window.add(minDate);
		
		maxDateText = new JTextField();
		maxDateText.setBounds(10, 237, 151, 20);
		window.add(maxDateText);
		maxDateText.setColumns(10);
		
		JLabel maxDate = new JLabel("To Date");
		maxDate.setBounds(171, 240, 73, 14);
		window.add(maxDate);
		
		JScrollPane scrollResults = new JScrollPane();
		scrollResults.setBounds(281, 83, 340, 174);
		window.add(scrollResults);
		
		results = new JTextPane();
		scrollResults.setViewportView(results);
		
		JLabel topkLabel = new JLabel("Select the top-K results");
		topkLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		topkLabel.setHorizontalAlignment(SwingConstants.LEFT);
		topkLabel.setBounds(10, 274, 162, 25);
		window.add(topkLabel);
		
		topK = new JTextField();
		topK.setToolTipText("");
		topK.setBounds(192, 278, 86, 20);
		window.add(topK);
		topK.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (topK == null) {
							results.setText("Top-K field must have a value");
						}							
						if (yes.isSelected()) {
							minLongText.setText("-74.0144996501386");
							maxLongText.setText("-73.9018372248612");
							minLatText.setText("40.67747711364791");
							maxLatText.setText("40.76662365086325");
							minDateText.setText("2012-05-09 00:00:00");
							maxDateText.setText("2012-11-06 23:59:00");
							guiThread(1);
						}
						if (no.isSelected()) {
							guiThread(0);
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						System.exit(1);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	private static void guiThread(int def) {
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				okButton.setText("Save Results");
				okButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						File resFile = null;
						FileWriter fw = null;
						try {
							resFile = new File("results.txt");
							String text = results.getText();
							fw = new FileWriter(resFile);
							fw.write(text);
							JOptionPane.showMessageDialog(window, "Results saved successfully to file \"results.txt\"");
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						finally {
							try {
								fw.close();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				});
				if (def == 1) {
					MasterGUI master = new MasterGUI(1, def, Integer.valueOf(topK.getText()));
					master.client.printResults(results);
				}
				else {
					MasterGUI master = new MasterGUI(1, def, Integer.valueOf(topK.getText()));
					master.client.queryValues(false, Double.parseDouble(minLongText.getText()), Double.parseDouble(maxLongText.getText())
							, Double.parseDouble(minLatText.getText()), Double.parseDouble(maxLatText.getText()), minDateText.getText(), maxDateText.getText());
				}
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}
}

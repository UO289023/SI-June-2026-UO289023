package uo289023.si26.calculaterate;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class CalculateRateView {

	private JFrame frame;
	private JComboBox<String> cbServiceLevel;
	private JComboBox<String> cbZone;
	private JTextField txtWeight;
	private JButton btnCalculate;
	private JLabel lblResult;
	private JTable tableRates;

	public CalculateRateView() {
		frame = new JFrame();
		frame.setTitle("Calculate Shipment Rate");
		frame.setBounds(200, 150, 640, 520);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(5, 5));

		JPanel formPanel = new JPanel(new GridLayout(0, 2, 8, 6));
		formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

		cbServiceLevel = new JComboBox<>(new String[] { "STANDARD", "EXPRESS" });
		cbZone = new JComboBox<>(new String[] { "LOCAL", "REGIONAL", "NATIONAL" });
		txtWeight = new JTextField();
		btnCalculate = new JButton("Calculate Rate");
		lblResult = new JLabel("Rate: -");
		lblResult.setFont(new Font("Segoe UI", Font.BOLD, 14));

		formPanel.add(new JLabel("<html>Service level <font color='red'>*</font></html>"));
		formPanel.add(cbServiceLevel);
		formPanel.add(new JLabel("<html>Destination zone <font color='red'>*</font></html>"));
		formPanel.add(cbZone);
		formPanel.add(new JLabel("<html>Package weight (kg) <font color='red'>*</font></html>"));
		formPanel.add(txtWeight);
		formPanel.add(btnCalculate);
		formPanel.add(lblResult);

		frame.getContentPane().add(formPanel, BorderLayout.NORTH);

		JPanel ratesPanel = new JPanel(new BorderLayout());
		ratesPanel.setBorder(BorderFactory.createTitledBorder("Rate Table"));
		tableRates = new JTable();
		ratesPanel.add(new JScrollPane(tableRates), BorderLayout.CENTER);
		frame.getContentPane().add(ratesPanel, BorderLayout.CENTER);
	}

	public JFrame getFrame() {
		return frame;
	}

	public JComboBox<String> getCbServiceLevel() {
		return cbServiceLevel;
	}

	public JComboBox<String> getCbZone() {
		return cbZone;
	}

	public JTextField getTxtWeight() {
		return txtWeight;
	}

	public JButton getBtnCalculate() {
		return btnCalculate;
	}

	public JLabel getLblResult() {
		return lblResult;
	}

	public JTable getTableRates() {
		return tableRates;
	}
}

package uo289023.si26.verifypackage;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class VerifyPackageView {

	private JFrame frame;
	private JComboBox<String> cbWarehouse;
	private JComboBox<String> cbOperation;
	private JTextField txtBarcode;
	private JButton btnSearch;
	private JTable tablePackageInfo;
	private JCheckBox chkVisualOk;
	private JTextField txtMeasuredWeight;
	private JButton btnRegisterVerification;

	public VerifyPackageView() {
		frame = new JFrame();
		frame.setTitle("Verify Package at Warehouse");
		frame.setBounds(200, 150, 640, 520);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(5, 5));

		JPanel searchPanel = new JPanel(new GridLayout(0, 2, 8, 6));
		searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

		cbWarehouse = new JComboBox<>();
		cbOperation = new JComboBox<>(new String[] { "LOAD", "UNLOAD" });
		txtBarcode = new JTextField();
		btnSearch = new JButton("Read Barcode");

		searchPanel.add(new JLabel("<html>Warehouse <font color='red'>*</font></html>"));
		searchPanel.add(cbWarehouse);
		searchPanel.add(new JLabel("<html>Operation <font color='red'>*</font></html>"));
		searchPanel.add(cbOperation);
		searchPanel.add(new JLabel("<html>Package barcode <font color='red'>*</font></html>"));
		searchPanel.add(txtBarcode);
		searchPanel.add(new JLabel(""));
		searchPanel.add(btnSearch);

		frame.getContentPane().add(searchPanel, BorderLayout.NORTH);

		JPanel infoPanel = new JPanel(new BorderLayout());
		infoPanel.setBorder(BorderFactory.createTitledBorder("Package Information"));
		tablePackageInfo = new JTable();
		infoPanel.add(new JScrollPane(tablePackageInfo), BorderLayout.CENTER);
		frame.getContentPane().add(infoPanel, BorderLayout.CENTER);

		JPanel checksPanel = new JPanel(new GridLayout(0, 2, 8, 6));
		checksPanel.setBorder(BorderFactory.createTitledBorder("Verification Checks"));

		chkVisualOk = new JCheckBox("No breaks or damage to the packaging");
		txtMeasuredWeight = new JTextField();
		btnRegisterVerification = new JButton("Register Verification");

		checksPanel.add(new JLabel("Visual inspection:"));
		checksPanel.add(chkVisualOk);
		checksPanel.add(new JLabel("<html>Measured weight (kg) <font color='red'>*</font></html>"));
		checksPanel.add(txtMeasuredWeight);
		checksPanel.add(new JLabel(""));
		checksPanel.add(btnRegisterVerification);

		frame.getContentPane().add(checksPanel, BorderLayout.SOUTH);
	}

	public JFrame getFrame() {
		return frame;
	}

	public JComboBox<String> getCbWarehouse() {
		return cbWarehouse;
	}

	public JComboBox<String> getCbOperation() {
		return cbOperation;
	}

	public JTextField getTxtBarcode() {
		return txtBarcode;
	}

	public JButton getBtnSearch() {
		return btnSearch;
	}

	public JTable getTablePackageInfo() {
		return tablePackageInfo;
	}

	public JCheckBox getChkVisualOk() {
		return chkVisualOk;
	}

	public JTextField getTxtMeasuredWeight() {
		return txtMeasuredWeight;
	}

	public JButton getBtnRegisterVerification() {
		return btnRegisterVerification;
	}
}

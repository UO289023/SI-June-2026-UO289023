package uo289023.si26.modifydeliveryplace;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

public class ModifyDeliveryPlaceView {

	private JFrame frame;
	private JComboBox<String> cbCustomer;
	private JTable tableShipments;
	private JRadioButton rbNewAddress;
	private JRadioButton rbLocation;
	private JTextField txtNewAddress;
	private JTextField txtNewCity;
	private JComboBox<String> cbLocation;
	private JButton btnApply;

	public ModifyDeliveryPlaceView() {
		frame = new JFrame();
		frame.setTitle("Modify Delivery Place");
		frame.setBounds(200, 120, 720, 560);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(5, 5));

		JPanel topPanel = new JPanel(new BorderLayout(5, 0));
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
		topPanel.add(new JLabel("Customer:"), BorderLayout.WEST);
		cbCustomer = new JComboBox<>();
		topPanel.add(cbCustomer, BorderLayout.CENTER);
		frame.getContentPane().add(topPanel, BorderLayout.NORTH);

		JPanel shipmentsPanel = new JPanel(new BorderLayout());
		shipmentsPanel.setBorder(BorderFactory.createTitledBorder("Active Shipments"));
		tableShipments = new JTable();
		tableShipments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		shipmentsPanel.add(new JScrollPane(tableShipments), BorderLayout.CENTER);
		frame.getContentPane().add(shipmentsPanel, BorderLayout.CENTER);

		JPanel changePanel = new JPanel(new GridLayout(0, 2, 8, 6));
		changePanel.setBorder(BorderFactory.createTitledBorder("New Delivery Place"));

		rbNewAddress = new JRadioButton("Deliver to a different address", true);
		rbLocation = new JRadioButton("Deliver to an office/warehouse");
		ButtonGroup group = new ButtonGroup();
		group.add(rbNewAddress);
		group.add(rbLocation);

		txtNewAddress = new JTextField();
		txtNewCity = new JTextField();
		cbLocation = new JComboBox<>();
		btnApply = new JButton("Apply Change");

		changePanel.add(rbNewAddress);
		changePanel.add(rbLocation);
		changePanel.add(new JLabel("<html>New address <font color='red'>*</font></html>"));
		changePanel.add(new JLabel("<html>Office/warehouse <font color='red'>*</font></html>"));
		changePanel.add(txtNewAddress);
		changePanel.add(cbLocation);
		changePanel.add(new JLabel("<html>New city <font color='red'>*</font></html>"));
		changePanel.add(new JLabel(""));
		changePanel.add(txtNewCity);
		changePanel.add(btnApply);

		frame.getContentPane().add(changePanel, BorderLayout.SOUTH);
	}

	public JFrame getFrame() {
		return frame;
	}

	public JComboBox<String> getCbCustomer() {
		return cbCustomer;
	}

	public JTable getTableShipments() {
		return tableShipments;
	}

	public JRadioButton getRbNewAddress() {
		return rbNewAddress;
	}

	public JRadioButton getRbLocation() {
		return rbLocation;
	}

	public JTextField getTxtNewAddress() {
		return txtNewAddress;
	}

	public JTextField getTxtNewCity() {
		return txtNewCity;
	}

	public JComboBox<String> getCbLocation() {
		return cbLocation;
	}

	public JButton getBtnApply() {
		return btnApply;
	}
}

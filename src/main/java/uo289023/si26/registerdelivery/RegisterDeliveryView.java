package uo289023.si26.registerdelivery;

import java.awt.BorderLayout;
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
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

public class RegisterDeliveryView {

	private JFrame frame;
	private JTable tableShipments;
	private JComboBox<String> cbResult;
	private JTextField txtNotes;
	private JButton btnRegisterDelivery;

	public RegisterDeliveryView() {
		frame = new JFrame();
		frame.setTitle("Register Package Delivery");
		frame.setBounds(200, 120, 760, 520);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(5, 5));

		JPanel shipmentsPanel = new JPanel(new BorderLayout());
		shipmentsPanel.setBorder(BorderFactory.createTitledBorder("Shipments Pending Delivery"));
		tableShipments = new JTable();
		tableShipments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		shipmentsPanel.add(new JScrollPane(tableShipments), BorderLayout.CENTER);
		frame.getContentPane().add(shipmentsPanel, BorderLayout.CENTER);

		JPanel deliveryPanel = new JPanel(new GridLayout(0, 2, 8, 6));
		deliveryPanel.setBorder(BorderFactory.createTitledBorder("Delivery Result"));

		cbResult = new JComboBox<>(new String[] { "DELIVERED", "ABSENT" });
		txtNotes = new JTextField();
		btnRegisterDelivery = new JButton("Register Delivery");

		deliveryPanel.add(new JLabel("Result:"));
		deliveryPanel.add(cbResult);
		deliveryPanel.add(new JLabel("Notes:"));
		deliveryPanel.add(txtNotes);
		deliveryPanel.add(new JLabel(""));
		deliveryPanel.add(btnRegisterDelivery);

		frame.getContentPane().add(deliveryPanel, BorderLayout.SOUTH);
	}

	public JFrame getFrame() {
		return frame;
	}

	public JTable getTableShipments() {
		return tableShipments;
	}

	public JComboBox<String> getCbResult() {
		return cbResult;
	}

	public JTextField getTxtNotes() {
		return txtNotes;
	}

	public JButton getBtnRegisterDelivery() {
		return btnRegisterDelivery;
	}
}

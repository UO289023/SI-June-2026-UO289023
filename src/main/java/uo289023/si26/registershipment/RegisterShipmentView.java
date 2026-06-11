package uo289023.si26.registershipment;

import java.awt.BorderLayout;
import java.awt.Font;
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
import javax.swing.table.DefaultTableModel;

public class RegisterShipmentView {

	private JFrame frame;
	private JComboBox<String> cbCustomer;
	private JTextField txtRecipientName;
	private JTextField txtRecipientPhone;
	private JComboBox<String> cbOriginOffice;
	private JCheckBox chkHomePickup;
	private JTextField txtPickupAddress;
	private JTextField txtPickupCity;
	private JComboBox<String> cbDestinationLocation;
	private JTextField txtDeliveryAddress;
	private JTextField txtDeliveryCity;
	private JComboBox<String> cbZone;
	private JComboBox<String> cbServiceLevel;
	private JTextField txtPackageWeight;
	private JTextField txtPackageDescription;
	private JButton btnAddPackage;
	private JButton btnRemovePackage;
	private JTable tablePackages;
	private DefaultTableModel packagesModel;
	private JLabel lblTotalPrice;
	private JButton btnRegister;

	@SuppressWarnings("serial")
	public RegisterShipmentView() {
		frame = new JFrame();
		frame.setTitle("Register Shipment by Telephone");
		frame.setBounds(150, 100, 760, 640);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(5, 5));

		JPanel formPanel = new JPanel(new GridLayout(0, 2, 8, 6));
		formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

		cbCustomer = new JComboBox<>();
		txtRecipientName = new JTextField();
		txtRecipientPhone = new JTextField();
		cbOriginOffice = new JComboBox<>();
		chkHomePickup = new JCheckBox();
		txtPickupAddress = new JTextField();
		txtPickupCity = new JTextField();
		cbDestinationLocation = new JComboBox<>();
		txtDeliveryAddress = new JTextField();
		txtDeliveryCity = new JTextField();
		cbZone = new JComboBox<>(new String[] { "LOCAL", "REGIONAL", "NATIONAL" });
		cbServiceLevel = new JComboBox<>(new String[] { "STANDARD", "EXPRESS" });

		formPanel.add(new JLabel("Customer (sender):"));
		formPanel.add(cbCustomer);
		formPanel.add(new JLabel("Recipient name:"));
		formPanel.add(txtRecipientName);
		formPanel.add(new JLabel("Recipient phone:"));
		formPanel.add(txtRecipientPhone);
		formPanel.add(new JLabel("Origin office:"));
		formPanel.add(cbOriginOffice);
		formPanel.add(new JLabel("Home pickup:"));
		formPanel.add(chkHomePickup);
		formPanel.add(new JLabel("Pickup address:"));
		formPanel.add(txtPickupAddress);
		formPanel.add(new JLabel("Pickup city:"));
		formPanel.add(txtPickupCity);
		formPanel.add(new JLabel("Destination office/warehouse:"));
		formPanel.add(cbDestinationLocation);
		formPanel.add(new JLabel("Delivery address:"));
		formPanel.add(txtDeliveryAddress);
		formPanel.add(new JLabel("Delivery city:"));
		formPanel.add(txtDeliveryCity);
		formPanel.add(new JLabel("Destination zone:"));
		formPanel.add(cbZone);
		formPanel.add(new JLabel("Service level:"));
		formPanel.add(cbServiceLevel);

		frame.getContentPane().add(formPanel, BorderLayout.NORTH);

		JPanel packagesPanel = new JPanel(new BorderLayout(5, 5));
		packagesPanel.setBorder(BorderFactory.createTitledBorder("Packages"));

		JPanel addPanel = new JPanel(new GridLayout(1, 0, 8, 0));
		txtPackageWeight = new JTextField();
		txtPackageDescription = new JTextField();
		btnAddPackage = new JButton("Add Package");
		btnRemovePackage = new JButton("Remove Selected");
		addPanel.add(new JLabel("Weight (kg):"));
		addPanel.add(txtPackageWeight);
		addPanel.add(new JLabel("Description:"));
		addPanel.add(txtPackageDescription);
		addPanel.add(btnAddPackage);
		addPanel.add(btnRemovePackage);
		packagesPanel.add(addPanel, BorderLayout.NORTH);

		packagesModel = new DefaultTableModel(new String[] { "Weight (kg)", "Description" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tablePackages = new JTable(packagesModel);
		packagesPanel.add(new JScrollPane(tablePackages), BorderLayout.CENTER);

		frame.getContentPane().add(packagesPanel, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
		lblTotalPrice = new JLabel("Total price: -");
		lblTotalPrice.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnRegister = new JButton("Register Shipment");
		bottomPanel.add(lblTotalPrice, BorderLayout.WEST);
		bottomPanel.add(btnRegister, BorderLayout.EAST);
		frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
	}

	public JFrame getFrame() {
		return frame;
	}

	public JComboBox<String> getCbCustomer() {
		return cbCustomer;
	}

	public JTextField getTxtRecipientName() {
		return txtRecipientName;
	}

	public JTextField getTxtRecipientPhone() {
		return txtRecipientPhone;
	}

	public JComboBox<String> getCbOriginOffice() {
		return cbOriginOffice;
	}

	public JCheckBox getChkHomePickup() {
		return chkHomePickup;
	}

	public JTextField getTxtPickupAddress() {
		return txtPickupAddress;
	}

	public JTextField getTxtPickupCity() {
		return txtPickupCity;
	}

	public JComboBox<String> getCbDestinationLocation() {
		return cbDestinationLocation;
	}

	public JTextField getTxtDeliveryAddress() {
		return txtDeliveryAddress;
	}

	public JTextField getTxtDeliveryCity() {
		return txtDeliveryCity;
	}

	public JComboBox<String> getCbZone() {
		return cbZone;
	}

	public JComboBox<String> getCbServiceLevel() {
		return cbServiceLevel;
	}

	public JTextField getTxtPackageWeight() {
		return txtPackageWeight;
	}

	public JTextField getTxtPackageDescription() {
		return txtPackageDescription;
	}

	public JButton getBtnAddPackage() {
		return btnAddPackage;
	}

	public JButton getBtnRemovePackage() {
		return btnRemovePackage;
	}

	public JTable getTablePackages() {
		return tablePackages;
	}

	public DefaultTableModel getPackagesModel() {
		return packagesModel;
	}

	public JLabel getLblTotalPrice() {
		return lblTotalPrice;
	}

	public JButton getBtnRegister() {
		return btnRegister;
	}
}

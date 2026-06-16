package uo289023.si26.registershipment;

import javax.swing.JOptionPane;

import uo289023.si26.utils.ApplicationException;
import uo289023.si26.utils.SwingUtil;

public class RegisterShipmentController {

	private RegisterShipmentModel model;
	private RegisterShipmentView view;
	private String simulatedDate;

	public RegisterShipmentController(RegisterShipmentModel model, RegisterShipmentView view) {
		this.model = model;
		this.view = view;
	}

	public void setSimulatedDate(String simulatedDate) {
		this.simulatedDate = simulatedDate;
	}

	public void initController() {
		loadCombos();
		view.getChkHomePickup().addActionListener(e -> SwingUtil.exceptionWrapper(this::updatePickupFields));
		view.getBtnAddPackage().addActionListener(e -> SwingUtil.exceptionWrapper(this::addPackage));
		view.getBtnRemovePackage().addActionListener(e -> SwingUtil.exceptionWrapper(this::removePackage));
		view.getCbZone().addActionListener(e -> SwingUtil.exceptionWrapper(this::updateTotalPrice));
		view.getCbServiceLevel().addActionListener(e -> SwingUtil.exceptionWrapper(this::updateTotalPrice));
		view.getBtnRegister().addActionListener(e -> SwingUtil.exceptionWrapper(this::registerShipment));
		updatePickupFields();
		view.getFrame().setVisible(true);
	}

	private void loadCombos() {
		view.getCbCustomer().removeAllItems();
		for (Object[] row : model.getCustomers())
			view.getCbCustomer().addItem(row[0] + " - " + row[1]);
		view.getCbOriginOffice().removeAllItems();
		for (Object[] row : model.getOffices())
			view.getCbOriginOffice().addItem(row[0] + " - " + row[1]);
		view.getCbDestinationLocation().removeAllItems();
		for (Object[] row : model.getDestinationLocations())
			view.getCbDestinationLocation().addItem((String) row[0]);
	}

	private void updatePickupFields() {
		boolean homePickup = view.getChkHomePickup().isSelected();
		view.getTxtPickupAddress().setEnabled(homePickup);
		view.getTxtPickupCity().setEnabled(homePickup);
	}

	private void addPackage() {
		double weight = parseWeight(view.getTxtPackageWeight().getText());
		String description = view.getTxtPackageDescription().getText().trim();
		view.getPackagesModel().addRow(new Object[] { weight, description });
		view.getTxtPackageWeight().setText("");
		view.getTxtPackageDescription().setText("");
		updateTotalPrice();
	}

	private void removePackage() {
		int row = view.getTablePackages().getSelectedRow();
		if (row < 0)
			throw new ApplicationException("Select a package to remove");
		view.getPackagesModel().removeRow(row);
		updateTotalPrice();
	}

	private double parseWeight(String text) {
		double weight;
		try {
			weight = Double.parseDouble(text.trim());
		} catch (NumberFormatException e) {
			throw new ApplicationException("Package weight must be a number");
		}
		if (weight <= 0)
			throw new ApplicationException("Package weight must be greater than zero");
		return weight;
	}

	private void updateTotalPrice() {
		try {
			if (view.getPackagesModel().getRowCount() == 0) {
				view.getLblTotalPrice().setText("Total price: -");
				return;
			}
			view.getLblTotalPrice().setText(String.format("Total price: %.2f EUR", calculateTotalPrice()));
		} catch (ApplicationException e) {
			view.getLblTotalPrice().setText("Total price: N/A");
		}
	}

	private double calculateTotalPrice() {
		String serviceLevel = (String) view.getCbServiceLevel().getSelectedItem();
		String zone = (String) view.getCbZone().getSelectedItem();
		double total = 0;
		for (int i = 0; i < view.getPackagesModel().getRowCount(); i++) {
			double weight = ((Number) view.getPackagesModel().getValueAt(i, 0)).doubleValue();
			total += model.getRate(serviceLevel, zone, weight);
		}
		return total;
	}

	private void registerShipment() {
		if (view.getCbCustomer().getSelectedItem() == null)
			throw new ApplicationException("Select a customer");
		String recipientName = view.getTxtRecipientName().getText().trim();
		String recipientPhone = view.getTxtRecipientPhone().getText().trim();
		if (recipientName.isEmpty() || recipientPhone.isEmpty())
			throw new ApplicationException("Recipient name and phone are required");
		String deliveryAddress = view.getTxtDeliveryAddress().getText().trim();
		String deliveryCity = view.getTxtDeliveryCity().getText().trim();
		if (deliveryAddress.isEmpty() || deliveryCity.isEmpty())
			throw new ApplicationException("Delivery address and city are required");
		if (view.getPackagesModel().getRowCount() == 0)
			throw new ApplicationException("Add at least one package");

		int customerId = Integer.parseInt(((String) view.getCbCustomer().getSelectedItem()).split(" - ")[0]);
		int originOfficeId = Integer.parseInt(((String) view.getCbOriginOffice().getSelectedItem()).split(" - ")[0]);
		String destinationLocation = (String) view.getCbDestinationLocation().getSelectedItem();
		String zone = (String) view.getCbZone().getSelectedItem();
		String serviceLevel = (String) view.getCbServiceLevel().getSelectedItem();

		boolean homePickup = view.getChkHomePickup().isSelected();
		String pickupAddress = "";
		String pickupCity = "";
		if (homePickup) {
			pickupAddress = view.getTxtPickupAddress().getText().trim();
			pickupCity = view.getTxtPickupCity().getText().trim();
			if (pickupAddress.isEmpty() || pickupCity.isEmpty())
				throw new ApplicationException("Pickup address and city are required for home pickup");
		}

		int packageCount = view.getPackagesModel().getRowCount();
		double[] weights = new double[packageCount];
		String[] descriptions = new String[packageCount];
		for (int i = 0; i < packageCount; i++) {
			weights[i] = ((Number) view.getPackagesModel().getValueAt(i, 0)).doubleValue();
			descriptions[i] = (String) view.getPackagesModel().getValueAt(i, 1);
		}

		double totalPrice = calculateTotalPrice();

		int shipmentId = model.registerShipment(customerId, recipientName, recipientPhone, homePickup, pickupAddress,
				pickupCity, originOfficeId, destinationLocation, deliveryAddress, deliveryCity, zone, serviceLevel,
				weights, descriptions, simulatedDate);

		JOptionPane.showMessageDialog(view.getFrame(),
				String.format("Shipment %d registered with %d package(s).%nTotal price: %.2f EUR", shipmentId,
						view.getPackagesModel().getRowCount(), totalPrice),
				"Shipment Registered", JOptionPane.INFORMATION_MESSAGE);
		view.getFrame().dispose();
	}
}

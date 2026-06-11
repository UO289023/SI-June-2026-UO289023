package uo289023.si26.modifydeliveryplace;

import java.util.List;

import javax.swing.JOptionPane;

import uo289023.si26.dtos.ShipmentDTO;
import uo289023.si26.utils.ApplicationException;
import uo289023.si26.utils.SwingUtil;

public class ModifyDeliveryPlaceController {

	private ModifyDeliveryPlaceModel model;
	private ModifyDeliveryPlaceView view;
	private String simulatedDate;

	public ModifyDeliveryPlaceController(ModifyDeliveryPlaceModel model, ModifyDeliveryPlaceView view) {
		this.model = model;
		this.view = view;
	}

	public void setSimulatedDate(String simulatedDate) {
		this.simulatedDate = simulatedDate;
	}

	public void initController() {
		loadCustomers();
		loadLocations();
		view.getCbCustomer().addActionListener(e -> SwingUtil.exceptionWrapper(this::loadShipments));
		view.getRbNewAddress().addActionListener(e -> SwingUtil.exceptionWrapper(this::updateFields));
		view.getRbLocation().addActionListener(e -> SwingUtil.exceptionWrapper(this::updateFields));
		view.getBtnApply().addActionListener(e -> SwingUtil.exceptionWrapper(this::applyChange));
		loadShipments();
		updateFields();
		view.getFrame().setVisible(true);
	}

	private void loadCustomers() {
		view.getCbCustomer().removeAllItems();
		for (Object[] row : model.getCustomers())
			view.getCbCustomer().addItem(row[0] + " - " + row[1]);
	}

	private void loadLocations() {
		view.getCbLocation().removeAllItems();
		for (Object[] row : model.getDestinationLocations())
			view.getCbLocation().addItem((String) row[0]);
	}

	private void loadShipments() {
		if (view.getCbCustomer().getSelectedItem() == null)
			return;
		int customerId = Integer.parseInt(((String) view.getCbCustomer().getSelectedItem()).split(" - ")[0]);
		List<ShipmentDTO> shipments = model.getActiveShipments(customerId);
		view.getTableShipments().setModel(SwingUtil.getTableModelFromPojos(shipments,
				new String[] { "shipmentId", "recipientName", "deliveryAddress", "deliveryCity", "status" }));
		SwingUtil.autoAdjustColumns(view.getTableShipments());
	}

	private void updateFields() {
		boolean newAddress = view.getRbNewAddress().isSelected();
		view.getTxtNewAddress().setEnabled(newAddress);
		view.getTxtNewCity().setEnabled(newAddress);
		view.getCbLocation().setEnabled(!newAddress);
	}

	private void applyChange() {
		int row = view.getTableShipments().getSelectedRow();
		if (row < 0)
			throw new ApplicationException("Select a shipment to modify");
		int shipmentId = ((Number) view.getTableShipments().getModel().getValueAt(row, 0)).intValue();

		String newAddress;
		String newCity;
		String placeDescription;
		if (view.getRbNewAddress().isSelected()) {
			newAddress = view.getTxtNewAddress().getText().trim();
			newCity = view.getTxtNewCity().getText().trim();
			if (newAddress.isEmpty() || newCity.isEmpty())
				throw new ApplicationException("New address and city are required");
			placeDescription = newAddress + ", " + newCity;
		} else {
			if (view.getCbLocation().getSelectedItem() == null)
				throw new ApplicationException("Select an office/warehouse");
			Object[] location = model.getLocation((String) view.getCbLocation().getSelectedItem());
			newAddress = (String) location[1];
			newCity = (String) location[2];
			placeDescription = location[0] + " (" + newAddress + ", " + newCity + ")";
		}

		model.updateDeliveryPlace(shipmentId, newAddress, newCity);
		for (Object[] pkg : model.getShipmentPackageIds(shipmentId)) {
			int packageId = ((Number) pkg[0]).intValue();
			model.insertTrackingEvent(packageId, simulatedDate, newAddress + ", " + newCity,
					"DELIVERY_PLACE_CHANGED", "Delivery place changed to " + placeDescription);
		}

		loadShipments();
		JOptionPane.showMessageDialog(view.getFrame(),
				"Delivery place of shipment " + shipmentId + " changed to:\n" + placeDescription,
				"Delivery Place Modified", JOptionPane.INFORMATION_MESSAGE);
	}
}

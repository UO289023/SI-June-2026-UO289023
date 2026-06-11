package uo289023.si26.registerdelivery;

import java.util.List;

import javax.swing.JOptionPane;

import uo289023.si26.dtos.ShipmentDTO;
import uo289023.si26.utils.ApplicationException;
import uo289023.si26.utils.SwingUtil;

public class RegisterDeliveryController {

	private static final int MAX_ADDITIONAL_ATTEMPTS = 3;

	private RegisterDeliveryModel model;
	private RegisterDeliveryView view;
	private String simulatedDate;

	public RegisterDeliveryController(RegisterDeliveryModel model, RegisterDeliveryView view) {
		this.model = model;
		this.view = view;
	}

	public void setSimulatedDate(String simulatedDate) {
		this.simulatedDate = simulatedDate;
	}

	public void initController() {
		loadShipments();
		view.getBtnRegisterDelivery().addActionListener(e -> SwingUtil.exceptionWrapper(this::registerDelivery));
		view.getFrame().setVisible(true);
	}

	private void loadShipments() {
		List<ShipmentDTO> shipments = model.getPendingDeliveries();
		view.getTableShipments().setModel(SwingUtil.getTableModelFromPojos(shipments, new String[] { "shipmentId",
				"recipientName", "deliveryAddress", "deliveryCity", "serviceLevel", "status", "failedAttempts" }));
		SwingUtil.autoAdjustColumns(view.getTableShipments());
	}

	private void registerDelivery() {
		int row = view.getTableShipments().getSelectedRow();
		if (row < 0)
			throw new ApplicationException("Select a shipment");
		int shipmentId = ((Number) view.getTableShipments().getModel().getValueAt(row, 0)).intValue();
		ShipmentDTO shipment = model.getShipment(shipmentId);
		String result = (String) view.getCbResult().getSelectedItem();
		String notes = view.getTxtNotes().getText().trim();
		String deliveryPlace = shipment.getDeliveryAddress() + ", " + shipment.getDeliveryCity();
		int attemptNumber = shipment.getFailedAttempts() + 1;

		if ("DELIVERED".equals(result)) {
			model.insertDeliveryAttempt(shipmentId, attemptNumber, simulatedDate, "DELIVERED",
					notes.isEmpty() ? "Delivered to recipient" : notes);
			model.updateShipmentStatus(shipmentId, "DELIVERED", shipment.getFailedAttempts());
			model.completeDeliveryLeg(shipmentId);
			model.updatePackagesStatus(shipmentId, "DELIVERED");
			for (Object[] pkg : model.getShipmentPackageIds(shipmentId))
				model.insertTrackingEvent(((Number) pkg[0]).intValue(), simulatedDate, deliveryPlace, "DELIVERED",
						"Delivered to recipient");
			JOptionPane.showMessageDialog(view.getFrame(),
					"Shipment " + shipmentId + " delivered.\nFinal status recorded: DELIVERED",
					"Delivery Registered", JOptionPane.INFORMATION_MESSAGE);
		} else {
			int failedAttempts = shipment.getFailedAttempts() + 1;
			model.insertDeliveryAttempt(shipmentId, attemptNumber, simulatedDate, "ABSENT",
					notes.isEmpty() ? "Recipient absent" : notes);
			if (failedAttempts > MAX_ADDITIONAL_ATTEMPTS) {
				model.updateShipmentStatus(shipmentId, "AT_OFFICE_FOR_PICKUP", failedAttempts);
				model.updatePackagesStatus(shipmentId, "AT_OFFICE_FOR_PICKUP");
				for (Object[] pkg : model.getShipmentPackageIds(shipmentId))
					model.insertTrackingEvent(((Number) pkg[0]).intValue(), simulatedDate,
							shipment.getDestinationLocation(), "AT_OFFICE_FOR_PICKUP",
							"All delivery attempts failed, package remains at " + shipment.getDestinationLocation()
									+ ", customer advised");
				JOptionPane.showMessageDialog(view.getFrame(),
						"Delivery failed (" + failedAttempts + " attempts).\nThe package remains at "
								+ shipment.getDestinationLocation() + " and the customer has been advised.",
						"Delivery Failed", JOptionPane.WARNING_MESSAGE);
			} else {
				model.updateShipmentStatus(shipmentId, "FAILED_DELIVERY", failedAttempts);
				model.updatePackagesStatus(shipmentId, "FAILED_DELIVERY");
				for (Object[] pkg : model.getShipmentPackageIds(shipmentId))
					model.insertTrackingEvent(((Number) pkg[0]).intValue(), simulatedDate, deliveryPlace,
							"FAILED_DELIVERY", "Recipient absent, new delivery attempt scheduled");
				JOptionPane.showMessageDialog(view.getFrame(),
						"Recipient absent (attempt " + attemptNumber + ").\nA new delivery attempt has been scheduled.",
						"Delivery Failed", JOptionPane.WARNING_MESSAGE);
			}
		}
		view.getTxtNotes().setText("");
		loadShipments();
	}
}

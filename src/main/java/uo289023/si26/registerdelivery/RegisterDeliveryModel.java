package uo289023.si26.registerdelivery;

import java.util.List;

import uo289023.si26.dtos.ShipmentDTO;
import uo289023.si26.utils.ApplicationException;
import uo289023.si26.utils.Database;

public class RegisterDeliveryModel {

	private static final int MAX_ADDITIONAL_ATTEMPTS = 3;

	protected Database db = new Database();

	public String registerDelivery(int shipmentId, String result, String date, String notes) {
		ShipmentDTO shipment = getShipment(shipmentId);
		if (date.compareTo(shipment.getRegistrationDate()) < 0)
			throw new ApplicationException("Delivery date " + date
					+ " cannot be earlier than the shipment registration date " + shipment.getRegistrationDate());
		String deliveryPlace = shipment.getDeliveryAddress() + ", " + shipment.getDeliveryCity();
		int attemptNumber = shipment.getFailedAttempts() + 1;

		if ("DELIVERED".equals(result)) {
			insertDeliveryAttempt(shipmentId, attemptNumber, date, "DELIVERED",
					notes == null || notes.isEmpty() ? "Delivered to recipient" : notes);
			updateShipmentStatus(shipmentId, "DELIVERED", shipment.getFailedAttempts());
			completeDeliveryLeg(shipmentId);
			updatePackagesStatus(shipmentId, "DELIVERED");
			for (Object[] pkg : getShipmentPackageIds(shipmentId))
				insertTrackingEvent(((Number) pkg[0]).intValue(), date, deliveryPlace, "DELIVERED",
						"Delivered to recipient");
			return "DELIVERED";
		}

		int failedAttempts = shipment.getFailedAttempts() + 1;
		insertDeliveryAttempt(shipmentId, attemptNumber, date, "ABSENT",
				notes == null || notes.isEmpty() ? "Recipient absent" : notes);
		if (failedAttempts > MAX_ADDITIONAL_ATTEMPTS) {
			updateShipmentStatus(shipmentId, "AT_OFFICE_FOR_PICKUP", failedAttempts);
			updatePackagesStatus(shipmentId, "AT_OFFICE_FOR_PICKUP");
			for (Object[] pkg : getShipmentPackageIds(shipmentId))
				insertTrackingEvent(((Number) pkg[0]).intValue(), date, shipment.getDestinationLocation(),
						"AT_OFFICE_FOR_PICKUP", "All delivery attempts failed, package remains at "
								+ shipment.getDestinationLocation() + ", customer advised");
			return "AT_OFFICE_FOR_PICKUP";
		}
		updateShipmentStatus(shipmentId, "FAILED_DELIVERY", failedAttempts);
		updatePackagesStatus(shipmentId, "FAILED_DELIVERY");
		for (Object[] pkg : getShipmentPackageIds(shipmentId))
			insertTrackingEvent(((Number) pkg[0]).intValue(), date, deliveryPlace, "FAILED_DELIVERY",
					"Recipient absent, new delivery attempt scheduled");
		return "FAILED_DELIVERY";
	}

	public List<ShipmentDTO> getPendingDeliveries() {
		return db.executeQueryPojo(ShipmentDTO.class,
				"select shipment_id as shipmentId, recipient_name as recipientName, delivery_address as deliveryAddress, delivery_city as deliveryCity, destination_location as destinationLocation, service_level as serviceLevel, status, failed_attempts as failedAttempts "
						+ "from Shipment where status not in ('DELIVERED','AT_OFFICE_FOR_PICKUP') order by shipment_id");
	}

	public ShipmentDTO getShipment(int shipmentId) {
		List<ShipmentDTO> result = db.executeQueryPojo(ShipmentDTO.class,
				"select shipment_id as shipmentId, recipient_name as recipientName, delivery_address as deliveryAddress, delivery_city as deliveryCity, destination_location as destinationLocation, registration_date as registrationDate, status, failed_attempts as failedAttempts "
						+ "from Shipment where shipment_id=?",
				shipmentId);
		if (result.isEmpty())
			throw new ApplicationException("Shipment not found: " + shipmentId);
		return result.get(0);
	}

	public void updateShipmentStatus(int shipmentId, String status, int failedAttempts) {
		db.executeUpdate("update Shipment set status=?, failed_attempts=? where shipment_id=?", status,
				failedAttempts, shipmentId);
	}

	public void completeDeliveryLeg(int shipmentId) {
		db.executeUpdate("update ShipmentLeg set status='COMPLETED' where shipment_id=? and leg_type='DELIVERY'",
				shipmentId);
	}

	public void updatePackagesStatus(int shipmentId, String status) {
		db.executeUpdate("update Package set status=? where shipment_id=?", status, shipmentId);
	}

	public List<Object[]> getShipmentPackageIds(int shipmentId) {
		return db.executeQueryArray("select package_id from Package where shipment_id=?", shipmentId);
	}

	public void insertDeliveryAttempt(int shipmentId, int attemptNumber, String attemptDate, String result,
			String notes) {
		db.executeUpdate(
				"insert into DeliveryAttempt (shipment_id, attempt_number, attempt_date, result, notes) values (?,?,?,?,?)",
				shipmentId, attemptNumber, attemptDate, result, notes);
	}

	public void insertTrackingEvent(int packageId, String eventDate, String location, String status,
			String description) {
		db.executeUpdate(
				"insert into TrackingEvent (package_id, event_date, location, status, description) values (?,?,?,?,?)",
				packageId, eventDate, location, status, description);
	}
}

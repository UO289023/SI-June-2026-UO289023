package uo289023.si26.registerdelivery;

import java.util.List;

import uo289023.si26.dtos.ShipmentDTO;
import uo289023.si26.utils.ApplicationException;
import uo289023.si26.utils.Database;

public class RegisterDeliveryModel {
	protected Database db = new Database();

	public List<ShipmentDTO> getPendingDeliveries() {
		return db.executeQueryPojo(ShipmentDTO.class,
				"select shipment_id as shipmentId, recipient_name as recipientName, delivery_address as deliveryAddress, delivery_city as deliveryCity, destination_location as destinationLocation, service_level as serviceLevel, status, failed_attempts as failedAttempts "
						+ "from Shipment where status not in ('DELIVERED','AT_OFFICE_FOR_PICKUP') order by shipment_id");
	}

	public ShipmentDTO getShipment(int shipmentId) {
		List<ShipmentDTO> result = db.executeQueryPojo(ShipmentDTO.class,
				"select shipment_id as shipmentId, recipient_name as recipientName, delivery_address as deliveryAddress, delivery_city as deliveryCity, destination_location as destinationLocation, status, failed_attempts as failedAttempts "
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

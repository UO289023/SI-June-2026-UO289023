package uo289023.si26.modifydeliveryplace;

import java.util.List;

import uo289023.si26.dtos.ShipmentDTO;
import uo289023.si26.utils.ApplicationException;
import uo289023.si26.utils.Database;

public class ModifyDeliveryPlaceModel {
	protected Database db = new Database();

	public List<Object[]> getCustomers() {
		return db.executeQueryArray("select customer_id, name || ' ' || surname from Customer order by surname, name");
	}

	public List<ShipmentDTO> getActiveShipments(int customerId) {
		return db.executeQueryPojo(ShipmentDTO.class,
				"select shipment_id as shipmentId, recipient_name as recipientName, delivery_address as deliveryAddress, delivery_city as deliveryCity, status "
						+ "from Shipment where customer_id=? and status not in ('DELIVERED','AT_OFFICE_FOR_PICKUP') order by shipment_id",
				customerId);
	}

	public List<Object[]> getDestinationLocations() {
		return db.executeQueryArray(
				"select name, address, city from Office union select name, address, city from Warehouse order by name");
	}

	public Object[] getLocation(String name) {
		List<Object[]> result = db.executeQueryArray(
				"select name, address, city from Office where name=? union select name, address, city from Warehouse where name=?",
				name, name);
		if (result.isEmpty())
			throw new ApplicationException("Location not found: " + name);
		return result.get(0);
	}

	public void updateDeliveryPlace(int shipmentId, String deliveryAddress, String deliveryCity) {
		db.executeUpdate("update Shipment set delivery_address=?, delivery_city=? where shipment_id=?",
				deliveryAddress, deliveryCity, shipmentId);
		db.executeUpdate(
				"update ShipmentLeg set destination=? where shipment_id=? and leg_type='DELIVERY' and status<>'COMPLETED'",
				deliveryAddress + ", " + deliveryCity, shipmentId);
	}

	public List<Object[]> getShipmentPackageIds(int shipmentId) {
		return db.executeQueryArray("select package_id from Package where shipment_id=?", shipmentId);
	}

	public void insertTrackingEvent(int packageId, String eventDate, String location, String status,
			String description) {
		db.executeUpdate(
				"insert into TrackingEvent (package_id, event_date, location, status, description) values (?,?,?,?,?)",
				packageId, eventDate, location, status, description);
	}
}

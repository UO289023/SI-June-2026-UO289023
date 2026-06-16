package uo289023.si26.registershipment;

import java.util.List;

import uo289023.si26.utils.ApplicationException;
import uo289023.si26.utils.Database;

public class RegisterShipmentModel {
	protected Database db = new Database();

	public List<Object[]> getCustomers() {
		return db.executeQueryArray("select customer_id, name || ' ' || surname from Customer order by surname, name");
	}

	public List<Object[]> getOffices() {
		return db.executeQueryArray("select office_id, name from Office order by name");
	}

	public List<Object[]> getDestinationLocations() {
		return db.executeQueryArray("select name from Office union select name from Warehouse order by name");
	}

	public Object[] getOffice(int officeId) {
		List<Object[]> result = db.executeQueryArray("select name, address, city from Office where office_id=?", officeId);
		if (result.isEmpty())
			throw new ApplicationException("Office not found: " + officeId);
		return result.get(0);
	}

	public double getRate(String serviceLevel, String zone, double weightKg) {
		List<Object[]> result = db.executeQueryArray(
				"select min(price) from Rate where service_level=? and zone=? and max_weight_kg>=?",
				serviceLevel, zone, weightKg);
		if (result.isEmpty() || result.get(0)[0] == null)
			throw new ApplicationException("No rate available for " + serviceLevel + "/" + zone + " and weight " + weightKg + " kg");
		return ((Number) result.get(0)[0]).doubleValue();
	}

	public Integer getPickupVehicle(String officeName) {
		List<Object[]> result = db.executeQueryArray(
				"select vehicle_id from Route where destination=? and origin like 'Customer Address%'", officeName);
		if (!result.isEmpty())
			return ((Number) result.get(0)[0]).intValue();
		return getDefaultVan();
	}

	public Integer getDeliveryVehicle(String locationName) {
		List<Object[]> result = db.executeQueryArray(
				"select vehicle_id from Route where origin=? and destination like 'Customer Address%'", locationName);
		if (!result.isEmpty())
			return ((Number) result.get(0)[0]).intValue();
		return getDefaultVan();
	}

	private Integer getDefaultVan() {
		List<Object[]> result = db.executeQueryArray("select min(vehicle_id) from Vehicle where type='VAN'");
		if (result.isEmpty() || result.get(0)[0] == null)
			return null;
		return ((Number) result.get(0)[0]).intValue();
	}

	public double calculateTotalPrice(String serviceLevel, String zone, double[] weights) {
		double total = 0;
		for (double weight : weights)
			total += getRate(serviceLevel, zone, weight);
		return total;
	}

	public int registerShipment(int customerId, String recipientName, String recipientPhone, boolean homePickup,
			String pickupAddress, String pickupCity, int originOfficeId, String destinationLocation,
			String deliveryAddress, String deliveryCity, String zone, String serviceLevel, double[] weights,
			String[] descriptions, String registrationDate) {
		Object[] originOffice = getOffice(originOfficeId);
		String originOfficeName = (String) originOffice[0];
		String originAddress = homePickup ? pickupAddress : (String) originOffice[1];
		String originCity = homePickup ? pickupCity : (String) originOffice[2];

		double totalPrice = calculateTotalPrice(serviceLevel, zone, weights);

		int shipmentId = insertShipment(customerId, recipientName, recipientPhone, originAddress, originCity,
				originOfficeId, destinationLocation, deliveryAddress, deliveryCity, homePickup ? 1 : 0, zone,
				serviceLevel, totalPrice, registrationDate);

		int legOrder = 1;
		if (homePickup) {
			insertLeg(shipmentId, legOrder, "PICKUP", originAddress + ", " + originCity, originOfficeName,
					getPickupVehicle(originOfficeName));
			legOrder++;
		}
		insertLeg(shipmentId, legOrder, "DELIVERY", destinationLocation, deliveryAddress + ", " + deliveryCity,
				getDeliveryVehicle(destinationLocation));

		String registrationLocation = homePickup ? originAddress + ", " + originCity : originOfficeName;
		String registrationDescription = homePickup ? "Shipment registered by telephone, home pickup scheduled"
				: "Shipment registered by telephone at office";
		for (int i = 0; i < weights.length; i++) {
			String barcode = "PKG-" + shipmentId + "-" + (i + 1);
			int packageId = insertPackage(shipmentId, barcode, weights[i], descriptions[i]);
			insertTrackingEvent(packageId, registrationDate, registrationLocation, "REGISTERED",
					registrationDescription);
		}
		return shipmentId;
	}

	public int insertShipment(int customerId, String recipientName, String recipientPhone, String originAddress,
			String originCity, int originOfficeId, String destinationLocation, String deliveryAddress,
			String deliveryCity, int homePickup, String zone, String serviceLevel, double price,
			String registrationDate) {
		Object id = db.executeInsert(
				"insert into Shipment (customer_id, recipient_name, recipient_phone, origin_address, origin_city, origin_office_id, destination_location, delivery_address, delivery_city, home_pickup, zone, service_level, price, channel, registration_date, status, failed_attempts) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,0)",
				customerId, recipientName, recipientPhone, originAddress, originCity, originOfficeId,
				destinationLocation, deliveryAddress, deliveryCity, homePickup, zone, serviceLevel, price,
				"TELEPHONE", registrationDate, "REGISTERED");
		return ((Number) id).intValue();
	}

	public int insertPackage(int shipmentId, String barcode, double weightKg, String description) {
		Object id = db.executeInsert(
				"insert into Package (shipment_id, barcode, weight_kg, description, status) values (?,?,?,?,?)",
				shipmentId, barcode, weightKg, description, "REGISTERED");
		return ((Number) id).intValue();
	}

	public void insertLeg(int shipmentId, int legOrder, String legType, String origin, String destination,
			Integer vehicleId) {
		db.executeUpdate(
				"insert into ShipmentLeg (shipment_id, leg_order, leg_type, origin, destination, vehicle_id, status) values (?,?,?,?,?,?,?)",
				shipmentId, legOrder, legType, origin, destination, vehicleId, "PENDING");
	}

	public void insertTrackingEvent(int packageId, String eventDate, String location, String status,
			String description) {
		db.executeUpdate(
				"insert into TrackingEvent (package_id, event_date, location, status, description) values (?,?,?,?,?)",
				packageId, eventDate, location, status, description);
	}
}

package uo289023.si26.trackshipment;

import java.util.List;

import uo289023.si26.dtos.TrackedPackageDTO;
import uo289023.si26.dtos.TrackingEventDTO;
import uo289023.si26.utils.Database;

public class TrackShipmentModel {
	protected Database db = new Database();

	public List<Object[]> getCustomers() {
		return db.executeQueryArray("select customer_id, name || ' ' || surname from Customer order by surname, name");
	}

	public List<TrackedPackageDTO> getCustomerPackages(int customerId) {
		return db.executeQueryPojo(TrackedPackageDTO.class,
				"select p.barcode, s.registration_date as registrationDate, s.delivery_address || ', ' || s.delivery_city as deliveryPlace, p.status, "
						+ "(select te.location from TrackingEvent te where te.package_id = p.package_id order by te.event_id desc limit 1) as lastLocation "
						+ "from Package p join Shipment s on p.shipment_id = s.shipment_id where s.customer_id=? order by p.package_id",
				customerId);
	}

	public List<TrackingEventDTO> getPackageEvents(String barcode) {
		return db.executeQueryPojo(TrackingEventDTO.class,
				"select te.event_id as eventId, te.package_id as packageId, te.event_date as eventDate, te.location, te.status, te.description "
						+ "from TrackingEvent te join Package p on te.package_id = p.package_id where p.barcode=? order by te.event_id",
				barcode);
	}
}

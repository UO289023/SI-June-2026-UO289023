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

	public List<TrackedPackageDTO> getCustomerPackages(int customerId, String asOfDate) {
		return db.executeQueryPojo(TrackedPackageDTO.class,
				"select p.barcode, "
						+ "(select te.event_date from TrackingEvent te where te.package_id = p.package_id and te.status='REGISTERED' order by te.event_id limit 1) as registrationDate, "
						+ "s.delivery_address || ', ' || s.delivery_city as deliveryPlace, "
						+ "(select te.status from TrackingEvent te where te.package_id = p.package_id and te.event_date <= ? and te.status <> 'DELIVERY_PLACE_CHANGED' order by te.event_date desc, te.event_id desc limit 1) as status, "
						+ "(select te.location from TrackingEvent te where te.package_id = p.package_id and te.event_date <= ? and te.status <> 'DELIVERY_PLACE_CHANGED' order by te.event_date desc, te.event_id desc limit 1) as lastLocation "
						+ "from Package p join Shipment s on p.shipment_id = s.shipment_id "
						+ "where s.customer_id = ? and exists (select 1 from TrackingEvent te where te.package_id = p.package_id and te.event_date <= ? and te.status <> 'DELIVERY_PLACE_CHANGED') "
						+ "order by p.package_id",
				asOfDate, asOfDate, customerId, asOfDate);
	}

	public List<TrackingEventDTO> getPackageEvents(String barcode, String asOfDate) {
		return db.executeQueryPojo(TrackingEventDTO.class,
				"select te.event_id as eventId, te.package_id as packageId, te.event_date as eventDate, te.location, te.status, te.description "
						+ "from TrackingEvent te join Package p on te.package_id = p.package_id where p.barcode=? and te.event_date <= ? "
						+ "order by te.event_date, te.event_id",
				barcode, asOfDate);
	}
}

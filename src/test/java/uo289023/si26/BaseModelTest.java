package uo289023.si26;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;

import uo289023.si26.utils.Database;

public abstract class BaseModelTest {

	protected Database db = new Database();

	@BeforeEach
	public void setUp() {
		db.createDatabase(false);
		db.executeBatch(fixture());
	}

	protected long count(String table) {
		return ((Number) db.executeQueryArray("select count(*) from " + table).get(0)[0]).longValue();
	}

	protected Object[] firstRow(String sql, Object... params) {
		List<Object[]> rows = db.executeQueryArray(sql, params);
		return rows.isEmpty() ? null : rows.get(0);
	}

	private String[] fixture() {
		return new String[] {
				"insert into Office (office_id, name, city, address) values (1,'Oviedo Central Office','Oviedo','Calle Uria 12')",
				"insert into Office (office_id, name, city, address) values (2,'Madrid Office','Madrid','Calle Alcala 200')",

				"insert into Warehouse (warehouse_id, name, city, address) values (1,'Leon Hub','Leon','Poligono de Leon, Nave 7')",

				"insert into Vehicle (vehicle_id, plate, type, capacity_kg) values (1,'V-1','VAN',800)",
				"insert into Vehicle (vehicle_id, plate, type, capacity_kg) values (2,'V-2','VAN',800)",
				"insert into Vehicle (vehicle_id, plate, type, capacity_kg) values (3,'T-1','TRUCK',12000)",

				"insert into Customer (customer_id, name, surname, phone, email, address, city) values (1,'Ana','Garcia','600111222','ana@mail.com','Calle Campoamor 3','Oviedo')",
				"insert into Customer (customer_id, name, surname, phone, email, address, city) values (2,'Luis','Fernandez','600333444','luis@mail.com','Calle Corrida 18','Gijon')",

				"insert into Rate (service_level, zone, max_weight_kg, price) values ('STANDARD','LOCAL',2,4.50)",
				"insert into Rate (service_level, zone, max_weight_kg, price) values ('STANDARD','LOCAL',5,5.50)",
				"insert into Rate (service_level, zone, max_weight_kg, price) values ('STANDARD','LOCAL',10,7.00)",
				"insert into Rate (service_level, zone, max_weight_kg, price) values ('STANDARD','LOCAL',20,9.50)",
				"insert into Rate (service_level, zone, max_weight_kg, price) values ('STANDARD','REGIONAL',2,6.00)",
				"insert into Rate (service_level, zone, max_weight_kg, price) values ('STANDARD','REGIONAL',5,7.50)",
				"insert into Rate (service_level, zone, max_weight_kg, price) values ('STANDARD','REGIONAL',10,9.50)",
				"insert into Rate (service_level, zone, max_weight_kg, price) values ('STANDARD','REGIONAL',20,13.00)",
				"insert into Rate (service_level, zone, max_weight_kg, price) values ('STANDARD','NATIONAL',2,8.00)",
				"insert into Rate (service_level, zone, max_weight_kg, price) values ('STANDARD','NATIONAL',5,10.00)",
				"insert into Rate (service_level, zone, max_weight_kg, price) values ('STANDARD','NATIONAL',10,13.00)",
				"insert into Rate (service_level, zone, max_weight_kg, price) values ('STANDARD','NATIONAL',20,17.50)",
				"insert into Rate (service_level, zone, max_weight_kg, price) values ('EXPRESS','LOCAL',2,6.50)",
				"insert into Rate (service_level, zone, max_weight_kg, price) values ('EXPRESS','LOCAL',5,7.50)",
				"insert into Rate (service_level, zone, max_weight_kg, price) values ('EXPRESS','LOCAL',10,9.50)",
				"insert into Rate (service_level, zone, max_weight_kg, price) values ('EXPRESS','LOCAL',20,12.50)",
				"insert into Rate (service_level, zone, max_weight_kg, price) values ('EXPRESS','REGIONAL',2,8.50)",
				"insert into Rate (service_level, zone, max_weight_kg, price) values ('EXPRESS','REGIONAL',5,10.00)",
				"insert into Rate (service_level, zone, max_weight_kg, price) values ('EXPRESS','REGIONAL',10,12.50)",
				"insert into Rate (service_level, zone, max_weight_kg, price) values ('EXPRESS','REGIONAL',20,16.50)",
				"insert into Rate (service_level, zone, max_weight_kg, price) values ('EXPRESS','NATIONAL',2,11.00)",
				"insert into Rate (service_level, zone, max_weight_kg, price) values ('EXPRESS','NATIONAL',5,13.50)",
				"insert into Rate (service_level, zone, max_weight_kg, price) values ('EXPRESS','NATIONAL',10,17.00)",
				"insert into Rate (service_level, zone, max_weight_kg, price) values ('EXPRESS','NATIONAL',20,22.50)",

				"insert into Route (origin, destination, vehicle_id) values ('Customer Address Oviedo','Oviedo Central Office',2)",
				"insert into Route (origin, destination, vehicle_id) values ('Madrid Office','Customer Address Madrid',2)",

				"insert into Shipment (shipment_id, customer_id, recipient_name, recipient_phone, origin_address, origin_city, origin_office_id, destination_location, delivery_address, delivery_city, home_pickup, zone, service_level, price, channel, registration_date, status, failed_attempts) values (1,1,'Marta Lopez','600555666','Calle Campoamor 3','Oviedo',1,'Madrid Office','Gran Via 60','Madrid',1,'NATIONAL','STANDARD',18.00,'TELEPHONE','2026-06-08','IN_WAREHOUSE',0)",
				"insert into Shipment (shipment_id, customer_id, recipient_name, recipient_phone, origin_address, origin_city, origin_office_id, destination_location, delivery_address, delivery_city, home_pickup, zone, service_level, price, channel, registration_date, status, failed_attempts) values (2,1,'Pedro Suarez','600777888','Calle Corrida 18','Gijon',1,'Oviedo Central Office','Calle Rosal 9','Oviedo',0,'REGIONAL','EXPRESS',8.50,'TELEPHONE','2026-06-05','DELIVERED',0)",
				"insert into Shipment (shipment_id, customer_id, recipient_name, recipient_phone, origin_address, origin_city, origin_office_id, destination_location, delivery_address, delivery_city, home_pickup, zone, service_level, price, channel, registration_date, status, failed_attempts) values (3,2,'Carlos Ruiz','600999000','Calle Corrida 18','Gijon',1,'Oviedo Central Office','Calle Rosal 9','Oviedo',0,'REGIONAL','STANDARD',7.50,'TELEPHONE','2026-06-09','OUT_FOR_DELIVERY',0)",
				"insert into Shipment (shipment_id, customer_id, recipient_name, recipient_phone, origin_address, origin_city, origin_office_id, destination_location, delivery_address, delivery_city, home_pickup, zone, service_level, price, channel, registration_date, status, failed_attempts) values (4,2,'Carlos Ruiz','600999000','Calle Corrida 18','Gijon',1,'Oviedo Central Office','Calle Rosal 9','Oviedo',0,'REGIONAL','STANDARD',7.50,'TELEPHONE','2026-06-09','OUT_FOR_DELIVERY',2)",
				"insert into Shipment (shipment_id, customer_id, recipient_name, recipient_phone, origin_address, origin_city, origin_office_id, destination_location, delivery_address, delivery_city, home_pickup, zone, service_level, price, channel, registration_date, status, failed_attempts) values (5,2,'Carlos Ruiz','600999000','Calle Corrida 18','Gijon',1,'Oviedo Central Office','Calle Rosal 9','Oviedo',0,'REGIONAL','STANDARD',7.50,'TELEPHONE','2026-06-09','OUT_FOR_DELIVERY',3)",

				"insert into Package (package_id, shipment_id, barcode, weight_kg, description, status) values (1,1,'PKG-1-1',2.5,'Books','IN_WAREHOUSE')",
				"insert into Package (package_id, shipment_id, barcode, weight_kg, description, status) values (2,1,'PKG-1-2',1.7,'Parcel','IN_WAREHOUSE')",
				"insert into Package (package_id, shipment_id, barcode, weight_kg, description, status) values (3,2,'PKG-2-1',8.0,'Electronics','DELIVERED')",
				"insert into Package (package_id, shipment_id, barcode, weight_kg, description, status) values (4,3,'PKG-3-1',1.5,'Docs','OUT_FOR_DELIVERY')",
				"insert into Package (package_id, shipment_id, barcode, weight_kg, description, status) values (5,4,'PKG-4-1',3.0,'Box','OUT_FOR_DELIVERY')",
				"insert into Package (package_id, shipment_id, barcode, weight_kg, description, status) values (6,5,'PKG-5-1',3.0,'Box','OUT_FOR_DELIVERY')",

				"insert into ShipmentLeg (shipment_id, leg_order, leg_type, origin, destination, vehicle_id, status) values (1,1,'DELIVERY','Madrid Office','Gran Via 60, Madrid',2,'PENDING')",
				"insert into ShipmentLeg (shipment_id, leg_order, leg_type, origin, destination, vehicle_id, status) values (3,1,'DELIVERY','Oviedo Central Office','Calle Rosal 9, Oviedo',2,'IN_PROGRESS')",
				"insert into ShipmentLeg (shipment_id, leg_order, leg_type, origin, destination, vehicle_id, status) values (4,1,'DELIVERY','Oviedo Central Office','Calle Rosal 9, Oviedo',2,'IN_PROGRESS')",
				"insert into ShipmentLeg (shipment_id, leg_order, leg_type, origin, destination, vehicle_id, status) values (5,1,'DELIVERY','Oviedo Central Office','Calle Rosal 9, Oviedo',2,'IN_PROGRESS')",

				"insert into TrackingEvent (package_id, event_date, location, status, description) values (1,'2026-06-08','Calle Campoamor 3, Oviedo','REGISTERED','Registered')",
				"insert into TrackingEvent (package_id, event_date, location, status, description) values (1,'2026-06-08','Oviedo Central Office','IN_TRANSIT','Departed office')",
				"insert into TrackingEvent (package_id, event_date, location, status, description) values (1,'2026-06-09','Leon Hub','IN_WAREHOUSE','Arrived at warehouse')",
				"insert into TrackingEvent (package_id, event_date, location, status, description) values (2,'2026-06-08','Calle Campoamor 3, Oviedo','REGISTERED','Registered')",
				"insert into TrackingEvent (package_id, event_date, location, status, description) values (2,'2026-06-09','Leon Hub','IN_WAREHOUSE','Arrived at warehouse')",
				"insert into TrackingEvent (package_id, event_date, location, status, description) values (3,'2026-06-05','Calle Corrida 18, Gijon','REGISTERED','Registered')",
				"insert into TrackingEvent (package_id, event_date, location, status, description) values (3,'2026-06-06','Calle Rosal 9, Oviedo','DELIVERED','Delivered to recipient')",
				"insert into TrackingEvent (package_id, event_date, location, status, description) values (4,'2026-06-09','Calle Corrida 18, Gijon','REGISTERED','Registered')",
				"insert into TrackingEvent (package_id, event_date, location, status, description) values (4,'2026-06-10','Oviedo Central Office','OUT_FOR_DELIVERY','Out for delivery')",
				"insert into TrackingEvent (package_id, event_date, location, status, description) values (5,'2026-06-09','Calle Corrida 18, Gijon','REGISTERED','Registered')",
				"insert into TrackingEvent (package_id, event_date, location, status, description) values (6,'2026-06-09','Calle Corrida 18, Gijon','REGISTERED','Registered')" };
	}
}

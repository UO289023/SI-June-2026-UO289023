delete from DeliveryAttempt;
delete from WarehouseVerification;
delete from TrackingEvent;
delete from ShipmentLeg;
delete from Package;
delete from Shipment;
delete from Route;
delete from Rate;
delete from Customer;
delete from Vehicle;
delete from Warehouse;
delete from Office;

insert into Office (office_id, name, city, address) values
	(1, 'Oviedo Central Office', 'Oviedo', 'Calle Uria 12'),
	(2, 'Gijon Office', 'Gijon', 'Avenida de la Costa 45'),
	(3, 'Madrid Office', 'Madrid', 'Calle Alcala 200');

insert into Warehouse (warehouse_id, name, city, address) values
	(1, 'Leon Hub', 'Leon', 'Poligono Industrial de Leon, Nave 7'),
	(2, 'Madrid Hub', 'Madrid', 'Poligono de Vallecas, Nave 21');

insert into Vehicle (vehicle_id, plate, type, capacity_kg) values
	(1, '1234-BBC', 'VAN', 800),
	(2, '5678-CCD', 'VAN', 800),
	(3, '9012-DDF', 'TRUCK', 12000),
	(4, '3456-FFG', 'TRUCK', 12000);

insert into Customer (customer_id, name, surname, phone, email, address, city) values
	(1, 'Ana', 'Garcia', '600111222', 'ana.garcia@mail.com', 'Calle Campoamor 3', 'Oviedo'),
	(2, 'Luis', 'Fernandez', '600333444', 'luis.fernandez@mail.com', 'Calle Corrida 18', 'Gijon'),
	(3, 'Marta', 'Lopez', '600555666', 'marta.lopez@mail.com', 'Gran Via 60', 'Madrid'),
	(4, 'Pedro', 'Suarez', '600777888', 'pedro.suarez@mail.com', 'Calle Rosal 9', 'Oviedo');

insert into Rate (service_level, zone, max_weight_kg, price) values
	('STANDARD', 'LOCAL', 2, 4.50),
	('STANDARD', 'LOCAL', 5, 5.50),
	('STANDARD', 'LOCAL', 10, 7.00),
	('STANDARD', 'LOCAL', 20, 9.50),
	('STANDARD', 'REGIONAL', 2, 6.00),
	('STANDARD', 'REGIONAL', 5, 7.50),
	('STANDARD', 'REGIONAL', 10, 9.50),
	('STANDARD', 'REGIONAL', 20, 13.00),
	('STANDARD', 'NATIONAL', 2, 8.00),
	('STANDARD', 'NATIONAL', 5, 10.00),
	('STANDARD', 'NATIONAL', 10, 13.00),
	('STANDARD', 'NATIONAL', 20, 17.50),
	('EXPRESS', 'LOCAL', 2, 6.50),
	('EXPRESS', 'LOCAL', 5, 7.50),
	('EXPRESS', 'LOCAL', 10, 9.50),
	('EXPRESS', 'LOCAL', 20, 12.50),
	('EXPRESS', 'REGIONAL', 2, 8.50),
	('EXPRESS', 'REGIONAL', 5, 10.00),
	('EXPRESS', 'REGIONAL', 10, 12.50),
	('EXPRESS', 'REGIONAL', 20, 16.50),
	('EXPRESS', 'NATIONAL', 2, 11.00),
	('EXPRESS', 'NATIONAL', 5, 13.50),
	('EXPRESS', 'NATIONAL', 10, 17.00),
	('EXPRESS', 'NATIONAL', 20, 22.50);

insert into Route (origin, destination, vehicle_id) values
	('Oviedo Central Office', 'Leon Hub', 3),
	('Leon Hub', 'Oviedo Central Office', 3),
	('Gijon Office', 'Leon Hub', 3),
	('Leon Hub', 'Gijon Office', 3),
	('Leon Hub', 'Madrid Hub', 4),
	('Madrid Hub', 'Leon Hub', 4),
	('Madrid Hub', 'Madrid Office', 4),
	('Madrid Office', 'Madrid Hub', 4),
	('Customer Address Oviedo', 'Oviedo Central Office', 1),
	('Oviedo Central Office', 'Customer Address Oviedo', 1),
	('Customer Address Gijon', 'Gijon Office', 1),
	('Gijon Office', 'Customer Address Gijon', 1),
	('Customer Address Madrid', 'Madrid Office', 2),
	('Madrid Office', 'Customer Address Madrid', 2);

insert into Shipment (shipment_id, customer_id, recipient_name, recipient_phone, origin_address, origin_city, origin_office_id, destination_location, delivery_address, delivery_city, home_pickup, zone, service_level, price, channel, registration_date, status, failed_attempts) values
	(1, 1, 'Marta Lopez', '600555666', 'Calle Campoamor 3', 'Oviedo', 1, 'Madrid Office', 'Gran Via 60', 'Madrid', 1, 'NATIONAL', 'STANDARD', 18.00, 'TELEPHONE', '2026-06-08', 'IN_WAREHOUSE', 0),
	(2, 2, 'Pedro Suarez', '600777888', 'Avenida de la Costa 45', 'Gijon', 2, 'Oviedo Central Office', 'Calle Rosal 9', 'Oviedo', 0, 'REGIONAL', 'EXPRESS', 8.50, 'TELEPHONE', '2026-06-09', 'OUT_FOR_DELIVERY', 1),
	(3, 3, 'Ana Garcia', '600111222', 'Gran Via 60', 'Madrid', 3, 'Oviedo Central Office', 'Calle Campoamor 3', 'Oviedo', 1, 'NATIONAL', 'EXPRESS', 17.00, 'TELEPHONE', '2026-06-05', 'DELIVERED', 0),
	(4, 2, 'Lucia Blanco', '600444555', 'Avenida de la Costa 45', 'Gijon', 2, 'Gijon Office', 'Calle Marques 7', 'Gijon', 0, 'REGIONAL', 'STANDARD', 7.50, 'TELEPHONE', '2026-06-12', 'OUT_FOR_DELIVERY', 3),
	(5, 4, 'Jorge Vega', '600666777', 'Calle Uria 12', 'Oviedo', 1, 'Madrid Office', 'Plaza Mayor 1', 'Madrid', 0, 'NATIONAL', 'STANDARD', 30.50, 'TELEPHONE', '2026-06-14', 'IN_WAREHOUSE', 0);

insert into Package (package_id, shipment_id, barcode, weight_kg, description, status) values
	(1, 1, 'PKG-1-1', 2.5, 'Box of books', 'IN_WAREHOUSE'),
	(2, 1, 'PKG-1-2', 1.7, 'Small parcel', 'IN_WAREHOUSE'),
	(3, 2, 'PKG-2-1', 1.5, 'Documents envelope', 'OUT_FOR_DELIVERY'),
	(4, 3, 'PKG-3-1', 8.0, 'Electronics', 'DELIVERED'),
	(5, 4, 'PKG-4-1', 5.0, 'Glassware', 'OUT_FOR_DELIVERY'),
	(6, 5, 'PKG-5-1', 10.0, 'Heavy tools', 'IN_WAREHOUSE'),
	(7, 5, 'PKG-5-2', 20.0, 'Max weight crate', 'IN_WAREHOUSE');

insert into ShipmentLeg (shipment_id, leg_order, leg_type, origin, destination, vehicle_id, status) values
	(1, 1, 'PICKUP', 'Calle Campoamor 3, Oviedo', 'Oviedo Central Office', 1, 'COMPLETED'),
	(1, 2, 'DELIVERY', 'Madrid Office', 'Gran Via 60, Madrid', 2, 'PENDING'),
	(2, 1, 'DELIVERY', 'Oviedo Central Office', 'Calle Rosal 9, Oviedo', 1, 'IN_PROGRESS'),
	(3, 1, 'PICKUP', 'Gran Via 60, Madrid', 'Madrid Office', 2, 'COMPLETED'),
	(3, 2, 'DELIVERY', 'Oviedo Central Office', 'Calle Campoamor 3, Oviedo', 1, 'COMPLETED'),
	(4, 1, 'DELIVERY', 'Gijon Office', 'Calle Marques 7, Gijon', 1, 'IN_PROGRESS'),
	(5, 1, 'DELIVERY', 'Madrid Office', 'Plaza Mayor 1, Madrid', 2, 'PENDING');

insert into TrackingEvent (package_id, event_date, location, status, description) values
	(1, '2026-06-08', 'Calle Campoamor 3, Oviedo', 'REGISTERED', 'Shipment registered, picked up at home'),
	(1, '2026-06-08', 'Oviedo Central Office', 'IN_TRANSIT', 'Departed origin office'),
	(1, '2026-06-09', 'Leon Hub', 'IN_WAREHOUSE', 'Arrived at intermediate warehouse'),
	(2, '2026-06-08', 'Calle Campoamor 3, Oviedo', 'REGISTERED', 'Shipment registered, picked up at home'),
	(2, '2026-06-08', 'Oviedo Central Office', 'IN_TRANSIT', 'Departed origin office'),
	(2, '2026-06-09', 'Leon Hub', 'IN_WAREHOUSE', 'Arrived at intermediate warehouse'),
	(3, '2026-06-09', 'Gijon Office', 'REGISTERED', 'Shipment registered at office'),
	(3, '2026-06-09', 'Leon Hub', 'IN_TRANSIT', 'In transit to destination'),
	(3, '2026-06-10', 'Oviedo Central Office', 'OUT_FOR_DELIVERY', 'Out for delivery'),
	(3, '2026-06-10', 'Calle Rosal 9, Oviedo', 'FAILED_DELIVERY', 'Recipient absent, new attempt scheduled'),
	(4, '2026-06-05', 'Gran Via 60, Madrid', 'REGISTERED', 'Shipment registered, picked up at home'),
	(4, '2026-06-05', 'Madrid Hub', 'IN_WAREHOUSE', 'Arrived at intermediate warehouse'),
	(4, '2026-06-06', 'Leon Hub', 'IN_WAREHOUSE', 'Arrived at intermediate warehouse'),
	(4, '2026-06-06', 'Oviedo Central Office', 'OUT_FOR_DELIVERY', 'Out for delivery'),
	(4, '2026-06-06', 'Calle Campoamor 3, Oviedo', 'DELIVERED', 'Delivered to recipient'),
	(5, '2026-06-12', 'Avenida de la Costa 45, Gijon', 'REGISTERED', 'Shipment registered at office'),
	(5, '2026-06-13', 'Gijon Office', 'OUT_FOR_DELIVERY', 'Out for delivery'),
	(5, '2026-06-13', 'Calle Marques 7, Gijon', 'FAILED_DELIVERY', 'Recipient absent, new attempt scheduled'),
	(5, '2026-06-14', 'Calle Marques 7, Gijon', 'FAILED_DELIVERY', 'Recipient absent, new attempt scheduled'),
	(5, '2026-06-15', 'Calle Marques 7, Gijon', 'FAILED_DELIVERY', 'Recipient absent, new attempt scheduled'),
	(6, '2026-06-14', 'Calle Uria 12, Oviedo', 'REGISTERED', 'Shipment registered at office'),
	(6, '2026-06-15', 'Leon Hub', 'IN_WAREHOUSE', 'Arrived at intermediate warehouse'),
	(7, '2026-06-14', 'Calle Uria 12, Oviedo', 'REGISTERED', 'Shipment registered at office'),
	(7, '2026-06-15', 'Leon Hub', 'IN_WAREHOUSE', 'Arrived at intermediate warehouse');

insert into WarehouseVerification (package_id, warehouse_id, operation, verification_date, barcode_ok, visual_ok, weight_ok, measured_weight_kg, result) values
	(1, 1, 'UNLOAD', '2026-06-09', 1, 1, 1, 2.5, 'OK'),
	(2, 1, 'UNLOAD', '2026-06-09', 1, 1, 1, 1.7, 'OK'),
	(4, 2, 'UNLOAD', '2026-06-05', 1, 1, 1, 8.0, 'OK'),
	(4, 1, 'UNLOAD', '2026-06-06', 1, 1, 1, 8.0, 'OK');

insert into DeliveryAttempt (shipment_id, attempt_number, attempt_date, result, notes) values
	(2, 1, '2026-06-10', 'ABSENT', 'Recipient absent, rescheduled'),
	(3, 1, '2026-06-06', 'DELIVERED', 'Signed by recipient'),
	(4, 1, '2026-06-13', 'ABSENT', 'Recipient absent, rescheduled'),
	(4, 2, '2026-06-14', 'ABSENT', 'Recipient absent, rescheduled'),
	(4, 3, '2026-06-15', 'ABSENT', 'Recipient absent, rescheduled');

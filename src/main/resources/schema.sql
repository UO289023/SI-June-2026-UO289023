drop table if exists DeliveryAttempt;
drop table if exists WarehouseVerification;
drop table if exists TrackingEvent;
drop table if exists ShipmentLeg;
drop table if exists Package;
drop table if exists Shipment;
drop table if exists Route;
drop table if exists Rate;
drop table if exists Customer;
drop table if exists Vehicle;
drop table if exists Warehouse;
drop table if exists Office;

create table Office (
	office_id integer primary key autoincrement,
	name text not null,
	city text not null,
	address text not null
);

create table Warehouse (
	warehouse_id integer primary key autoincrement,
	name text not null,
	city text not null,
	address text not null
);

create table Vehicle (
	vehicle_id integer primary key autoincrement,
	plate text not null unique,
	type text not null,
	capacity_kg real not null
);

create table Customer (
	customer_id integer primary key autoincrement,
	name text not null,
	surname text not null,
	phone text not null,
	email text,
	address text not null,
	city text not null
);

create table Rate (
	rate_id integer primary key autoincrement,
	service_level text not null,
	zone text not null,
	max_weight_kg real not null,
	price real not null
);

create table Route (
	route_id integer primary key autoincrement,
	origin text not null,
	destination text not null,
	vehicle_id integer not null references Vehicle(vehicle_id)
);

create table Shipment (
	shipment_id integer primary key autoincrement,
	customer_id integer not null references Customer(customer_id),
	recipient_name text not null,
	recipient_phone text not null,
	origin_address text not null,
	origin_city text not null,
	origin_office_id integer not null references Office(office_id),
	destination_location text not null,
	delivery_address text not null,
	delivery_city text not null,
	home_pickup integer not null default 0,
	zone text not null,
	service_level text not null,
	price real not null,
	channel text not null,
	registration_date text not null,
	status text not null,
	failed_attempts integer not null default 0
);

create table Package (
	package_id integer primary key autoincrement,
	shipment_id integer not null references Shipment(shipment_id),
	barcode text not null unique,
	weight_kg real not null,
	description text,
	status text not null
);

create table ShipmentLeg (
	leg_id integer primary key autoincrement,
	shipment_id integer not null references Shipment(shipment_id),
	leg_order integer not null,
	leg_type text not null,
	origin text not null,
	destination text not null,
	vehicle_id integer references Vehicle(vehicle_id),
	status text not null
);

create table TrackingEvent (
	event_id integer primary key autoincrement,
	package_id integer not null references Package(package_id),
	event_date text not null,
	location text not null,
	status text not null,
	description text
);

create table WarehouseVerification (
	verification_id integer primary key autoincrement,
	package_id integer not null references Package(package_id),
	warehouse_id integer not null references Warehouse(warehouse_id),
	operation text not null,
	verification_date text not null,
	barcode_ok integer not null,
	visual_ok integer not null,
	weight_ok integer not null,
	measured_weight_kg real,
	result text not null
);

create table DeliveryAttempt (
	attempt_id integer primary key autoincrement,
	shipment_id integer not null references Shipment(shipment_id),
	attempt_number integer not null,
	attempt_date text not null,
	result text not null,
	notes text
);

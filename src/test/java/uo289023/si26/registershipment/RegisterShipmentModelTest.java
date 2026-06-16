package uo289023.si26.registershipment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import uo289023.si26.BaseModelTest;
import uo289023.si26.utils.ApplicationException;

public class RegisterShipmentModelTest extends BaseModelTest {

	private RegisterShipmentModel model = new RegisterShipmentModel();

	@Test
	public void testTotalPriceIsTheSumOfTheRateOfEachPackage() {
		double total = model.calculateTotalPrice("STANDARD", "NATIONAL", new double[] { 2.0, 6.0 });
		assertEquals(8.00 + 13.00, total, 0.001, "Total price must add the rate of each package");
	}

	@Test
	public void testRegisterShipmentWithHomePickupCreatesShipmentPackagesLegsAndEvents() {
		long shipmentsBefore = count("Shipment");
		long packagesBefore = count("Package");

		int id = model.registerShipment(1, "Marta Lopez", "600555666", true, "Calle Campoamor 3", "Oviedo", 1,
				"Madrid Office", "Gran Via 60", "Madrid", "NATIONAL", "STANDARD", new double[] { 2.0, 6.0 },
				new String[] { "Books", "Clothes" }, "2026-06-11");

		assertTrue(id > 5, "The new shipment id must be greater than the previous ones");
		assertEquals(shipmentsBefore + 1, count("Shipment"), "Exactly one shipment must be created");
		assertEquals(packagesBefore + 2, count("Package"), "Both packages must be created");

		Object[] shipment = firstRow(
				"select status, price, home_pickup, zone, service_level, channel, registration_date from Shipment where shipment_id=?",
				id);
		assertEquals("REGISTERED", shipment[0], "A new shipment must be in REGISTERED status");
		assertEquals(21.00, ((Number) shipment[1]).doubleValue(), 0.001, "Stored price must be the calculated total");
		assertEquals(1, ((Number) shipment[2]).intValue(), "Home pickup flag must be stored");
		assertEquals("NATIONAL", shipment[3], "Zone must be stored");
		assertEquals("STANDARD", shipment[4], "Service level must be stored");
		assertEquals("TELEPHONE", shipment[5], "Channel must be TELEPHONE for the office story");
		assertEquals("2026-06-11", shipment[6], "Registration date must be the simulated date");

		assertEquals(2, count("Package") - packagesBefore, "Two packages were registered");
		Object[] pkg1 = firstRow("select barcode, weight_kg, status from Package where shipment_id=? order by package_id limit 1", id);
		assertEquals("PKG-" + id + "-1", pkg1[0], "Barcode must be generated from the shipment id");
		assertEquals(2.0, ((Number) pkg1[1]).doubleValue(), 0.001, "Package weight must be stored");
		assertEquals("REGISTERED", pkg1[2], "Package must start in REGISTERED status");

		assertEquals(2, ((Number) firstRow("select count(*) from ShipmentLeg where shipment_id=?", id)[0]).intValue(),
				"Home pickup must create a pickup leg and a delivery leg");
		assertEquals(2,
				((Number) firstRow("select vehicle_id from ShipmentLeg where shipment_id=? and leg_type='PICKUP'", id)[0])
						.intValue(),
				"Pickup leg must use the vehicle assigned to the customer-side route");
		assertEquals(2, ((Number) firstRow(
				"select count(*) from TrackingEvent te join Package p on te.package_id=p.package_id where p.shipment_id=? and te.status='REGISTERED'",
				id)[0]).intValue(), "Each package must get a REGISTERED tracking event");
	}

	@Test
	public void testRegisterShipmentWithoutHomePickupCreatesOnlyDeliveryLeg() {
		int id = model.registerShipment(1, "Marta Lopez", "600555666", false, "", "", 1, "Madrid Office",
				"Gran Via 60", "Madrid", "NATIONAL", "STANDARD", new double[] { 3.0 }, new String[] { "Box" },
				"2026-06-11");

		assertEquals(1, ((Number) firstRow("select count(*) from ShipmentLeg where shipment_id=?", id)[0]).intValue(),
				"Without home pickup only the delivery leg must be created");
		assertNull(firstRow("select leg_id from ShipmentLeg where shipment_id=? and leg_type='PICKUP'", id),
				"There must be no pickup leg when there is no home pickup");
		Object[] origin = firstRow("select origin_address, origin_city from Shipment where shipment_id=?", id);
		assertEquals("Calle Uria 12", origin[0], "Origin must be the office address when there is no home pickup");
	}

	@Test
	public void testRegisterShipmentWithWeightOverMaximumThrowsAndDoesNotPersistAnything() {
		long shipmentsBefore = count("Shipment");
		long packagesBefore = count("Package");

		assertThrows(ApplicationException.class,
				() -> model.registerShipment(1, "Marta Lopez", "600555666", false, "", "", 1, "Madrid Office",
						"Gran Via 60", "Madrid", "NATIONAL", "STANDARD", new double[] { 25.0 },
						new String[] { "Too heavy" }, "2026-06-11"),
				"A package over the maximum weight has no rate and must abort the registration");

		assertEquals(shipmentsBefore, count("Shipment"), "No shipment must be created when the rate is not available");
		assertEquals(packagesBefore, count("Package"), "No package must be created when the rate is not available");
	}
}

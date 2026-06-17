package uo289023.si26.pa_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import uo289023.si26.BaseModelTest;
import uo289023.si26.registerdelivery.RegisterDeliveryModel;
import uo289023.si26.utils.ApplicationException;

// Use case 2 - Register package delivery (RegisterDeliveryModel)
// Each test is annotated with the test case (TC) and equivalence class it covers.
public class RegisterDeliveryModelTest extends BaseModelTest {

	private RegisterDeliveryModel model = new RegisterDeliveryModel();

	// TC7 - Use case 2 - support: pending list excludes DELIVERED and AT_OFFICE_FOR_PICKUP (4 pending)
	@Test
	public void testPendingDeliveriesExcludeDeliveredAndAtOfficeShipments() {
		assertEquals(4, model.getPendingDeliveries().size(),
				"Only shipments not yet finished must appear as pending");
	}

	// TC1 - Use case 2 - valid: result DELIVERED -> shipment DELIVERED, leg COMPLETED, packages DELIVERED
	@Test
	public void testSuccessfulDeliveryCompletesShipmentAndLeg() {
		String status = model.registerDelivery(3, "DELIVERED", "2026-06-11", "Signed by recipient");

		assertEquals("DELIVERED", status, "A successful delivery must finish the shipment");
		Object[] shipment = firstRow("select status, failed_attempts from Shipment where shipment_id=3");
		assertEquals("DELIVERED", shipment[0], "Shipment final status must be DELIVERED");
		assertEquals(0, ((Number) shipment[1]).intValue(), "A successful delivery must not add failed attempts");
		assertEquals("COMPLETED", firstRow("select status from ShipmentLeg where shipment_id=3 and leg_type='DELIVERY'")[0],
				"The delivery leg must be completed");
		assertEquals("DELIVERED", firstRow("select status from Package where shipment_id=3")[0],
				"The package must be marked as delivered");
		assertEquals("DELIVERED", firstRow("select result from DeliveryAttempt where shipment_id=3")[0],
				"A delivery attempt with result DELIVERED must be recorded");
	}

	// TC2 - Use case 2 - valid: ABSENT with 0 previous failures -> FAILED_DELIVERY, failed_attempts = 1
	@Test
	public void testFirstAbsentSchedulesANewAttempt() {
		String status = model.registerDelivery(3, "ABSENT", "2026-06-11", "Recipient absent");

		assertEquals("FAILED_DELIVERY", status, "An absent recipient on the first attempt schedules a new delivery");
		assertEquals(1, ((Number) firstRow("select failed_attempts from Shipment where shipment_id=3")[0]).intValue(),
				"The failed attempts counter must increase");
	}

	// TC3 - Use case 2 - valid (boundary): ABSENT on the third additional attempt -> FAILED_DELIVERY, failed_attempts = 3
	@Test
	public void testThirdAdditionalAbsentStillSchedulesANewAttempt() {
		String status = model.registerDelivery(4, "ABSENT", "2026-06-11", "Recipient absent");

		assertEquals("FAILED_DELIVERY", status, "Up to three additional deliveries are still allowed");
		assertEquals(3, ((Number) firstRow("select failed_attempts from Shipment where shipment_id=4")[0]).intValue(),
				"The third additional attempt must be recorded");
	}

	// TC4 - Use case 2 - valid (boundary): ABSENT with attempts exhausted (4th) -> AT_OFFICE_FOR_PICKUP, failed_attempts = 4
	@Test
	public void testFourthAbsentLeavesPackageAtDestinationOffice() {
		String status = model.registerDelivery(5, "ABSENT", "2026-06-11", "Recipient absent");

		assertEquals("AT_OFFICE_FOR_PICKUP", status,
				"After three additional failed deliveries the package remains at the office");
		assertEquals(4, ((Number) firstRow("select failed_attempts from Shipment where shipment_id=5")[0]).intValue(),
				"The fourth failed attempt must be recorded");
		assertEquals("AT_OFFICE_FOR_PICKUP", firstRow("select status from Package where shipment_id=5")[0],
				"The package must wait at the destination office for pickup");
	}

	// TC5 - Use case 2 - invalid class: delivery date earlier than registration -> exception, nothing persisted
	@Test
	public void testDeliverBeforeRegistrationDateThrowsAndDoesNotPersistAnything() {
		long attemptsBefore = count("DeliveryAttempt");
		assertThrows(ApplicationException.class, () -> model.registerDelivery(3, "DELIVERED", "2026-06-08", "Too early"),
				"A shipment cannot be delivered before it was registered");
		assertEquals("OUT_FOR_DELIVERY", firstRow("select status from Shipment where shipment_id=3")[0],
				"The shipment status must remain unchanged");
		assertEquals(attemptsBefore, count("DeliveryAttempt"),
				"No delivery attempt must be stored for a date earlier than registration");
	}

	// TC6 - Use case 2 - invalid class: a non-existent shipment is rejected -> exception
	@Test
	public void testRegisterDeliveryOnNonexistentShipmentThrows() {
		assertThrows(ApplicationException.class, () -> model.registerDelivery(999, "DELIVERED", "2026-06-11", ""),
				"Registering a delivery for an unknown shipment must throw");
	}
}

package uo289023.si26.trackshipment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import uo289023.si26.BaseModelTest;
import uo289023.si26.dtos.TrackedPackageDTO;
import uo289023.si26.dtos.TrackingEventDTO;

public class TrackShipmentModelTest extends BaseModelTest {

	private TrackShipmentModel model = new TrackShipmentModel();

	private static final String NOW = "2026-06-30";

	@Test
	public void testCustomerSeesAllOwnPackagesIncludingDeliveredOnes() {
		List<TrackedPackageDTO> packages = model.getCustomerPackages(1, NOW);
		assertEquals(3, packages.size(), "The customer must see every already-registered package of their shipments");
		assertTrue(packages.stream().anyMatch(p -> "PKG-2-1".equals(p.getBarcode()) && "DELIVERED".equals(p.getStatus())),
				"Delivered packages must still be visible when tracking");
	}

	@Test
	public void testEachCustomerSeesOnlyOwnPackages() {
		assertEquals(3, model.getCustomerPackages(2, NOW).size(), "Customer 2 must see only their own packages");
	}

	@Test
	public void testTrackedPackageShowsItsCurrentLocationAndStatus() {
		TrackedPackageDTO pkg = packageOf(1, "PKG-1-1", NOW);
		assertEquals("Leon Hub", pkg.getLastLocation(), "The last location must be the most recent event so far");
		assertEquals("IN_WAREHOUSE", pkg.getStatus(), "The status must reflect the current situation");
	}

	@Test
	public void testStatusReflectsThePastWhenConsultingAnEarlierDate() {
		TrackedPackageDTO pkg = packageOf(1, "PKG-1-1", "2026-06-08");
		assertEquals("IN_TRANSIT", pkg.getStatus(), "On 2026-06-08 the package had not reached the warehouse yet");
		assertNotEquals("IN_WAREHOUSE", pkg.getStatus(), "The final status must not leak into a past consultation");
		assertEquals("Oviedo Central Office", pkg.getLastLocation(), "The location must be the one known on that date");
	}

	@Test
	public void testPackageNotYetRegisteredIsNotVisibleInThePast() {
		assertTrue(model.getCustomerPackages(1, "2026-06-04").isEmpty(),
				"Before any registration date no package must appear in the tracking");
	}

	@Test
	public void testHistoryIsLimitedToTheConsultationDate() {
		assertEquals(2, model.getPackageEvents("PKG-1-1", "2026-06-08").size(),
				"Only the events up to the consultation date must be shown");
		assertEquals(3, model.getPackageEvents("PKG-1-1", NOW).size(),
				"Later on, the whole history up to the consultation date must be shown");
	}

	@Test
	public void testPackageEventsAreReturnedInChronologicalOrder() {
		List<TrackingEventDTO> events = model.getPackageEvents("PKG-1-1", NOW);
		assertEquals("REGISTERED", events.get(0).getStatus(), "The first event must be the registration");
		assertEquals("IN_WAREHOUSE", events.get(events.size() - 1).getStatus(),
				"The last event must be the most recent situation");
	}

	private TrackedPackageDTO packageOf(int customerId, String barcode, String asOfDate) {
		return model.getCustomerPackages(customerId, asOfDate).stream()
				.filter(p -> barcode.equals(p.getBarcode())).findFirst().orElseThrow();
	}
}

package uo289023.si26.verifypackage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import uo289023.si26.BaseModelTest;
import uo289023.si26.dtos.PackageDTO;
import uo289023.si26.utils.ApplicationException;

public class VerifyPackageModelTest extends BaseModelTest {

	private VerifyPackageModel model = new VerifyPackageModel();

	@Test
	public void testFindExistingPackageReturnsItsData() {
		PackageDTO pkg = model.findPackageByBarcode("PKG-1-1");
		assertNotNull(pkg, "An existing barcode must return the package");
		assertEquals(2.5, pkg.getWeightKg(), 0.001, "The declared weight must be read from the database");
	}

	@Test
	public void testFindNonexistentPackageReturnsNull() {
		assertNull(model.findPackageByBarcode("DOES-NOT-EXIST"), "A wrong barcode must not match any package");
	}

	@Test
	public void testWeightToleranceBoundary() {
		assertTrue(model.isWeightOk(2.5, 2.625), "A 5% difference is still within tolerance");
		assertFalse(model.isWeightOk(2.5, 2.7), "A difference above 5% means weight loss");
	}

	@Test
	public void testUnloadValidPackageGivesOkResultAndInWarehouseStatus() {
		String status = model.verifyPackage("PKG-1-1", 1, "UNLOAD", "2026-06-11", true, 2.5);

		assertEquals("IN_WAREHOUSE", status, "A correct unloaded package must be in the warehouse");
		Object[] verification = firstRow(
				"select result, barcode_ok, visual_ok, weight_ok from WarehouseVerification where package_id=1");
		assertEquals("OK", verification[0], "Result must be OK when all checks pass");
		assertEquals(1, ((Number) verification[1]).intValue(), "Barcode check must be ok");
		assertEquals(1, ((Number) verification[2]).intValue(), "Visual check must be ok");
		assertEquals(1, ((Number) verification[3]).intValue(), "Weight check must be ok");
		assertEquals("IN_WAREHOUSE", firstRow("select status from Package where package_id=1")[0],
				"Package status must be updated");
	}

	@Test
	public void testLoadValidPackageGivesInTransitStatus() {
		String status = model.verifyPackage("PKG-1-1", 1, "LOAD", "2026-06-11", true, 2.5);
		assertEquals("IN_TRANSIT", status, "A correct loaded package must leave the warehouse in transit");
	}

	@Test
	public void testVisualDamageGivesIncidentAndRetainsPackage() {
		String status = model.verifyPackage("PKG-1-1", 1, "UNLOAD", "2026-06-11", false, 2.5);

		assertEquals("RETAINED", status, "A damaged package must be retained");
		Object[] verification = firstRow(
				"select result, visual_ok, weight_ok from WarehouseVerification where package_id=1");
		assertEquals("INCIDENT", verification[0], "Result must be an incident when the visual check fails");
		assertEquals(0, ((Number) verification[1]).intValue(), "Visual check must be recorded as failed");
	}

	@Test
	public void testWeightLossGivesIncidentAndRetainsPackage() {
		String status = model.verifyPackage("PKG-1-1", 1, "UNLOAD", "2026-06-11", true, 2.0);

		assertEquals("RETAINED", status, "A package with weight loss must be retained");
		Object[] verification = firstRow(
				"select result, weight_ok from WarehouseVerification where package_id=1");
		assertEquals("INCIDENT", verification[0], "Result must be an incident when weight is lost");
		assertEquals(0, ((Number) verification[1]).intValue(), "Weight check must be recorded as failed");
	}

	@Test
	public void testVerifyBeforeRegistrationDateThrowsAndDoesNotPersistAnything() {
		long verificationsBefore = count("WarehouseVerification");
		assertThrows(ApplicationException.class,
				() -> model.verifyPackage("PKG-1-1", 1, "UNLOAD", "2026-06-07", true, 2.5),
				"A package cannot be verified before its shipment was registered");
		assertEquals(verificationsBefore, count("WarehouseVerification"),
				"No verification must be stored for a date earlier than registration");
		assertEquals("IN_WAREHOUSE", firstRow("select status from Package where package_id=1")[0],
				"The package status must remain unchanged");
	}

	@Test
	public void testVerifyNonexistentPackageThrowsAndDoesNotPersistAnything() {
		long verificationsBefore = count("WarehouseVerification");
		assertThrows(ApplicationException.class,
				() -> model.verifyPackage("DOES-NOT-EXIST", 1, "UNLOAD", "2026-06-11", true, 2.5),
				"Verifying a wrong package must fail");
		assertEquals(verificationsBefore, count("WarehouseVerification"),
				"No verification must be stored for a wrong package");
	}
}

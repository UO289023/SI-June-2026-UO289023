package uo289023.si26.calculaterate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import uo289023.si26.BaseModelTest;
import uo289023.si26.utils.ApplicationException;

public class CalculateRateModelTest extends BaseModelTest {

	private CalculateRateModel model = new CalculateRateModel();

	@ParameterizedTest
	@CsvSource({
			"STANDARD, LOCAL, 1.0, 4.50",
			"STANDARD, LOCAL, 2.0, 4.50",
			"STANDARD, REGIONAL, 5.0, 7.50",
			"STANDARD, NATIONAL, 4.2, 10.00",
			"STANDARD, NATIONAL, 10.0, 13.00",
			"STANDARD, NATIONAL, 15.0, 17.50",
			"EXPRESS, LOCAL, 1.0, 6.50",
			"EXPRESS, REGIONAL, 7.0, 12.50",
			"EXPRESS, NATIONAL, 20.0, 22.50" })
	public void testRateByServiceZoneAndWeightTier(String serviceLevel, String zone, double weight,
			double expectedPrice) {
		assertEquals(expectedPrice, model.getRate(serviceLevel, zone, weight), 0.001,
				"Rate for " + serviceLevel + "/" + zone + "/" + weight + "kg");
	}

	@Test
	public void testExpressIsMoreExpensiveThanStandardForSameWeightAndZone() {
		double standard = model.getRate("STANDARD", "NATIONAL", 4.2);
		double express = model.getRate("EXPRESS", "NATIONAL", 4.2);
		assertTrue(express > standard,
				"Express (" + express + ") must be more expensive than standard (" + standard + ")");
	}

	@Test
	public void testWeightOverMaximumTierThrows() {
		assertThrows(ApplicationException.class, () -> model.getRate("STANDARD", "NATIONAL", 25.0),
				"Weight over the maximum tier has no rate and must throw");
	}

	@Test
	public void testNonexistentServiceLevelThrows() {
		assertThrows(ApplicationException.class, () -> model.getRate("URGENT", "NATIONAL", 4.2),
				"A nonexistent service level has no rate and must throw");
	}

	@Test
	public void testNonexistentZoneThrows() {
		assertThrows(ApplicationException.class, () -> model.getRate("STANDARD", "INTERNATIONAL", 4.2),
				"A nonexistent zone has no rate and must throw");
	}

	@Test
	public void testGetAllRatesReturnsTheWholeRateTable() {
		assertEquals(24, model.getAllRates().size(), "The rate table must contain all configured rates");
	}
}

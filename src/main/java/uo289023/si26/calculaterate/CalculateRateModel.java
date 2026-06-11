package uo289023.si26.calculaterate;

import java.util.List;

import uo289023.si26.dtos.RateDTO;
import uo289023.si26.utils.ApplicationException;
import uo289023.si26.utils.Database;

public class CalculateRateModel {
	protected Database db = new Database();

	public double getRate(String serviceLevel, String zone, double weightKg) {
		List<Object[]> result = db.executeQueryArray(
				"select min(price) from Rate where service_level=? and zone=? and max_weight_kg>=?",
				serviceLevel, zone, weightKg);
		if (result.isEmpty() || result.get(0)[0] == null)
			throw new ApplicationException("No rate available for " + serviceLevel + "/" + zone + " and weight " + weightKg + " kg");
		return ((Number) result.get(0)[0]).doubleValue();
	}

	public List<RateDTO> getAllRates() {
		return db.executeQueryPojo(RateDTO.class,
				"select service_level as serviceLevel, zone, max_weight_kg as maxWeightKg, price from Rate order by service_level, zone, max_weight_kg");
	}
}

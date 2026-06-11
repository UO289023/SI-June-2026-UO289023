package uo289023.si26.dtos;

public class RateDTO {
	private String serviceLevel;
	private String zone;
	private double maxWeightKg;
	private double price;

	public String getServiceLevel() {
		return serviceLevel;
	}

	public void setServiceLevel(String serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public double getMaxWeightKg() {
		return maxWeightKg;
	}

	public void setMaxWeightKg(double maxWeightKg) {
		this.maxWeightKg = maxWeightKg;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}

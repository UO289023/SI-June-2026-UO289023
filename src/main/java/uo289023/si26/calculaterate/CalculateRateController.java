package uo289023.si26.calculaterate;

import java.util.List;

import uo289023.si26.dtos.RateDTO;
import uo289023.si26.utils.ApplicationException;
import uo289023.si26.utils.SwingUtil;

public class CalculateRateController {

	private CalculateRateModel model;
	private CalculateRateView view;
	private String simulatedDate;

	public CalculateRateController(CalculateRateModel model, CalculateRateView view) {
		this.model = model;
		this.view = view;
	}

	public void setSimulatedDate(String simulatedDate) {
		this.simulatedDate = simulatedDate;
	}

	public String getSimulatedDate() {
		return simulatedDate;
	}

	public void initController() {
		loadRatesTable();
		view.getBtnCalculate().addActionListener(e -> SwingUtil.exceptionWrapper(this::calculateRate));
		view.getFrame().setVisible(true);
	}

	private void loadRatesTable() {
		List<RateDTO> rates = model.getAllRates();
		view.getTableRates().setModel(SwingUtil.getTableModelFromPojos(rates,
				new String[] { "serviceLevel", "zone", "maxWeightKg", "price" }));
		SwingUtil.autoAdjustColumns(view.getTableRates());
	}

	private void calculateRate() {
		double weight;
		try {
			weight = Double.parseDouble(view.getTxtWeight().getText().trim());
		} catch (NumberFormatException e) {
			throw new ApplicationException("Weight must be a number");
		}
		if (weight <= 0)
			throw new ApplicationException("Weight must be greater than zero");
		String serviceLevel = (String) view.getCbServiceLevel().getSelectedItem();
		String zone = (String) view.getCbZone().getSelectedItem();
		double rate = model.getRate(serviceLevel, zone, weight);
		view.getLblResult().setText(String.format("Rate: %.2f EUR", rate));
	}
}

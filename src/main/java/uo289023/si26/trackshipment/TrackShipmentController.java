package uo289023.si26.trackshipment;

import java.util.List;

import uo289023.si26.dtos.TrackedPackageDTO;
import uo289023.si26.dtos.TrackingEventDTO;
import uo289023.si26.utils.SwingUtil;

public class TrackShipmentController {

	private TrackShipmentModel model;
	private TrackShipmentView view;
	private String simulatedDate;

	public TrackShipmentController(TrackShipmentModel model, TrackShipmentView view) {
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
		loadCustomers();
		view.getCbCustomer().addActionListener(e -> SwingUtil.exceptionWrapper(this::loadPackages));
		view.getTablePackages().getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting())
				SwingUtil.exceptionWrapper(this::loadEvents);
		});
		loadPackages();
		view.getFrame().setVisible(true);
	}

	private void loadCustomers() {
		view.getCbCustomer().removeAllItems();
		for (Object[] row : model.getCustomers())
			view.getCbCustomer().addItem(row[0] + " - " + row[1]);
	}

	private void loadPackages() {
		if (view.getCbCustomer().getSelectedItem() == null)
			return;
		int customerId = Integer.parseInt(((String) view.getCbCustomer().getSelectedItem()).split(" - ")[0]);
		List<TrackedPackageDTO> packages = model.getCustomerPackages(customerId, simulatedDate);
		view.getTablePackages().setModel(SwingUtil.getTableModelFromPojos(packages,
				new String[] { "barcode", "registrationDate", "deliveryPlace", "status", "lastLocation" }));
		SwingUtil.autoAdjustColumns(view.getTablePackages());
		view.getTableEvents().setModel(SwingUtil.getTableModelFromPojos(null,
				new String[] { "eventDate", "location", "status", "description" }));
	}

	private void loadEvents() {
		String barcode = SwingUtil.getSelectedKey(view.getTablePackages());
		if (barcode.isEmpty())
			return;
		List<TrackingEventDTO> events = model.getPackageEvents(barcode, simulatedDate);
		view.getTableEvents().setModel(SwingUtil.getTableModelFromPojos(events,
				new String[] { "eventDate", "location", "status", "description" }));
		SwingUtil.autoAdjustColumns(view.getTableEvents());
	}
}

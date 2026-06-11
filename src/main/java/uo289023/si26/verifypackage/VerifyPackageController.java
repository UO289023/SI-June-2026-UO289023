package uo289023.si26.verifypackage;

import javax.swing.JOptionPane;

import uo289023.si26.dtos.PackageDTO;
import uo289023.si26.utils.ApplicationException;
import uo289023.si26.utils.SwingUtil;

public class VerifyPackageController {

	private static final double WEIGHT_TOLERANCE = 0.05;

	private VerifyPackageModel model;
	private VerifyPackageView view;
	private String simulatedDate;
	private PackageDTO currentPackage;

	public VerifyPackageController(VerifyPackageModel model, VerifyPackageView view) {
		this.model = model;
		this.view = view;
	}

	public void setSimulatedDate(String simulatedDate) {
		this.simulatedDate = simulatedDate;
	}

	public void initController() {
		loadWarehouses();
		view.getBtnSearch().addActionListener(e -> SwingUtil.exceptionWrapper(this::searchPackage));
		view.getBtnRegisterVerification().addActionListener(e -> SwingUtil.exceptionWrapper(this::registerVerification));
		view.getFrame().setVisible(true);
	}

	private void loadWarehouses() {
		view.getCbWarehouse().removeAllItems();
		for (Object[] row : model.getWarehouses())
			view.getCbWarehouse().addItem(row[0] + " - " + row[1]);
	}

	private void searchPackage() {
		String barcode = view.getTxtBarcode().getText().trim();
		if (barcode.isEmpty())
			throw new ApplicationException("Enter a barcode to read");
		currentPackage = model.findPackageByBarcode(barcode);
		if (currentPackage == null) {
			view.getTablePackageInfo().setModel(new javax.swing.table.DefaultTableModel());
			throw new ApplicationException("No package found with barcode " + barcode + ": verification failed, wrong package");
		}
		view.getTablePackageInfo().setModel(SwingUtil.getRecordModelFromPojo(currentPackage,
				new String[] { "packageId", "shipmentId", "barcode", "weightKg", "description", "status" }));
		SwingUtil.autoAdjustColumns(view.getTablePackageInfo());
	}

	private void registerVerification() {
		if (currentPackage == null)
			throw new ApplicationException("Read a package barcode first");
		if (view.getCbWarehouse().getSelectedItem() == null)
			throw new ApplicationException("Select a warehouse");
		double measuredWeight;
		try {
			measuredWeight = Double.parseDouble(view.getTxtMeasuredWeight().getText().trim());
		} catch (NumberFormatException e) {
			throw new ApplicationException("Measured weight must be a number");
		}
		if (measuredWeight <= 0)
			throw new ApplicationException("Measured weight must be greater than zero");

		int warehouseId = Integer.parseInt(((String) view.getCbWarehouse().getSelectedItem()).split(" - ")[0]);
		String warehouseName = ((String) view.getCbWarehouse().getSelectedItem()).split(" - ")[1];
		String operation = (String) view.getCbOperation().getSelectedItem();
		boolean visualOk = view.getChkVisualOk().isSelected();
		boolean weightOk = Math.abs(measuredWeight - currentPackage.getWeightKg()) <= WEIGHT_TOLERANCE
				* currentPackage.getWeightKg();
		boolean allOk = visualOk && weightOk;
		String result = allOk ? "OK" : "INCIDENT";

		model.insertVerification(currentPackage.getPackageId(), warehouseId, operation, simulatedDate, 1,
				visualOk ? 1 : 0, weightOk ? 1 : 0, measuredWeight, result);

		String newStatus;
		String description;
		if (!allOk) {
			newStatus = "RETAINED";
			description = "Verification incident on " + operation.toLowerCase() + ": "
					+ (visualOk ? "" : "packaging damaged ") + (weightOk ? "" : "weight loss detected");
		} else if ("LOAD".equals(operation)) {
			newStatus = "IN_TRANSIT";
			description = "Package verified and loaded at warehouse";
		} else {
			newStatus = "IN_WAREHOUSE";
			description = "Package verified and unloaded at warehouse";
		}
		model.updatePackageStatus(currentPackage.getPackageId(), newStatus);
		model.insertTrackingEvent(currentPackage.getPackageId(), simulatedDate, warehouseName, newStatus,
				description.trim());

		JOptionPane.showMessageDialog(view.getFrame(),
				"Verification registered with result " + result + ".\nPackage status: " + newStatus,
				"Verification Registered",
				allOk ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
		searchPackage();
	}
}

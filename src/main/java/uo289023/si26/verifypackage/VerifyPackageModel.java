package uo289023.si26.verifypackage;

import java.util.List;

import uo289023.si26.dtos.PackageDTO;
import uo289023.si26.utils.ApplicationException;
import uo289023.si26.utils.Database;

public class VerifyPackageModel {

	private static final double WEIGHT_TOLERANCE = 0.05;

	protected Database db = new Database();

	public List<Object[]> getWarehouses() {
		return db.executeQueryArray("select warehouse_id, name from Warehouse order by name");
	}

	public PackageDTO findPackageByBarcode(String barcode) {
		List<PackageDTO> result = db.executeQueryPojo(PackageDTO.class,
				"select package_id as packageId, shipment_id as shipmentId, barcode, weight_kg as weightKg, description, status from Package where barcode=?",
				barcode);
		return result.isEmpty() ? null : result.get(0);
	}

	public String getShipmentRegistrationDate(int shipmentId) {
		List<Object[]> result = db.executeQueryArray("select registration_date from Shipment where shipment_id=?",
				shipmentId);
		if (result.isEmpty())
			throw new ApplicationException("Shipment not found: " + shipmentId);
		return (String) result.get(0)[0];
	}

	public String getWarehouseName(int warehouseId) {
		List<Object[]> result = db.executeQueryArray("select name from Warehouse where warehouse_id=?", warehouseId);
		if (result.isEmpty())
			throw new ApplicationException("Warehouse not found: " + warehouseId);
		return (String) result.get(0)[0];
	}

	public boolean isWeightOk(double declaredWeightKg, double measuredWeightKg) {
		return Math.abs(measuredWeightKg - declaredWeightKg) <= WEIGHT_TOLERANCE * declaredWeightKg;
	}

	public String verifyPackage(String barcode, int warehouseId, String operation, String verificationDate,
			boolean visualOk, double measuredWeightKg) {
		PackageDTO pkg = findPackageByBarcode(barcode);
		if (pkg == null)
			throw new ApplicationException(
					"No package found with barcode " + barcode + ": verification failed, wrong package");
		String registrationDate = getShipmentRegistrationDate(pkg.getShipmentId());
		if (verificationDate.compareTo(registrationDate) < 0)
			throw new ApplicationException("Verification date " + verificationDate
					+ " cannot be earlier than the shipment registration date " + registrationDate);
		boolean weightOk = isWeightOk(pkg.getWeightKg(), measuredWeightKg);
		boolean allOk = visualOk && weightOk;
		String result = allOk ? "OK" : "INCIDENT";

		insertVerification(pkg.getPackageId(), warehouseId, operation, verificationDate, 1, visualOk ? 1 : 0,
				weightOk ? 1 : 0, measuredWeightKg, result);

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
		updatePackageStatus(pkg.getPackageId(), newStatus);
		insertTrackingEvent(pkg.getPackageId(), verificationDate, getWarehouseName(warehouseId), newStatus,
				description.trim());
		return newStatus;
	}

	public void insertVerification(int packageId, int warehouseId, String operation, String verificationDate,
			int barcodeOk, int visualOk, int weightOk, double measuredWeightKg, String result) {
		db.executeUpdate(
				"insert into WarehouseVerification (package_id, warehouse_id, operation, verification_date, barcode_ok, visual_ok, weight_ok, measured_weight_kg, result) values (?,?,?,?,?,?,?,?,?)",
				packageId, warehouseId, operation, verificationDate, barcodeOk, visualOk, weightOk, measuredWeightKg,
				result);
	}

	public void updatePackageStatus(int packageId, String status) {
		db.executeUpdate("update Package set status=? where package_id=?", status, packageId);
	}

	public void insertTrackingEvent(int packageId, String eventDate, String location, String status,
			String description) {
		db.executeUpdate(
				"insert into TrackingEvent (package_id, event_date, location, status, description) values (?,?,?,?,?)",
				packageId, eventDate, location, status, description);
	}
}

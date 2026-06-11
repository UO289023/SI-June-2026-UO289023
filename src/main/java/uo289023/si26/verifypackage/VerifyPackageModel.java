package uo289023.si26.verifypackage;

import java.util.List;

import uo289023.si26.dtos.PackageDTO;
import uo289023.si26.utils.Database;

public class VerifyPackageModel {
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

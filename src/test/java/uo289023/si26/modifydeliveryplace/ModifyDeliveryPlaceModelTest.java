package uo289023.si26.modifydeliveryplace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import uo289023.si26.BaseModelTest;
import uo289023.si26.dtos.ShipmentDTO;
import uo289023.si26.utils.ApplicationException;

public class ModifyDeliveryPlaceModelTest extends BaseModelTest {

	private ModifyDeliveryPlaceModel model = new ModifyDeliveryPlaceModel();

	@Test
	public void testActiveShipmentsExcludeAlreadyDeliveredOnes() {
		List<ShipmentDTO> active = model.getActiveShipments(1);
		assertEquals(1, active.size(), "A delivered shipment can no longer change its delivery place");
		assertEquals(1, active.get(0).getShipmentId(), "Only the in-progress shipment must be modifiable");
	}

	@Test
	public void testChangeToNewAddressUpdatesShipmentAndPendingDeliveryLeg() {
		model.updateDeliveryPlace(1, "Calle Nueva 5", "Madrid");

		Object[] shipment = firstRow("select delivery_address, delivery_city from Shipment where shipment_id=1");
		assertEquals("Calle Nueva 5", shipment[0], "The delivery address must be updated");
		assertEquals("Madrid", shipment[1], "The delivery city must be updated");
		assertEquals("Calle Nueva 5, Madrid",
				firstRow("select destination from ShipmentLeg where shipment_id=1 and leg_type='DELIVERY'")[0],
				"The pending delivery leg must point to the new place");
	}

	@Test
	public void testChangeToOfficeUsesThatLocationAddress() {
		Object[] location = model.getLocation("Madrid Office");
		model.updateDeliveryPlace(1, (String) location[1], (String) location[2]);

		Object[] shipment = firstRow("select delivery_address, delivery_city from Shipment where shipment_id=1");
		assertEquals("Calle Alcala 200", shipment[0], "The delivery address must be the office address");
		assertEquals("Madrid", shipment[1], "The delivery city must be the office city");
	}

	@Test
	public void testChangingOneShipmentDoesNotAffectOthers() {
		model.updateDeliveryPlace(1, "Calle Nueva 5", "Madrid");
		assertEquals("Calle Rosal 9", firstRow("select delivery_address from Shipment where shipment_id=3")[0],
				"Modifying one shipment must not change the delivery place of others");
	}

	@Test
	public void testGetNonexistentLocationThrows() {
		assertThrows(ApplicationException.class, () -> model.getLocation("Nowhere Office"),
				"An unknown office or warehouse must throw");
	}
}

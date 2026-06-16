package uo289023.si26.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import uo289023.si26.calculaterate.CalculateRateController;
import uo289023.si26.calculaterate.CalculateRateModel;
import uo289023.si26.calculaterate.CalculateRateView;
import uo289023.si26.modifydeliveryplace.ModifyDeliveryPlaceController;
import uo289023.si26.modifydeliveryplace.ModifyDeliveryPlaceModel;
import uo289023.si26.modifydeliveryplace.ModifyDeliveryPlaceView;
import uo289023.si26.registerdelivery.RegisterDeliveryController;
import uo289023.si26.registerdelivery.RegisterDeliveryModel;
import uo289023.si26.registerdelivery.RegisterDeliveryView;
import uo289023.si26.registershipment.RegisterShipmentController;
import uo289023.si26.registershipment.RegisterShipmentModel;
import uo289023.si26.registershipment.RegisterShipmentView;
import uo289023.si26.trackshipment.TrackShipmentController;
import uo289023.si26.trackshipment.TrackShipmentModel;
import uo289023.si26.trackshipment.TrackShipmentView;
import uo289023.si26.utils.Database;
import uo289023.si26.utils.SwingUtil;
import uo289023.si26.utils.Util;
import uo289023.si26.verifypackage.VerifyPackageController;
import uo289023.si26.verifypackage.VerifyPackageModel;
import uo289023.si26.verifypackage.VerifyPackageView;

public class MainWindow {

	private JFrame frame;
	private JTextField txtSystemDate;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			MainWindow window = new MainWindow();
			window.frame.setVisible(true);
		});
	}

	public MainWindow() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Package Shipping Company - SI June 2026");
		frame.setBounds(100, 100, 720, 480);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(5, 5));

		JPanel topPanel = new JPanel(new BorderLayout(5, 0));
		JLabel lblDate = new JLabel("<html>System Date (yyyy-MM-dd) <font color='red'>*</font></html>");
		lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtSystemDate = new JTextField(Util.dateToIsoString(new Date()));
		txtSystemDate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		topPanel.add(lblDate, BorderLayout.WEST);
		topPanel.add(txtSystemDate, BorderLayout.CENTER);
		frame.getContentPane().add(topPanel, BorderLayout.NORTH);

		JPanel featuresPanel = new JPanel(new GridLayout(0, 2, 15, 15));
		featuresPanel.setBorder(BorderFactory.createCompoundBorder(
				new TitledBorder(null, "User Stories", TitledBorder.LEADING, TitledBorder.TOP,
						new Font("Segoe UI", Font.BOLD, 14), new Color(0, 120, 215)),
				BorderFactory.createEmptyBorder(15, 15, 15, 15)));
		frame.getContentPane().add(featuresPanel, BorderLayout.CENTER);

		JButton btnRegisterShipment = new JButton("Register Shipment by Telephone");
		btnRegisterShipment.addActionListener(e -> SwingUtil.exceptionWrapper(() -> {
			if (!checkDateAndConfirm())
				return;
			RegisterShipmentModel model = new RegisterShipmentModel();
			RegisterShipmentView view = new RegisterShipmentView();
			RegisterShipmentController controller = new RegisterShipmentController(model, view);
			controller.setSimulatedDate(txtSystemDate.getText());
			controller.initController();
		}));

		JButton btnCalculateRate = new JButton("Calculate Shipment Rate");
		btnCalculateRate.addActionListener(e -> SwingUtil.exceptionWrapper(() -> {
			if (!checkDateAndConfirm())
				return;
			CalculateRateModel model = new CalculateRateModel();
			CalculateRateView view = new CalculateRateView();
			CalculateRateController controller = new CalculateRateController(model, view);
			controller.setSimulatedDate(txtSystemDate.getText());
			controller.initController();
		}));

		JButton btnVerifyPackage = new JButton("Verify Package at Warehouse");
		btnVerifyPackage.addActionListener(e -> SwingUtil.exceptionWrapper(() -> {
			if (!checkDateAndConfirm())
				return;
			VerifyPackageModel model = new VerifyPackageModel();
			VerifyPackageView view = new VerifyPackageView();
			VerifyPackageController controller = new VerifyPackageController(model, view);
			controller.setSimulatedDate(txtSystemDate.getText());
			controller.initController();
		}));

		JButton btnTrackShipment = new JButton("Track Shipments");
		btnTrackShipment.addActionListener(e -> SwingUtil.exceptionWrapper(() -> {
			if (!checkDateAndConfirm())
				return;
			TrackShipmentModel model = new TrackShipmentModel();
			TrackShipmentView view = new TrackShipmentView();
			TrackShipmentController controller = new TrackShipmentController(model, view);
			controller.setSimulatedDate(txtSystemDate.getText());
			controller.initController();
		}));

		JButton btnModifyDeliveryPlace = new JButton("Modify Delivery Place");
		btnModifyDeliveryPlace.addActionListener(e -> SwingUtil.exceptionWrapper(() -> {
			if (!checkDateAndConfirm())
				return;
			ModifyDeliveryPlaceModel model = new ModifyDeliveryPlaceModel();
			ModifyDeliveryPlaceView view = new ModifyDeliveryPlaceView();
			ModifyDeliveryPlaceController controller = new ModifyDeliveryPlaceController(model, view);
			controller.setSimulatedDate(txtSystemDate.getText());
			controller.initController();
		}));

		JButton btnRegisterDelivery = new JButton("Register Package Delivery");
		btnRegisterDelivery.addActionListener(e -> SwingUtil.exceptionWrapper(() -> {
			if (!checkDateAndConfirm())
				return;
			RegisterDeliveryModel model = new RegisterDeliveryModel();
			RegisterDeliveryView view = new RegisterDeliveryView();
			RegisterDeliveryController controller = new RegisterDeliveryController(model, view);
			controller.setSimulatedDate(txtSystemDate.getText());
			controller.initController();
		}));

		featuresPanel.add(btnRegisterShipment);
		featuresPanel.add(btnCalculateRate);
		featuresPanel.add(btnVerifyPackage);
		featuresPanel.add(btnTrackShipment);
		featuresPanel.add(btnModifyDeliveryPlace);
		featuresPanel.add(btnRegisterDelivery);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
		frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

		JButton btnInitializeDatabase = new JButton("Initialize Blank Database");
		btnInitializeDatabase.addActionListener(e -> SwingUtil.exceptionWrapper(() -> {
			Database db = new Database();
			db.createDatabase(false);
		}));
		bottomPanel.add(btnInitializeDatabase);

		JButton btnLoadSampleData = new JButton("Load Sample Data");
		btnLoadSampleData.addActionListener(e -> SwingUtil.exceptionWrapper(() -> {
			Database db = new Database();
			db.createDatabase(false);
			db.loadDatabase();
		}));
		bottomPanel.add(btnLoadSampleData);
	}

	public JFrame getFrame() {
		return frame;
	}

	public JTextField getTxtSystemDate() {
		return txtSystemDate;
	}

	private boolean checkDateAndConfirm() {
		String dateStr = txtSystemDate.getText().trim();
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
			LocalDate.parse(dateStr, formatter);
			return true;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(frame,
					"The system date '" + dateStr + "' is not a valid date (expected format yyyy-MM-dd).\nPlease enter a valid date before using the system.",
					"Invalid System Date", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
}

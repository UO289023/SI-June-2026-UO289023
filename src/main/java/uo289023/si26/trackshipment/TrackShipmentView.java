package uo289023.si26.trackshipment;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

public class TrackShipmentView {

	private JFrame frame;
	private JComboBox<String> cbCustomer;
	private JTable tablePackages;
	private JTable tableEvents;

	public TrackShipmentView() {
		frame = new JFrame();
		frame.setTitle("Track Shipments");
		frame.setBounds(200, 120, 760, 560);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(5, 5));

		JPanel topPanel = new JPanel(new BorderLayout(5, 0));
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
		topPanel.add(new JLabel("Customer:"), BorderLayout.WEST);
		cbCustomer = new JComboBox<>();
		topPanel.add(cbCustomer, BorderLayout.CENTER);
		frame.getContentPane().add(topPanel, BorderLayout.NORTH);

		JPanel centerPanel = new JPanel(new GridLayout(2, 1, 5, 5));

		JPanel packagesPanel = new JPanel(new BorderLayout());
		packagesPanel.setBorder(BorderFactory.createTitledBorder("My Packages"));
		tablePackages = new JTable();
		tablePackages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		packagesPanel.add(new JScrollPane(tablePackages), BorderLayout.CENTER);
		centerPanel.add(packagesPanel);

		JPanel eventsPanel = new JPanel(new BorderLayout());
		eventsPanel.setBorder(BorderFactory.createTitledBorder("Situation and Status History"));
		tableEvents = new JTable();
		eventsPanel.add(new JScrollPane(tableEvents), BorderLayout.CENTER);
		centerPanel.add(eventsPanel);

		frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
	}

	public JFrame getFrame() {
		return frame;
	}

	public JComboBox<String> getCbCustomer() {
		return cbCustomer;
	}

	public JTable getTablePackages() {
		return tablePackages;
	}

	public JTable getTableEvents() {
		return tableEvents;
	}
}

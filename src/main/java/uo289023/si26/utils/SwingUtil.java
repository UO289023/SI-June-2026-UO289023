package uo289023.si26.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.beanutils.PropertyUtils;

public class SwingUtil {
	private SwingUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static void exceptionWrapper(Runnable consumer) {
		try {
			consumer.run();
		} catch (ApplicationException e) {
			showMessage(e.getMessage(), "Information", JOptionPane.INFORMATION_MESSAGE);
		} catch (RuntimeException e) {
			e.printStackTrace();
			showMessage(e.toString(), "Unexpected exception", JOptionPane.ERROR_MESSAGE);
		}
	}

	private static void showMessage(String message, String title, int type) {
		JOptionPane pane = new JOptionPane(message, type, JOptionPane.DEFAULT_OPTION);
		pane.setOptions(new Object[] { "OK" });
		JDialog d = pane.createDialog(pane, title);
		d.setLocation(200, 200);
		d.setVisible(true);
	}

	public static void autoAdjustColumns(JTable table) {
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnAdjuster tca = new TableColumnAdjuster(table);
		tca.adjustColumns();
	}

	public static String getSelectedKey(JTable table) {
		int row = table.getSelectedRow();
		if (row >= 0)
			return String.valueOf(table.getModel().getValueAt(row, 0));
		else
			return "";
	}

	public static String selectAndGetSelectedKey(JTable table, String key) {
		for (int i = 0; i < table.getModel().getRowCount(); i++)
			if (String.valueOf(table.getModel().getValueAt(i, 0)).equals(key)) {
				table.setRowSelectionInterval(i, i);
				return key;
			}
		return "";
	}

	public static <E> TableModel getTableModelFromPojos(List<E> pojos, String[] colProperties) {
		TableModel tm;
		if (pojos == null)
			return new DefaultTableModel(colProperties, 0);
		else
			tm = new DefaultTableModel(colProperties, pojos.size());
		for (int i = 0; i < pojos.size(); i++) {
			for (int j = 0; j < colProperties.length; j++) {
				try {
					Object pojo = pojos.get(i);
					Object value = PropertyUtils.getSimpleProperty(pojo, colProperties[j]);
					tm.setValueAt(value, i, j);
				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					throw new UnexpectedException(e);
				}
			}
		}
		return tm;
	}

	public static <E> TableModel getRecordModelFromPojo(E pojo, String[] colProperties) {
		TableModel tm = new DefaultTableModel(new String[] { "", "" }, colProperties.length);
		for (int j = 0; j < colProperties.length; j++) {
			try {
				tm.setValueAt(colProperties[j], j, 0);
				Object value = PropertyUtils.getSimpleProperty(pojo, colProperties[j]);
				tm.setValueAt(value, j, 1);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				throw new UnexpectedException(e);
			}
		}
		return tm;
	}

	public static ComboBoxModel<Object> getComboModelFromList(List<Object[]> lst) {
		DefaultComboBoxModel<Object> cm = new DefaultComboBoxModel<>();
		for (int i = 0; i < lst.size(); i++) {
			Object value = lst.get(i)[0];
			cm.addElement(value);
		}
		return cm;
	}
}

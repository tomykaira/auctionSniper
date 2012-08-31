package auctionsniper.ui;

import javax.swing.table.AbstractTableModel;

import auctionsniper.SniperState;

public class SnipersTableModel extends AbstractTableModel {
	private String statusText = MainWindow.STATUS_JOINING;

	@Override
	public int getColumnCount() { return 1; }
	@Override
	public int getRowCount() { return 1; }
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) { return statusText; }

	public void setStatusText(String newStatusText) {
		statusText = newStatusText;
		fireTableRowsUpdated(0, 0);
	}
	public void sniperStatusChanged(SniperState sniperState,
			String statusBidding) {
		// TODO Auto-generated method stub
		
	}
}
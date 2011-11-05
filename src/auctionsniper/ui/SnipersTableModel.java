package auctionsniper.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import auctionsniper.SniperListener;
import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;

public class SnipersTableModel extends AbstractTableModel implements SniperListener {
	private List<SniperSnapshot> snapshots;

	private static String[] STATUS_TEXT = {
		"Joining", "Bidding", "Winning", "Lost", "Won"
	};

	public SnipersTableModel() {
		snapshots = new ArrayList<SniperSnapshot>();
	}

	@Override
	public int getColumnCount() {
		return Column.values().length;
	}

	@Override
	public int getRowCount() {
		return snapshots.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return Column.at(columnIndex).valueIn(snapshots.get(rowIndex));
	}

	@Override
	public void sniperStateChanged(SniperSnapshot newSnapshot) {
		for (int i=0; i<snapshots.size(); i++) {
			if(snapshots.get(i).itemId.equals(newSnapshot.itemId)) {
				snapshots.set(i, newSnapshot);
				fireTableRowsUpdated(i, i);
			}
		}
	}

	public static String textFor(SniperState state) {
		return STATUS_TEXT[state.ordinal()];
	}

	@Override
	public String getColumnName(int column) {
		return Column.at(column).name;
	}
	public void addSniper(SniperSnapshot joining) {
		snapshots.add(joining);
		fireTableRowsInserted(snapshots.size()-1, snapshots.size()-1);
	}
}
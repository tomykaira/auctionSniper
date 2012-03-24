package auctionsniper.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import auctionsniper.SniperListener;
import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;

import com.objogate.exception.Defect;

public class SnipersTableModel extends AbstractTableModel implements SniperListener {
	private List<SniperSnapshot> sniperSnapshots = new ArrayList<SniperSnapshot>();

	private static String[] STATUS_TEXT = {
		"Joining", "Bidding", "Winning", "Lost", "Won"
	};

	@Override
	public int getColumnCount() {
		return Column.values().length;
	}
	@Override
	public int getRowCount() {
		return sniperSnapshots.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return Column.at(columnIndex).valueIn(sniperSnapshots.get(rowIndex));
	}

	@Override
	public void sniperStateChanged(SniperSnapshot newSnapshot) {
		for (int i=0; i < sniperSnapshots.size(); i++) {
			if (sniperSnapshots.get(i).itemId.equals(newSnapshot.itemId)) {
				sniperSnapshots.set(i, newSnapshot);
				fireTableRowsUpdated(i, i);
				return ;
			}
		}
		throw new Defect("Cannot find match for " + newSnapshot);
	}

	public static String textFor(SniperState state) {
		return STATUS_TEXT[state.ordinal()];
	}

	@Override
	public String getColumnName(int column) {
		return Column.at(column).name;
	}

	public void addSniper(SniperSnapshot snapshot) {
		sniperSnapshots.add(snapshot);
		fireTableRowsInserted(sniperSnapshots.size()-1, sniperSnapshots.size()-1);
	}
}
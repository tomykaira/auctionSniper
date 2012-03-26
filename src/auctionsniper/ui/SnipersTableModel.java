package auctionsniper.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import auctionsniper.AuctionSniper;
import auctionsniper.SniperCollector;
import auctionsniper.SniperListener;
import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;

import com.objogate.exception.Defect;

public class SnipersTableModel extends AbstractTableModel implements SniperListener, SniperCollector {
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
		int row = rowMatching(newSnapshot);
		sniperSnapshots.set(row, newSnapshot);
		fireTableRowsUpdated(row, row);
	}

	private int rowMatching(SniperSnapshot snapshot) {
		for (int i=0; i < sniperSnapshots.size(); i++) {
			if (snapshot.isForSameItemAs(sniperSnapshots.get(i))) {
				return i;
			}
		}
		throw new Defect("Cannot find match for " + snapshot);
	}
	public static String textFor(SniperState state) {
		return STATUS_TEXT[state.ordinal()];
	}

	@Override
	public String getColumnName(int column) {
		return Column.at(column).name;
	}

	@Override
	public void addSniper(AuctionSniper sniper) {
		// TODO Auto-generated method stub

	}

	public void addSniper(SniperSnapshot snapshot) {
		sniperSnapshots.add(snapshot);
		fireTableRowsInserted(sniperSnapshots.size()-1, sniperSnapshots.size()-1);
	}
}
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
	private List<SniperSnapshot> snapshots;

	private List<AuctionSniper>	notToBeGCd;

	private static String[] STATUS_TEXT = {
		"Joining", "Bidding", "Winning", "Lost", "Won"
	};

	public SnipersTableModel() {
		snapshots = new ArrayList<SniperSnapshot>();
		notToBeGCd = new ArrayList<AuctionSniper>();
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
		int row = rowMatching(newSnapshot);
		snapshots.set(row, newSnapshot);
		fireTableRowsUpdated(row, row);
	}

	private int rowMatching(SniperSnapshot newSnapshot) {
		for (int i=0; i<snapshots.size(); i++) {
			if(newSnapshot.isForSameItemAs(snapshots.get(i))) {
				return i;
			}
		}
		throw new Defect("No existing Sniper state for " + newSnapshot.itemId);
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
		notToBeGCd.add(sniper);
		addSniperSnapshot(sniper.getSnapshot());
		sniper.addSniperListener(new SwingThreadSniperListener(this));
	}

	private void addSniperSnapshot(SniperSnapshot snapshot) {
		snapshots.add(snapshot);
		int row = snapshots.size()-1;
		fireTableRowsInserted(row, row);
	}
}
package auctionsniper.ui;

import javax.swing.table.AbstractTableModel;

import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;

public class SnipersTableModel extends AbstractTableModel {
	private static final SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINING);
	private SniperSnapshot sniperSnapshot = STARTING_UP;

	private static String[] STATUS_TEXT = {
		"Joining", "Bidding", "Winning", "Lost", "Won"
	};

	@Override
	public int getColumnCount() {
		return Column.values().length;
	}
	@Override
	public int getRowCount() { return 1; }

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(Column.at(columnIndex)) {
		case ITEM_IDENTIFIER:
			return sniperSnapshot.itemId;
		case LAST_PRICE:
			return sniperSnapshot.lastPrice;
		case LAST_BID:
			return sniperSnapshot.lastBid;
		case SNIPER_STATE:
			return textFor(sniperSnapshot.state);
		default :
			throw new IllegalArgumentException("No column at " + columnIndex);
		}
	}

	public void sniperStateChanged(SniperSnapshot newSnapshot) {
		sniperSnapshot = newSnapshot;
		fireTableRowsUpdated(0, 0);
	}

	public static String textFor(SniperState state) {
		return STATUS_TEXT[state.ordinal()];
	}
}
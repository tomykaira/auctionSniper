package auctionsniper;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import auctionsniper.ui.SnipersTableModel;

public class SniperLauncher implements UserRequestListener {
	private final List<Auction> notToBeGCd = new ArrayList<Auction>();
	private final SnipersTableModel snipers;
	private final AuctionHouse auctionHouse;

	public SniperLauncher(AuctionHouse auctionHouse, SnipersTableModel snipers) {
		this.auctionHouse = auctionHouse;
		this.snipers = snipers;
	}

	@Override
	public void joinAuction(String itemId) {
		snipers.addSniper(SniperSnapshot.joining(itemId));

		Auction auction = auctionHouse.auctionFor(itemId);
		notToBeGCd.add(auction);
		auction.addAuctionEventListener(new AuctionSniper(itemId, auction, new SwingThreadSniperListener(snipers)));
		auction.join();
	}


	public class SwingThreadSniperListener implements SniperListener {
		private SniperListener delegate;

		public SwingThreadSniperListener(SniperListener delegate) {
			this.delegate = delegate;
		}

		@Override
		public void sniperStateChanged(final SniperSnapshot snapshot) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					delegate.sniperStateChanged(snapshot);
				}
			});
		}
	}
}

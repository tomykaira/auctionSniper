package auctionsniper;

import auctionsniper.util.Announcer;


public class AuctionSniper implements AuctionEventListener {
	private Announcer<SniperListener> sniperListeners = Announcer.to(SniperListener.class);
	private Auction auction;
	private SniperSnapshot snapshot;

	public AuctionSniper(String itemId, Auction auction) {
		this.auction = auction;
		this.snapshot = SniperSnapshot.joining(itemId);
	}

	public void addSniperListener(SniperListener listener) {
		this.sniperListeners.addListener(listener);
	}

	@Override
	public void auctionClosed() {
		snapshot = snapshot.closed();
		notifyChange();
	}

	@Override
	public void currentPrice(int price, int increment, PriceSource source) {
		switch(source) {
		case FromSniper:
			snapshot = snapshot.winning(price);
			break;
		case FromOtherBidder:
			int bid = price + increment;
			auction.bid(bid);
			snapshot = snapshot.bidding(price, bid);
			break;
		}
		notifyChange();
	}

	private void notifyChange() {
		sniperListeners.announce().sniperStateChanged(snapshot);
	}

	public SniperSnapshot getSnapshot() {
		return snapshot;
	}
}

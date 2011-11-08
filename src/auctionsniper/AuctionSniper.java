package auctionsniper;

import auctionsniper.UserRequestListener.Item;
import auctionsniper.util.Announcer;


public class AuctionSniper implements AuctionEventListener {
	private Announcer<SniperListener> sniperListeners = Announcer.to(SniperListener.class);
	private Auction auction;
	private SniperSnapshot snapshot;

	public AuctionSniper(Item item, Auction auction) {
		this.auction = auction;
		this.snapshot = SniperSnapshot.joining(item.identifier);
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

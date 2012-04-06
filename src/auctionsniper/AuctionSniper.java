package auctionsniper;


public class AuctionSniper implements AuctionEventListener {
	private SniperListener sniperListener;
	private Auction auction;
	private SniperSnapshot snapshot;
	private final Item item;

	public AuctionSniper(Item item, Auction auction) {
		this.auction = auction;
		this.item = item;
		this.snapshot = SniperSnapshot.joining(item.identifier);
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
			if (item.allowsBid(bid)) {
				auction.bid(bid);
				snapshot = snapshot.bidding(price, bid);
			} else {
				snapshot = snapshot.losing(price);
			}
			break;
		}
		notifyChange();
	}

	@Override
	public void auctionFailed() {
		snapshot = snapshot.failed();
		notifyChange();
	}

	private void notifyChange() {
		sniperListener.sniperStateChanged(snapshot);
	}

	public void addSniperListener(SniperListener listener) {
		this.sniperListener = listener;
	}

	public SniperSnapshot getSnapshot() {
		return snapshot;
	}
}

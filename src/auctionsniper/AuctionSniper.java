package auctionsniper;


public class AuctionSniper implements AuctionEventListener {
	private SniperListener sniperListener;
	private Auction auction;
	private SniperSnapshot snapshot;

	public AuctionSniper(String itemId, Auction auction) {
		this.auction = auction;
		this.snapshot = SniperSnapshot.joining(itemId);
	}

	public void addSniperListener(SniperListener listener) {
		this.sniperListener = listener;
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
		sniperListener.sniperStateChanged(snapshot);
	}

	public SniperSnapshot getSnapshot() {
		return snapshot;
	}
}

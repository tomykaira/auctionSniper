package auctionsniper;


public class AuctionSniper implements AuctionEventListener {

	private SniperListener sniperListener;
	private Auction auction;

	public AuctionSniper(Auction auction, SniperListener sniperListener) {
		this.sniperListener = sniperListener;
		this.auction = auction;
	}

	@Override
	public void auctionClosed() {
		sniperListener.sniperLost();
	}

	@Override
	public void currentPrice(int price, int increment, PriceSource source) {
		switch(source) {
		case FromOtherBidder:
			auction.bid(price + increment);
			sniperListener.sniperBidding();
			break;
		case FromSniper:
			sniperListener.sniperWinning();
			break;
		}
	}
}

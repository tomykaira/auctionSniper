package auctionsniper;


public class AuctionSniper implements AuctionEventListener {

	private SniperListener sniperListener;
	private Auction auction;
	private boolean isWinning = false;

	public AuctionSniper(Auction auction, SniperListener sniperListener) {
		this.sniperListener = sniperListener;
		this.auction = auction;
	}

	@Override
	public void auctionClosed() {
		if (isWinning) {
			sniperListener.sniperWon();
		} else {
			sniperListener.sniperLost();
		}
	}

	@Override
	public void currentPrice(int price, int increment, PriceSource source) {
		isWinning = source == PriceSource.FromSniper;
		if (isWinning) {
			sniperListener.sniperWinning();
		} else {
			auction.bid(price + increment);
			sniperListener.sniperBidding();
		}
	}
}

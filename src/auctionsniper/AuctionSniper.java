package auctionsniper;


public class AuctionSniper implements AuctionEventListener {

	private SniperListener sniperListener;

	public AuctionSniper(SniperListener sniperListener) {
		this.sniperListener = sniperListener;
	}

	@Override
	public void auctionClosed() {
		sniperListener.sniperLost();
	}

	@Override
	public void currentPrice(int price, int increment) {
		// TODO Auto-generated method stub
		
	}
}

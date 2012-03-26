package auctionsniper;


public class SniperLauncher implements UserRequestListener {
	private final AuctionHouse auctionHouse;
	private final SniperCollector collector;

	public SniperLauncher(AuctionHouse auctionHouse, SniperCollector snipers) {
		this.auctionHouse = auctionHouse;
		this.collector = snipers;
	}

	@Override
	public void joinAuction(String itemId) {
		Auction auction = auctionHouse.auctionFor(itemId);
		AuctionSniper sniper = new AuctionSniper(itemId, auction);
		auction.addAuctionEventListener(sniper);
		collector.addSniper(sniper);
		auction.join();
	}
}

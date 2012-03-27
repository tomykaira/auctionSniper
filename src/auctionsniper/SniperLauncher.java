package auctionsniper;


public class SniperLauncher implements UserRequestListener {
	private final AuctionHouse auctionHouse;
	private final SniperCollector collector;

	public SniperLauncher(AuctionHouse auctionHouse, SniperCollector snipers) {
		this.auctionHouse = auctionHouse;
		this.collector = snipers;
	}

	@Override
	public void joinAuction(Item item) {
		Auction auction = auctionHouse.auctionFor(item.identifier);
		AuctionSniper sniper = new AuctionSniper(item, auction);
		auction.addAuctionEventListener(sniper);
		collector.addSniper(sniper);
		auction.join();
	}
}

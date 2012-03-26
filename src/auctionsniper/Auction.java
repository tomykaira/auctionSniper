package auctionsniper;

public interface Auction {

	void bid(int i);

	void join();

	void addAuctionEventListener(AuctionEventListener listener);
}

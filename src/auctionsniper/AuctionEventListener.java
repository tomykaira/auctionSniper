package auctionsniper;

import java.util.EventListener;

public interface AuctionEventListener extends EventListener{

	void auctionClosed();

	void currentPrice(int price, int increment);

}

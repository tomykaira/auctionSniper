package auctionsniper;

import java.util.EventListener;

public class SniperPortfolio {
	public interface PortfolioListener extends EventListener {
		void sniperAdded(AuctionSniper sniper);
	}
}

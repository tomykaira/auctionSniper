package auctionsniper;

import java.util.EventListener;

import auctionsniper.util.Announcer;

public class SniperPortfolio implements SniperCollector {
	public interface PortfolioListener extends EventListener {
		void sniperAdded(AuctionSniper sniper);
	}

	private Announcer<PortfolioListener> listeners = Announcer.to(PortfolioListener.class);

	public void addPortfolioListener(PortfolioListener listener) {
		listeners.addListener(listener);
	}

	@Override
	public void addSniper(AuctionSniper sniper) {
		listeners.announce().sniperAdded(sniper);
	}
}

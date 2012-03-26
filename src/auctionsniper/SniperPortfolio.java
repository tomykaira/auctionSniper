package auctionsniper;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import auctionsniper.util.Announcer;

public class SniperPortfolio implements SniperCollector {
	private Announcer<PortfolioListener> listeners = Announcer.to(PortfolioListener.class);
	private final List<AuctionSniper> snipers = new ArrayList<AuctionSniper>();

	public interface PortfolioListener extends EventListener {
		void sniperAdded(AuctionSniper sniper);
	}

	public void addPortfolioListener(PortfolioListener listener) {
		listeners.addListener(listener);
	}

	@Override
	public void addSniper(AuctionSniper sniper) {
		snipers.add(sniper);
		listeners.announce().sniperAdded(sniper);
	}
}

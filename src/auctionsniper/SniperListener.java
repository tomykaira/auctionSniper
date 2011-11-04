package auctionsniper;

import java.util.EventListener;

public interface SniperListener extends EventListener {
	void sniperLost();

	void sniperBidding(SniperSnapshot sniperState);

	void sniperWinning();

	void sniperWon();

	void sniperJoining(SniperSnapshot sniperState);
}

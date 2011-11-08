package auctionsniper;

import java.util.ArrayList;

import auctionsniper.ui.SnipersTableModel;
import auctionsniper.xmpp.XMPPAuctionHouse;

public class SniperLauncher implements UserRequestListener{

	private final XMPPAuctionHouse	auctionHouse;
	private final SnipersTableModel	snipers;
	private final ArrayList<Auction>	notToBeGCd = new ArrayList<Auction>();
	private final SniperCollector collector;

	public SniperLauncher(XMPPAuctionHouse auctionHouse, SnipersTableModel snipers) {
		this.auctionHouse = auctionHouse;
		this.snipers = snipers;
		this.collector = new SnipersTableModel();
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

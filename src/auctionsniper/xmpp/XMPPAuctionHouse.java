package auctionsniper.xmpp;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import auctionsniper.Auction;
import auctionsniper.AuctionHouse;
import auctionsniper.UserRequestListener.Item;

public class XMPPAuctionHouse implements AuctionHouse {
	private static final String AUCTION_RESOURCE = "Auction";

	private XMPPConnection	connection;

	public XMPPAuctionHouse(XMPPConnection connection) {
		this.connection = connection;
	}

	@Override
	public Auction auctionFor(Item item) {
		return new XMPPAuction(connection, item);
	}

	public static XMPPAuctionHouse connect(String hostname, String username, String password) throws XMPPException {
		XMPPConnection connection = new XMPPConnection(hostname);
		connection.connect();
		connection.login(username, password, AUCTION_RESOURCE);

		return new XMPPAuctionHouse(connection);
	}

	public void disconnect() {
		this.connection.disconnect();
	}

}

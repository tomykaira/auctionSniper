package auctionsniper.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import auctionsniper.Auction;
import auctionsniper.AuctionEventListener;
import auctionsniper.util.Announcer;

public final class XMPPAuction implements Auction {
	private final Announcer<AuctionEventListener> auctionEventListeners =
			Announcer.to(AuctionEventListener.class);
	private final Chat chat;
	private static final String ITEM_ID_AS_LOGIN = "auction-%s";
	private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + XMPPAuctionHouse.AUCTION_RESOURCE;
	public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
	public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: Bid; Price: %d;";

	public XMPPAuction(XMPPConnection connection, String itemId) {
		AuctionMessageTranslator translator = translatorFor(connection);
		chat = connection.getChatManager().
				createChat(auctionId(itemId, connection),translator);
		addAuctionEventListener(chatDisconnectorFor(translator));
	}

	private AuctionEventListener chatDisconnectorFor(
			final AuctionMessageTranslator translator) {
		return new AuctionEventListener() {

			@Override
			public void auctionClosed() {}

			@Override
			public void currentPrice(int price, int increment, PriceSource source) {}

			@Override
			public void auctionFailed() {
				chat.removeMessageListener(translator);
			}

		};
	}

	private AuctionMessageTranslator translatorFor(XMPPConnection connection) {
		return new AuctionMessageTranslator(
				connection.getUser(),
				auctionEventListeners.announce(),
				new XMPPFailureReporter() {
					@Override
					public void cannotTranslateMessage(String auctionId,
							String failedMessage, Exception exception) {}});
	}

	@Override
	public void bid(int amount) {
		sendMessage(String.format(BID_COMMAND_FORMAT, amount));
	}

	@Override
	public void join() {
		sendMessage(JOIN_COMMAND_FORMAT);
	}

	@Override
	public void addAuctionEventListener(AuctionEventListener listener) {
		auctionEventListeners.addListener(listener);
	}

	private void sendMessage(final String message) {
		try {
			chat.sendMessage(message);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String auctionId(String itemId, XMPPConnection connection) {
		return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
	}
};

package auctionsniper.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import auctionsniper.Auction;
import auctionsniper.AuctionEventListener;
import auctionsniper.util.Announcer;

public class XMPPAuction implements Auction {
	private final Announcer<AuctionEventListener> auctionEventListeners = Announcer.to(AuctionEventListener.class);
	private final Chat chat;
	public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
	public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: Bid; Price: %d;";

	public XMPPAuction(XMPPConnection connection, String auctionJID, XMPPFailureReporter failureReporter) {
		AuctionMessageTranslator translator = translatorFor(connection, failureReporter);
		this.chat = connection.getChatManager().createChat(auctionJID, translator);
		addAuctionEventListener(chatDisconnectorFor(translator));
	}

	private AuctionMessageTranslator translatorFor(XMPPConnection connection, XMPPFailureReporter failureReporter) {
		return new AuctionMessageTranslator(connection.getUser(), auctionEventListeners.announce(), failureReporter);
	}

	@Override
	public void bid(int amount) {
		sendMessage(String.format(BID_COMMAND_FORMAT, amount));
	}

	@Override
	public void join() {
		sendMessage(JOIN_COMMAND_FORMAT);
	}

	private void sendMessage(final String message) {
		try {
			chat.sendMessage(message);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void addAuctionEventListener(AuctionEventListener listener) {
		auctionEventListeners.addListener(listener);
	}

	private AuctionEventListener chatDisconnectorFor(final AuctionMessageTranslator translator) {
		return new AuctionEventListener() {

			@Override
			public void currentPrice(int price, int increment, PriceSource source) {
				// do nothing
			}

			@Override
			public void auctionFailed() {
				chat.removeMessageListener(translator);
			}

			@Override
			public void auctionClosed() {
				// do nothing
			}
		};
	}

}

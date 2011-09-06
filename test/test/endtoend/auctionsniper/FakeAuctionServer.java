package test.endtoend.auctionsniper;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.hamcrest.Matcher;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import auctionsniper.Main;

public class FakeAuctionServer {
	public static final String ITEM_ID_AS_LOGIN = "auction-%s";
	public static final String AUCTION_RESOURCE = "Auction";
	public static final String XMPP_HOSTNAME = "tomita-macpro";
	private static final String AUCTION_PASSWORD = "auction";

	private final String itemId;
	private final XMPPConnection connection;
	private Chat currentChat;
	private final SingleMessageListener messageListener = new SingleMessageListener();

	public FakeAuctionServer(String itemId) {
		this.itemId = itemId;
		this.connection = new XMPPConnection(XMPP_HOSTNAME);
	}

	public void startSellingItem() throws XMPPException {
		connection.connect();
		connection.login(String.format(ITEM_ID_AS_LOGIN, getItemId()), AUCTION_PASSWORD, AUCTION_RESOURCE);
		connection.getChatManager().addChatListener(
				new ChatManagerListener() {
					@Override
					public void chatCreated(Chat chat, boolean createdLocally) {
						currentChat = chat;
						chat.addMessageListener(messageListener);
					}
				});
	}

	public String getItemId() {
		return itemId;
	}

	public void hasReceivedJoinRequestFrom(String sniperId) throws InterruptedException {
		receivesAMessageMatching(sniperId, equalTo(Main.JOIN_COMMAND_FORMAT));
	}

	public void announceClosed() throws XMPPException {
		currentChat.sendMessage(new Message());
	}

	public void stop() {
		connection.disconnect();
	}

	public class SingleMessageListener implements MessageListener {
		private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(1);

		@Override
		public void processMessage(Chat chat, Message message) {
			messages.add(message);
		}

		public void receiveAMessage(Matcher<? super String> messageMatcher) throws InterruptedException {
			final Message message = messages.poll(5, TimeUnit.SECONDS);
			assertThat("Message", message, is(notNullValue()));
			assertThat(message.getBody(), messageMatcher);
		}

	}

	public void reportPrice(int price, int increment, String bidder) throws XMPPException {
		currentChat.sendMessage(String.format("SOLVersion: 1.1; Event: PRICE; "+
				"CurrentPrice: $d; Increemnt: %d; Bidder: %s;",
				price, increment, bidder));
	}

	public void hasReceivedBid(int bid, String sniperId) throws InterruptedException {
		receivesAMessageMatching(sniperId, equalTo(String.format(Main.BID_COMMAND_FORMAT, bid)));
	}

	private void receivesAMessageMatching(String sniperId,
			Matcher<? super String> messageMatcher) throws InterruptedException {
		messageListener.receiveAMessage(messageMatcher);
		assertThat(currentChat.getParticipant(), equalTo(sniperId));

	}

}


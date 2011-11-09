package test.unit.auctionsniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.endtoend.auctionsniper.ApplicationRunner;
import auctionsniper.AuctionEventListener;
import auctionsniper.AuctionEventListener.PriceSource;
import auctionsniper.xmpp.AuctionMessageTranslator;
import auctionsniper.xmpp.XMPPFailureReporter;

@RunWith(JMock.class)
public class AuctionMessageTranslatorTest {
	public static final Chat UNUSED_CHAT = null;
	private final Mockery context = new Mockery();
	private final AuctionEventListener listener = context.mock(AuctionEventListener.class);
	private final XMPPFailureReporter	failureReporter = context.mock(XMPPFailureReporter.class);
	private final AuctionMessageTranslator translator =
			new AuctionMessageTranslator(ApplicationRunner.SNIPER_ID, listener, failureReporter);

	@Test public void
	notifiesAuctionClosedWhenCloseMessageReceived() {
		context.checking(new Expectations() {{
			oneOf(listener).auctionClosed();
		}});
		translator.processMessage(UNUSED_CHAT, message("SOLVersion: 1.1; Event: CLOSE;"));
	}

	@Test public void
	notifiesBidDtailsWhenCurrentPriceMessageReceivedFromOtherBidder() {
		context.checking(new Expectations() {{
			exactly(1).of(listener).currentPrice(192, 7, PriceSource.FromOtherBidder);
		}});

		translator.processMessage(UNUSED_CHAT,
				message("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;"));
	}

	@Test public void
	notifiesBidDtailsWhenCurrentPriceMessageReceivedFromSniper() {
		context.checking(new Expectations() {{
			exactly(1).of(listener).currentPrice(234, 5, PriceSource.FromSniper);
		}});

		translator.processMessage(UNUSED_CHAT,
				message("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 234; Increment: 5; Bidder: "+ ApplicationRunner.SNIPER_ID +";"));
	}

	@Test public void
	notifiesAuctionFailedWhenBadMessageReceived() {
		String badMessage = "a bad message";
		expectFailureWithMessage(badMessage);

		translator.processMessage(UNUSED_CHAT, message(badMessage));
	}

	@Test public void
	notifiesAuctionFailedWhenEventTypeMissing() {
		String badMessage = "SOLVersion: 1.1; CurrentPrice: 192; Increment: 7; Bidder: Someone else;";
		expectFailureWithMessage(badMessage);

		translator.processMessage(UNUSED_CHAT, message(badMessage));
	}

	private void expectFailureWithMessage(final String badMessage) {
		context.checking(new Expectations() {
		{
			oneOf(listener).auctionFailed();
			oneOf(failureReporter).cannotTranslateMessage(with(ApplicationRunner.SNIPER_ID), with(badMessage), with(any(Exception.class)));
		}});
	}

	private Message message(String body) {
		Message message = new Message();
		message.setBody(body);
		return message;
	}

}

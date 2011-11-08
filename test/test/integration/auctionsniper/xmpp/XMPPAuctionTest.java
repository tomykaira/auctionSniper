package test.integration.auctionsniper.xmpp;

import static org.junit.Assert.*;
import static test.endtoend.auctionsniper.FakeAuctionServer.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.endtoend.auctionsniper.ApplicationRunner;
import test.endtoend.auctionsniper.FakeAuctionServer;
import auctionsniper.Auction;
import auctionsniper.AuctionEventListener;
import auctionsniper.UserRequestListener.Item;
import auctionsniper.xmpp.XMPPAuction;

public class XMPPAuctionTest {

	private XMPPConnection	connection;
	private final FakeAuctionServer auctionServer = new FakeAuctionServer("item-54321");

	@Before public void
	openConnection() throws XMPPException {
		connection = new XMPPConnection(XMPP_HOSTNAME);
		connection.connect();
		connection.login(ApplicationRunner.SNIPER_ID, ApplicationRunner.SNIPER_PASSWORD, AUCTION_RESOURCE);
	}
	@After public void
	closeConnection() {
		connection.disconnect();
	}

  @Before public void startAuction() throws XMPPException {
    auctionServer.startSellingItem();
  }
  @After public void stopAuction() {
    auctionServer.stop();
  }


	@Test public void
	receivesEventsFromAuctionServerAfterJoining() throws Exception {
		CountDownLatch auctionWasClosed = new CountDownLatch(1);

		Auction auction = new XMPPAuction(connection, new Item(auctionServer.getItemId(), 10));
		auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));

		auction.join();
		auctionServer.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
		auctionServer.announceClosed();

		assertTrue("should have been closed", auctionWasClosed.await(2, TimeUnit.SECONDS));
	}

	private AuctionEventListener auctionClosedListener(final CountDownLatch auctionWasClosed) {
		return new AuctionEventListener() {

			@Override
			public void currentPrice(int price, int increment, PriceSource source) {
				// not implemented
			}

			@Override
			public void auctionClosed() {
				auctionWasClosed.countDown();
			}
		};
	}
}

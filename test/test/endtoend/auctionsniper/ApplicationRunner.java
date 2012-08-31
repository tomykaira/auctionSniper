package test.endtoend.auctionsniper;

import static test.endtoend.auctionsniper.FakeAuctionServer.*;
import auctionsniper.Main;
import auctionsniper.SniperState;
import auctionsniper.ui.SnipersTableModel;

public class ApplicationRunner {
	public static final String SNIPER_ID = "sniper";
	public static final String SNIPER_PASSWORD = "sniper";
	public static final String SNIPER_XMPP_ID = SNIPER_ID + "@" + XMPP_HOSTNAME + "/Auction";
	private AuctionSniperDriver driver;
	private String itemId;

	// trigger an event to drive the test
	public void startBiddingIn(final FakeAuctionServer auction) {
		itemId = auction.getItemId();
		Thread thread = new Thread("Test Application") {
			@Override public void run() {
				try {
					Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
		driver = new AuctionSniperDriver(1000);
		driver.showsSniperStatus(SnipersTableModel.textFor(SniperState.JOINING)); //test case
	}

	public void showsSniperHasLostAcution() {
		driver.showsSniperStatus(SnipersTableModel.textFor(SniperState.LOST)); // test case
	}

	public void showsSniperHasWonAcution() {
		driver.showsSniperStatus(SnipersTableModel.textFor(SniperState.WON)); // test case
	}

	public void stop() {
		if (driver != null) {
			driver.dispose();
		}
	}

	public void hasShownSniperIsBidding() {
		driver.showsSniperStatus(SnipersTableModel.textFor(SniperState.BIDDING)); // test case
	}

	public void hasShownSniperIsWinning() {
		driver.showsSniperStatus(SnipersTableModel.textFor(SniperState.WINNING)); // test case
	}

	public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
		driver.showsSniperStatus(itemId, lastPrice, lastBid, SnipersTableModel.textFor(SniperState.BIDDING));
	}

	public void hasShownSniperIsWinning(int lastPrice) {
		driver.showsSniperStatus(itemId, lastPrice, lastPrice, SnipersTableModel.textFor(SniperState.WINNING));
	}

	public void showsSniperHasWonAcution(int lastPrice) {
		driver.showsSniperStatus(itemId, lastPrice, lastPrice, SnipersTableModel.textFor(SniperState.WON));
	}
}

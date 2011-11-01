package test.endtoend.auctionsniper;

import static test.endtoend.auctionsniper.FakeAuctionServer.*;
import auctionsniper.Main;
import auctionsniper.ui.MainWindow;

public class ApplicationRunner {
	public static final String SNIPER_ID = "sniper";
	public static final String SNIPER_PASSWORD = "sniper";
	public static final String SNIPER_XMPP_ID = SNIPER_ID + "@" + XMPP_HOSTNAME + "/Auction";
	private AuctionSniperDriver driver;

	// trigger an event to drive the test
	public void startBiddingIn(final FakeAuctionServer auction) {
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
		driver.showsSniperStatus(MainWindow.STATUS_JOINING); //test case
	}

	public void showsSniperHasLostAcution() {
		driver.showsSniperStatus(MainWindow.STATUS_LOST); // test case
	}

	public void showsSniperHasWonAcution() {
		driver.showsSniperStatus(MainWindow.STATUS_WON); // test case
	}

	public void stop() {
		if (driver != null) {
			driver.dispose();
		}
	}

	public void hasShownSniperIsBidding() {
		driver.showsSniperStatus(MainWindow.STATUS_BIDDING); // test case
	}

	public void hasShownSniperIsWinning() {
		driver.showsSniperStatus(MainWindow.STATUS_WINNING); // test case
	}
}

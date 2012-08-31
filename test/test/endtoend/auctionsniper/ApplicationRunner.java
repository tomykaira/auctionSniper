package test.endtoend.auctionsniper;

import static auctionsniper.Main.*;
import static test.endtoend.auctionsniper.FakeAuctionServer.*;
import auctionsniper.Main;

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
		driver.showsSniperStatus(STATUS_JOINING); //test case
	}

	public void showsSniperHasLotAcution() {
		driver.showsSniperStatus(STATUS_LOST); // test case
	}

	public void stop() {
		if (driver != null) {
			driver.dispose();
		}
	}

	public void hasShownSniperIsBidding() {
		// TODO Auto-generated method stub

	}
}

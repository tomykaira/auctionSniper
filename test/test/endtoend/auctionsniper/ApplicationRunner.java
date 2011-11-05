package test.endtoend.auctionsniper;

import static test.endtoend.auctionsniper.FakeAuctionServer.*;
import auctionsniper.Main;
import auctionsniper.SniperState;
import auctionsniper.ui.MainWindow;
import auctionsniper.ui.SnipersTableModel;

public class ApplicationRunner {
	public static final String SNIPER_ID = "sniper";
	public static final String SNIPER_PASSWORD = "sniper";
	public static final String SNIPER_XMPP_ID = SNIPER_ID + "@" + XMPP_HOSTNAME + "/Auction";
	private AuctionSniperDriver driver;

	// trigger an event to drive the test
	public void startBiddingIn(final FakeAuctionServer... auctions) {
		startSniper();
		for (FakeAuctionServer auction : auctions) {
			final String itemId = auction.getItemId();
			driver.startBiddingFor(itemId);
			driver.showsSniperStatus(auction.getItemId(), 0, 0, SnipersTableModel.textFor(SniperState.JOINING));
		}
	}

	private void startSniper() {
		Thread thread = new Thread("Test Application") {
			@Override public void run() {
				try {
					Main.main(arguments());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.setDaemon(true);
		thread.start();

		driver = new AuctionSniperDriver(1000);
		driver.hasTitle(MainWindow.APPLICATION_TITLE);
		driver.hasColumnTitles();
	}

	protected static String[] arguments(FakeAuctionServer... auctions) {
		String[] arguments = new String[auctions.length + 3];
		arguments[0] = XMPP_HOSTNAME;
		arguments[1] = SNIPER_ID;
		arguments[2] = SNIPER_PASSWORD;
		for (int i = 0; i < auctions.length; i++) {
			arguments[i+3] = auctions[i].getItemId();
		}
		return arguments;
	}

	public void showsSniperHasLostAcution(int lastPrice, int lastBid) {
		driver.showsSniperStatus(SnipersTableModel.textFor(SniperState.LOST)); // test case
	}

	public void showsSniperHasWonAcution(FakeAuctionServer auction, int lastPrice) {
		driver.showsSniperStatus(auction.getItemId(), lastPrice, lastPrice,
				SnipersTableModel.textFor(SniperState.WON)); // test case
	}

	public void stop() {
		if (driver != null) {
			driver.dispose();
		}
	}

	public void hasShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid) {
		driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid,
				SnipersTableModel.textFor(SniperState.BIDDING)); // test case
	}

	public void hasShownSniperIsWinning(FakeAuctionServer auction, int winningBid) {
		driver.showsSniperStatus(auction.getItemId(), winningBid, winningBid,
				SnipersTableModel.textFor(SniperState.WINNING)); // test case
	}
}

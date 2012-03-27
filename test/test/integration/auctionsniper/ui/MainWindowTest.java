package test.integration.auctionsniper.ui;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import test.endtoend.auctionsniper.AuctionSniperDriver;
import auctionsniper.Item;
import auctionsniper.SniperPortfolio;
import auctionsniper.UserRequestListener;
import auctionsniper.ui.MainWindow;

import com.objogate.wl.swing.probe.ValueMatcherProbe;

public class MainWindowTest {
	private final SniperPortfolio portfolio = new SniperPortfolio();
	private final MainWindow mainWindow = new MainWindow(portfolio);
	private final AuctionSniperDriver driver = new AuctionSniperDriver(100);

	@Test public void
	makeUserRequestWhenJoinButtonClicked() {
		final ValueMatcherProbe<Item> itemProbe =
				new ValueMatcherProbe<Item>(equalTo(new Item("an item-id", 789)), "join request");
		mainWindow.addUserRequestListener(new UserRequestListener() {

			@Override
			public void joinAuction(Item item) {
				itemProbe.setReceivedValue(item);
			}
		});
		driver.startBiddingFor("an item-id", 789);
		driver.check(itemProbe);
	}
}

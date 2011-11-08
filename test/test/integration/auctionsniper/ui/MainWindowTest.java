package test.integration.auctionsniper.ui;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import test.endtoend.auctionsniper.AuctionSniperDriver;
import auctionsniper.SniperPortfolio;
import auctionsniper.UserRequestListener;
import auctionsniper.UserRequestListener.Item;
import auctionsniper.ui.MainWindow;

import com.objogate.wl.swing.probe.ValueMatcherProbe;

public class MainWindowTest {
	private final MainWindow mainWindow = new MainWindow(new SniperPortfolio());
	private final AuctionSniperDriver driver = new AuctionSniperDriver(1000);

	@Test public void
	makeUserRequestWhenJoinButtonClicked() {
		final ValueMatcherProbe<Item> itemProbe = new ValueMatcherProbe<Item>(equalTo(new Item("item-id", 789)), "item request");
		mainWindow.addUserRequestListener(new UserRequestListener() {
			@Override
			public void joinAuction(Item item) {
				itemProbe.setReceivedValue(item);
			}
		});
		driver.startBiddingFor("item-id", 789);
		driver.check(itemProbe);
	}
}

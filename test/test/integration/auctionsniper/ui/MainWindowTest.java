package test.integration.auctionsniper.ui;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import test.endtoend.auctionsniper.AuctionSniperDriver;
import auctionsniper.SniperPortfolio;
import auctionsniper.UserRequestListener;
import auctionsniper.ui.MainWindow;

import com.objogate.wl.swing.probe.ValueMatcherProbe;

public class MainWindowTest {
	private final MainWindow mainWindow = new MainWindow(new SniperPortfolio());
	private final AuctionSniperDriver driver = new AuctionSniperDriver(1000);

	@Test public void
	makeUserRequestWhenJoinButtonClicked() {
		final ValueMatcherProbe<String> buttonProbe = new ValueMatcherProbe<String>(equalTo("item-id"), "join request");
		mainWindow.addUserRequestListener(new UserRequestListener() {
			@Override
			public void joinAuction(String itemId) {
				buttonProbe.setReceivedValue(itemId);
			}
		});
		driver.startBiddingFor("item-id");
		driver.check(buttonProbe);
	}
}

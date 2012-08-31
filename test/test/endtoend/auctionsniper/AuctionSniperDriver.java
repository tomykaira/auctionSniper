package test.endtoend.auctionsniper;

import static com.objogate.wl.swing.matcher.JLabelTextMatcher.*;
import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.*;
import static org.hamcrest.CoreMatchers.*;
import static java.lang.String.valueOf;
import auctionsniper.ui.MainWindow;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JTableDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

public class AuctionSniperDriver extends JFrameDriver {
	public AuctionSniperDriver(int timeoutMillis) {
		super(new GesturePerformer(),
				JFrameDriver.topLevelFrame(named(MainWindow.MAIN_WINDOW_NAME), showingOnScreen()),
					new AWTEventQueueProber(timeoutMillis, 100));
	}

	public void showsSniperStatus(String statusText) {
		new JTableDriver(this).hasCell(withLabelText(equalTo(statusText)));
	}

	public void showsSniperStatus(String itemId, int lastPrice, int lastBid,
			String statusText) {
		JTableDriver table = new JTableDriver(this);
		table.hasRow(
				matching(withLabelText(itemId), withLabelText(valueOf(lastPrice)),
						withLabelText(valueOf(lastBid)), withLabelText(statusText)));
	}
}

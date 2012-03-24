package test.endtoend.auctionsniper;

import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.*;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.*;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;

import auctionsniper.ui.MainWindow;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JButtonDriver;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JTableDriver;
import com.objogate.wl.swing.driver.JTableHeaderDriver;
import com.objogate.wl.swing.driver.JTextFieldDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

public class AuctionSniperDriver extends JFrameDriver {
	public AuctionSniperDriver(int timeoutMillis) {
		super(new GesturePerformer(),
				JFrameDriver.topLevelFrame(named(MainWindow.MAIN_WINDOW_NAME), showingOnScreen()),
					new AWTEventQueueProber(timeoutMillis, 100));
	}

	public void showsSniperStatus(String statusText) {
		new JTableDriver(this).hasCell(withLabelText(statusText));
	}

	public void showsSniperStatus(String itemId, int lastPrice, int lastBid, String statusText) {
		JTableDriver table = new JTableDriver(this);
		table.hasRow(matching(withLabelText(itemId), withLabelText(String.valueOf(lastPrice)),
				withLabelText(String.valueOf(lastBid)), withLabelText(statusText)));
	}

	public void hasColumnTitles() {
		JTableHeaderDriver headers = new JTableHeaderDriver(this, JTableHeader.class);
		headers.hasHeaders(matching(withLabelText("Item"), withLabelText("Last Price"),
				withLabelText("Last Bid"), withLabelText("State")));
	}

	public void startBiddingFor(String itemId) {
		itemIdField().replaceAllText(itemId);
		bidButton().click();
	}

	private JTextFieldDriver itemIdField() {
		JTextFieldDriver newItemId = new JTextFieldDriver(this, JTextField.class, named(MainWindow.NEW_ITEM_ID_NAME));
		newItemId.focusWithMouse();
		return newItemId;
	}

	private JButtonDriver bidButton() {
		return new JButtonDriver(this, JButton.class, named(MainWindow.JOIN_BUTTON_NAME));
	}
}

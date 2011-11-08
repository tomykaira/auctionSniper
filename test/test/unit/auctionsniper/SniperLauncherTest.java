package test.unit.auctionsniper;

import static org.hamcrest.CoreMatchers.*;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.junit.Test;

import auctionsniper.Auction;
import auctionsniper.AuctionHouse;
import auctionsniper.AuctionSniper;
import auctionsniper.SniperCollector;
import auctionsniper.SniperLauncher;
import auctionsniper.UserRequestListener.Item;

public class SniperLauncherTest {

	private final Mockery context = new Mockery();
	private final Auction auction = context.mock(Auction.class);
	private final AuctionHouse auctionHouse = context.mock(AuctionHouse.class);
	private final SniperCollector collector = context.mock(SniperCollector.class);
	private final SniperLauncher launcher = new SniperLauncher(auctionHouse, collector);
	private final States auctionState = context.states("auction state").startsAs("not joined");

	@Test public void
	addsNewSniperToCollectorAndThenJoinsAuction() {
		final Item item = new Item("item 123", 456);

		context.checking(new Expectations() {{
			allowing(auctionHouse).auctionFor(item);
				will(returnValue(auction));
			oneOf(auction).addAuctionEventListener(with(sniperForItem(item.identifier)));
				when(auctionState.is("not joined"));
			oneOf(collector).addSniper(with(sniperForItem(item.identifier)));
				when(auctionState.is("not joined"));
			one(auction).join();
				then(auctionState.is("joined"));
		}});

		launcher.joinAuction(item);
	}

	protected Matcher<AuctionSniper> sniperForItem(String itemId) {
		return new FeatureMatcher<AuctionSniper, String>(equalTo(itemId), "sniper that is ", "was") {
			@Override
			protected String featureValueOf(AuctionSniper actual) {
				return actual.getSnapshot().itemId;
			}
		};
	}
}

package auctionsniper.ui;

import javax.swing.SwingUtilities;

import auctionsniper.SniperListener;
import auctionsniper.SniperSnapshot;

public class SwingThreadSniperListener implements SniperListener {
	private SniperListener delegate;

	public SwingThreadSniperListener(SniperListener delegate) {
		this.delegate = delegate;
	}

	@Override
	public void sniperStateChanged(final SniperSnapshot snapshot) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				delegate.sniperStateChanged(snapshot);
			}
		});
	}
}

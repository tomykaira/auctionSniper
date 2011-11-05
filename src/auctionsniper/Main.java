package auctionsniper;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import auctionsniper.ui.MainWindow;
import auctionsniper.ui.SnipersTableModel;
import auctionsniper.xmpp.AuctionMessageTranslator;

public class Main {

	private static final int ARG_HOSTNAME = 0;
	private static final int ARG_USERNAME = 1;
	private static final int ARG_PASSWORD = 2;
	private static final int ARG_ITEM_ID  = 3;
	private static final String AUCTION_RESOURCE = "Auction";
	private static final String ITEM_ID_AS_LOGIN = "auction-%s";
	private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;
	public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
	public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: Bid; Price: %d;";

	private final SnipersTableModel snipers = new SnipersTableModel();

	private MainWindow ui;
	private List<Chat> notToBeGCd;

	public Main() throws Exception {
		startUserInterface();
		notToBeGCd = new ArrayList<Chat>();
	}


	private void startUserInterface() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				ui = new MainWindow(snipers);
			}
		});

	}


	public static void main(String ...args) throws Exception {
		Main main = new Main();
		XMPPConnection connection = connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
		main.disconnectWhenUICloses(connection);
		for (int i = 3; i < args.length; i++) {
			main.joinAuction(connection, args[i]);
		}
	}


	private void joinAuction(XMPPConnection connection, String itemId) throws XMPPException {
		final Chat chat = connection.getChatManager().createChat(auctionId(itemId, connection), null);
		notToBeGCd.add(chat);

		Auction auction = new XMPPAuction(chat);
		chat.addMessageListener(
				new AuctionMessageTranslator(
						connection.getUser(),
						new AuctionSniper(itemId, auction, new SwingThreadSniperListener(snipers))));
		auction.join();
	}


	private void disconnectWhenUICloses(final XMPPConnection connection) {
		ui.addWindowListener(new WindowAdapter() {
			@Override public void windowClosed(WindowEvent e) {
				connection.disconnect();
			}
		});

	}


	private static String auctionId(String itemId, XMPPConnection connection) {
		return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
	}


	private static XMPPConnection connection(String hostname, String username, String password) throws XMPPException {
		XMPPConnection connection = new XMPPConnection(hostname);
		connection.connect();
		connection.login(username, password, AUCTION_RESOURCE);

		return connection;
	}


	public static class XMPPAuction implements Auction {
		private final Chat chat;

		public XMPPAuction(Chat chat) {
			this.chat = chat;
		}

		@Override
		public void bid(int amount) {
			sendMessage(String.format(BID_COMMAND_FORMAT, amount));
		}

		@Override
		public void join() {
			sendMessage(JOIN_COMMAND_FORMAT);
		}

		private void sendMessage(final String message) {
			try {
				chat.sendMessage(message);
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};


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

}

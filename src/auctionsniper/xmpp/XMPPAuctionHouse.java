package auctionsniper.xmpp;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.io.FilenameUtils;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import auctionsniper.Auction;
import auctionsniper.AuctionHouse;

public class XMPPAuctionHouse implements AuctionHouse {
	public static final String LOG_FILE_NAME = "auction-sniper.log";
	static final String AUCTION_RESOURCE = "Auction";

	private static final String LOGGER_NAME = "auction-sniper";

	private final XMPPConnection	connection;
	private final XMPPFailureReporter failureReporter;

	public XMPPAuctionHouse(XMPPConnection connection) {
		this.connection = connection;
		this.failureReporter = new LoggingXMPPFailureReporter(makeLogger());
	}

	private Logger makeLogger() {
		Logger logger = Logger.getLogger(LOGGER_NAME);
		logger.setUseParentHandlers(false);
		logger.addHandler(simpleFileHandler());
		return logger;
	}

	private Handler simpleFileHandler() {
		try {
			FileHandler handler = new FileHandler(LOG_FILE_NAME);
			handler.setFormatter(new SimpleFormatter());
			return handler;
		} catch (Exception e) {
			throw new XMPPAuctionException("Could not create logger FileHandler "
					+ FilenameUtils.getFullPath(LOG_FILE_NAME), e);
		}
	}

	@Override
	public Auction auctionFor(String itemId) {
		return new XMPPAuction(connection, itemId, failureReporter);
	}

	public static XMPPAuctionHouse connect(String hostname, String username, String password) throws XMPPException {
		XMPPConnection connection = new XMPPConnection(hostname);
		connection.connect();
		connection.login(username, password, AUCTION_RESOURCE);

		return new XMPPAuctionHouse(connection);
	}

	public void disconnect() {
		this.connection.disconnect();
	}

}

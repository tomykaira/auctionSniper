package auctionsniper.xmpp;

import java.util.logging.Logger;

public class LoggingXMPPFailureReporter implements XMPPFailureReporter {

	final private Logger logger;

	public LoggingXMPPFailureReporter(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void cannotTranslateMessage(String auctionId, String failedMessage,
			Exception exception) {
		logger.severe("<" + auctionId + "> "
				+ "Could not translate message \"" + failedMessage + "\" "
				+ "because \"" + exception + "\"");
	}
}

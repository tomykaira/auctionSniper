package auctionsniper.xmpp;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import auctionsniper.AuctionEventListener;

public class AuctionMessageTranslator implements MessageListener {

	private AuctionEventListener listener;

	public AuctionMessageTranslator(String sniperId, AuctionEventListener listener) {
		this.listener = listener;
	}

	@Override
	public void processMessage(Chat chat, Message message) {
		AuctionEvent event = AuctionEvent.from(message.getBody());

		String type = event.type();
		if ("CLOSE".equals(type)) {
			listener.auctionClosed();
		} else if ("PRICE".equals(type)) {
			listener.currentPrice(event.currentPrice(), event.increment(), null);
		}
	}

	private static class AuctionEvent {
		private final Map<String, String> fields = new HashMap<String, String>();
		public String type() { return get("Event"); }
		public int currentPrice() { return getInt("CurrentPrice"); }
		public int increment() { return getInt("Increment"); }

		private int getInt(String fieldName) {
			return Integer.parseInt(get(fieldName));
		}

		private String get(String fieldName) {
			return fields.get(fieldName);
		}

		private void addField(String field) {
			String[] pair = field.split(":");
			fields.put(pair[0].trim(), pair[1].trim());
		}

		static AuctionEvent from(String messageBody) {
			AuctionEvent event = new AuctionEvent();
			for (String field : fieldsIn(messageBody)) {
				event.addField(field);
			}
			return event;
		}

		static String[] fieldsIn(String messageBody) {
			return messageBody.split(";");
		}
	}

}

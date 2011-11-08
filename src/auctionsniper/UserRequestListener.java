package auctionsniper;

import java.util.EventListener;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public interface UserRequestListener extends EventListener {
	void joinAuction(Item item);

	public class Item {
		public final String identifier;
		public final int stopPrice;

		public Item(String identifier, int stopPrice) {
			this.identifier = identifier;
			this.stopPrice = stopPrice;
		}

	  @Override
	  public boolean equals(Object obj) { return EqualsBuilder.reflectionEquals(this, obj); }
	  @Override
	  public int hashCode() { return HashCodeBuilder.reflectionHashCode(this); }
	  @Override
	  public String toString() { return "Item: " + identifier + ", stop price: " + stopPrice; }
	}

}

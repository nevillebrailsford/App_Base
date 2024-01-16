package application.model;

import java.util.Objects;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public final class Address implements Comparable<Address> {
	private String street = "";
	private String town = "";
	private String county = "";
	private PostCode postcode = null;

	public Address(PostCode postCode, String[] linesOfAddress) {
		if (postCode == null) {
			throw new IllegalArgumentException("Address: postCode is null");
		}
		if (linesOfAddress == null) {
			throw new IllegalArgumentException("Address: lines of address are null");
		}
		if (linesOfAddress.length == 0) {
			throw new IllegalArgumentException("Address: lines of address are blank");
		}
		if (linesOfAddress.length > 3) {
			throw new IllegalArgumentException("Address: too many lines of address");
		}
		this.postcode = postCode;
		this.street = linesOfAddress[0];
		this.town = linesOfAddress[1];
		this.county = linesOfAddress[2];
	}

	public Address(Address that) {
		if (that == null) {
			throw new IllegalArgumentException("Address: that is null");
		}
		this.postcode = new PostCode(that.postcode);
		this.street = that.street;
		this.town = that.town;
		this.county = that.county;
	}

	public Address(Element addressElement) {
		if (addressElement == null) {
			throw new IllegalArgumentException("Address: addressElement is null");
		}
		if (!ElementChecker.verifyTag(addressElement, AppXMLConstants.ADDRESS)) {
			throw new IllegalArgumentException("Address: addressElement is not for Address");
		}
		NodeList list = addressElement.getElementsByTagName(AppXMLConstants.LINE);
		String[] linesOfAddress = new String[list.getLength()];
		for (int index = 0; index < list.getLength(); index++) {
			linesOfAddress[index] = list.item(index).getTextContent();
		}
		this.postcode = new PostCode((Element) addressElement.getElementsByTagName(AppXMLConstants.POSTCODE).item(0));
		this.street = linesOfAddress[0];
		this.town = linesOfAddress[1];
		this.county = linesOfAddress[2];
	}

	public Element buildElement(Document document) {
		if (document == null) {
			throw new IllegalArgumentException("Address: document is null");
		}
		Element result = document.createElement(AppXMLConstants.ADDRESS);
		result.appendChild(postCode().buildElement(document));
		for (int index = 0; index < linesOfAddress().length; index++) {
			result.appendChild(ElementBuilder.build(AppXMLConstants.LINE, linesOfAddress()[index], document));
		}
		return result;

	}

	public String[] linesOfAddress() {
		String[] lines = new String[3];
		lines[0] = street;
		lines[1] = town;
		lines[2] = county;
		return lines;
	}

	public PostCode postCode() {
		return new PostCode(postcode);
	}

	public String street() {
		return street;
	}

	public String town() {
		return town;
	}

	public String county() {
		return county;
	}

	public String fullAddress() {
		return toString();
	}

	@Override
	public int compareTo(Address that) {
		return this.toString().compareTo(that.toString());
	}

	@Override
	public int hashCode() {
		return Objects.hash(street, town, county, postcode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address that = (Address) obj;
		return Objects.equals(street, that.street) && Objects.equals(this.town, that.town)
				&& Objects.equals(this.county, that.county) && Objects.equals(postcode, that.postcode);
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(street);
		stringBuilder.append(", ").append(town);
		stringBuilder.append(", ").append(county);
		stringBuilder.append(" ").append(postcode.toString());
		return stringBuilder.toString();
	}

	public static class Builder {
		private PostCode postcode = null;
		private String street = null;
		private String town = null;
		private String county = null;

		public Builder street(String street) {
			this.street = street;
			return this;
		}

		public Builder town(String town) {
			this.town = town;
			return this;
		}

		public Builder county(String county) {
			this.county = county;
			return this;
		}

		public Builder postCode(PostCode postcode) {
			this.postcode = postcode;
			return this;
		}

		public Address build() {
			if (postcode == null) {
				throw new IllegalArgumentException("Address: postcode is null");
			}
			if (street == null) {
				throw new IllegalArgumentException("Address: street is null");
			}
			if (street.isBlank()) {
				throw new IllegalArgumentException("Address: street is blank");
			}
			if (town == null) {
				throw new IllegalArgumentException("Address: town is null");
			}
			if (town.isBlank()) {
				throw new IllegalArgumentException("Address: town is blank");
			}
			if (county == null) {
				throw new IllegalArgumentException("Address: county is null");
			}
			if (county.isBlank()) {
				throw new IllegalArgumentException("Address: county is blank");
			}
			return new Address(postcode, new String[] { street, town, county });
		}
	}
}

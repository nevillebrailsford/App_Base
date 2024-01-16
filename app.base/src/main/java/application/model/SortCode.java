package application.model;

import java.util.Objects;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class SortCode implements Comparable<SortCode> {
	public static String sortCodeRegularExpression = "^[0-9]{2}[-][0-9]{2}[-][0-9]{2}$";
	public static String postCodeRegularExpression = "^[A-Z]{1,2}\\d[A-Z\\d]? ?\\d[A-Z]{2}$";
	private String value = "";

	public SortCode(String value) {
		if (value == null) {
			throw new IllegalArgumentException("SortCode: value is null");
		}
		if (!value.matches(sortCodeRegularExpression)) {
			throw new IllegalArgumentException("SortCode: " + value + " is not a valid sort code");
		}
		this.value = value;
	}

	public SortCode(SortCode that) {
		if (that == null) {
			throw new IllegalArgumentException("SortCode: that is null");
		}
		this.value = that.value;
	}

	public SortCode(Element sortCodeElement) {
		if (sortCodeElement == null) {
			throw new IllegalArgumentException("SortCode: sortCodeElement is null");
		}
		if (!ElementChecker.verifyTag(sortCodeElement, AppXMLConstants.SORTCODE)) {
			throw new IllegalArgumentException("SortCode: sortCodeElement is not for SortCode");
		}
		this.value = sortCodeElement.getTextContent();
	}

	public Element buildElement(Document document) {
		if (document == null) {
			throw new IllegalArgumentException("SortCode: document is null");
		}
		Element result = document.createElement(AppXMLConstants.SORTCODE);
		result.setTextContent(value);
		return result;
	}

	public String value() {
		return value;
	}

	@Override
	public int compareTo(SortCode that) {
		return this.value.compareTo(that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SortCode that = (SortCode) obj;
		return Objects.equals(value, that.value);
	}

	@Override
	public String toString() {
		return value;
	}

}

package application.model;

import java.util.Objects;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class PostCode implements Comparable<PostCode> {
	public static String postCodeRegularExpression = "^[A-Z]{1,2}\\d[A-Z\\d]? ?\\d[A-Z]{2}$";
	private String value = "";

	public PostCode(String value) {
		if (value == null) {
			throw new IllegalArgumentException("PostCode: value is null");
		}
		if (!value.matches(postCodeRegularExpression)) {
			throw new IllegalArgumentException(" is not a valid sort code");
		}
		this.value = value;
	}

	public PostCode(PostCode that) {
		if (that == null) {
			throw new IllegalArgumentException("PostCode: that is null");
		}
		this.value = that.value;
	}

	public PostCode(Element postCodeElement) {
		if (postCodeElement == null) {
			throw new IllegalArgumentException("PostCode: postCodeElement is null");
		}
		if (!ElementChecker.verifyTag(postCodeElement, AppXMLConstants.POSTCODE)) {
			throw new IllegalArgumentException("PostCode: postCodelement is not for PostCode");
		}
		this.value = postCodeElement.getTextContent();
	}

	public Element buildElement(Document document) {
		if (document == null) {
			throw new IllegalArgumentException("PostCode: document is null");
		}
		Element result = document.createElement(AppXMLConstants.POSTCODE);
		result.setTextContent(value);
		return result;
	}

	public String value() {
		return value;
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
		PostCode that = (PostCode) obj;
		return Objects.equals(value, that.value);
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public int compareTo(PostCode that) {
		return this.value.compareTo(that.value);
	}

}

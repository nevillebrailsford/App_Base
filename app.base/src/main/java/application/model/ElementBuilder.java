package application.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class ElementBuilder {
	public synchronized static Element build(String tag, String text, Document document) {
		if (tag == null) {
			throw new IllegalArgumentException("Element Builder: tag is null");
		}
		if (text == null) {
			throw new IllegalArgumentException("Element Builder: text is null");
		}
		if (document == null) {
			throw new IllegalArgumentException("Element Builder: document is null");
		}
		if (tag.isBlank()) {
			throw new IllegalArgumentException("Element Builder: tag is blank");
		}
		if (text.isBlank()) {
			throw new IllegalArgumentException("Element Builder: text is blank");
		}
		Element result = document.createElement(tag);
		result.setTextContent(text);
		return result;
	}

}

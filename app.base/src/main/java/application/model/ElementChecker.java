package application.model;

import org.w3c.dom.Element;

public final class ElementChecker {
	public synchronized static boolean verifyTag(Element element, String tag) {
		if (element == null) {
			throw new IllegalArgumentException("ElementCheck: element is null");
		}
		if (tag == null) {
			throw new IllegalArgumentException("ElementCheck: tag is null");
		}
		if (tag.isBlank()) {
			throw new IllegalArgumentException("ElementCheck: tag is blank");
		}
		return tag.equals(element.getTagName());
	}

}

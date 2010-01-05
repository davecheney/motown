package net.cheney.motown.uri;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

public abstract class Path implements Comparable<Path>, Enumerable<String> {

	public static final Path EMPTY_PATH = Path.builder().build();
	public static final char SEPERATOR = '/';

	public static Path.Builder builder() {
		return new Builder();
	}
	
	public abstract Path pop();
	
	public static Path fromString(String string) {
		return builder().addElement(StringUtils.split(string, SEPERATOR)).build();
	}
	
	public static class Builder {

		private final ArrayList<String> elements = new ArrayList<String>();

		private Builder() {
		}

		public Builder addElement(String element) {
			elements.add(element);
			return this;
		}

		public Builder addElement(String... elements) {
			return addElement(elements, 0, elements.length);
		}

		public Builder addElement(String[] elements, int offset, int length) {
			for (; offset < length; ++offset) {
				this.elements.add(elements[offset]);
			}
			return this;
		}

		public Path build() {
			return new ArrayPath(elements.toArray(new String[elements.size()]));
		}

	}
}

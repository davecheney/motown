package net.cheney.motown.server.api;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang.StringUtils;

public abstract class Path implements Comparable<Path>, Enumerable<String> {

	public static final Path EMPTY_PATH = new ArrayPath(Collections.<String>emptyList());
	public static final char SEPERATOR = '/';

	public abstract Path pop();
	
	public static Path fromString(String string) {
		return new ArrayPath(Arrays.asList(StringUtils.split(string, SEPERATOR)));
	}
	
	public static Path fromUri(URI uri) {
		return fromString(uri.getPath());
	}
	
	public abstract int size();

	/**
	 * @param size the number of elements to return
	 * @return a {@link Path} containing the first {@link size} elements
	 */
	public abstract Path first(int size);
	
	@Override
	public abstract boolean equals(Object obj);
	
	@Override
	public String toString() {
		return StringUtils.join(this.iterator(), SEPERATOR);
	}
}

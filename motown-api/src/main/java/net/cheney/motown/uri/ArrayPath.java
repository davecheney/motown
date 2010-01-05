package net.cheney.motown.uri;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

public class ArrayPath extends Path {

	private String[] content;

	protected ArrayPath(String[] array) {
		this.content = array;
	}

	@Override
	public Path pop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compareTo(Path o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String first() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String last() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		return StringUtils.join(this.iterator(), SEPERATOR);
	}

	@Override
	public Iterator<String> iterator() {
		return Arrays.asList(this.content).iterator();
	}

}

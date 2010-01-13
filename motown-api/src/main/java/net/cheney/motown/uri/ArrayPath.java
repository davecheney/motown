package net.cheney.motown.uri;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class ArrayPath extends Path {

	private final List<String> elements;
	
	ArrayPath(Iterable<String> elements) {
		this.elements = Lists.newArrayList(elements);
	}
	
	@Override
	public Path pop() {
		return new ArrayPath(Lists.partition(elements, 1).get(1));
	}

	@Override
	public int size() {
		return elements.size();
	}

	@Override
	public Path first(int size) {
		return new ArrayPath(Lists.partition(elements, size).get(0));
	}

	@Override
	public int compareTo(Path o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String first() {
		return Iterables.get(elements, 0);
	}

	@Override
	public String last() {
		return Iterables.getLast(elements);
	}

	@Override
	public Iterator<String> iterator() {
		return elements.iterator();
	}

	@Override
	public boolean equals(Object that) {
		if(that instanceof Path && ((Path)that).size() == size()) {
			return Iterables.elementsEqual(this, ((Path)that));
		}
		return false;
	}

}

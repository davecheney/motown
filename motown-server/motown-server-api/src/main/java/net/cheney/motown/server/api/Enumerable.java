package net.cheney.motown.server.api;

public interface Enumerable<T> extends Iterable<T> {

	T first();

	T last();

}

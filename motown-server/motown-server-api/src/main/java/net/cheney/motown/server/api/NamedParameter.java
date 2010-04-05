package net.cheney.motown.server.api;

public abstract class NamedParameter<K> implements Parameter<K> {

	public abstract String name();
	
	@Override
	public String toString() {
		return name();
	}
}

package net.cheney.motown.server.api;

public abstract class NamedParameter<K> extends Parameter<K> {

	protected NamedParameter(Class<K> klazz) {
		super(klazz);
	}

	public abstract String name();
	
	@Override
	public String toString() {
		return name();
	}

}

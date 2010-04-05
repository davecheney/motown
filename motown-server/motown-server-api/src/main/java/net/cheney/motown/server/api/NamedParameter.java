package net.cheney.motown.server.api;

public abstract class NamedParameter<K> extends Parameter<K> {

	private final String name;

	protected NamedParameter(String name,Class<K> klazz) {
		super(klazz);
		this.name = name;
	}

	public String name() {
		return name;
	}
	
	@Override
	public String toString() {
		return name();
	}

}

package net.cheney.motown.server.api;

public abstract class Parameter<K> { 
	
	private final Class<K> klazz;

	protected Parameter(Class<K> klazz) {
		this.klazz = klazz;
	}

	public abstract K decode(String string);
	
	public abstract String encode(K value);
}
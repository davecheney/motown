package net.cheney.motown.server.api;

import java.util.HashMap;
import java.util.Map;

public class Parameters {

	private final Map<Parameter<?>, Object> entries = new HashMap<Parameter<?>, Object>();

	@SuppressWarnings("unchecked")
	public <K> K get(Parameter<K> key) {
		return (K) entries.get(key);
	}
	
	public <K> K put(Parameter<K> key, K value) {
		entries.put(key, value);
		return value;
	}
	
	public Iterable<Parameter<?>> keys() {
		return entries.keySet();
	}
}

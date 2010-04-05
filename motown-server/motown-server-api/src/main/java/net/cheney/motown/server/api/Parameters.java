package net.cheney.motown.server.api;

import java.util.HashMap;
import java.util.Map;

public class Parameters {

	private final Map<Key<?>, Object> entries = new HashMap<Key<?>, Object>();

	public interface Key<K> { 
		
	}
	
	@SuppressWarnings("unchecked")
	public <K> K get(Key<K> key) {
		return (K) entries.get(key);
	}
	
	public <K> K put(Key<K> key, K value) {
		entries.put(key, value);
		return value;
	}
	
	public Iterable<Key<?>> keys() {
		return entries.keySet();
	}
}

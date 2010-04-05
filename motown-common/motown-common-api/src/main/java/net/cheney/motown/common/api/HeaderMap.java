package net.cheney.motown.common.api;

import java.util.HashMap;
import java.util.Map;

public class HeaderMap {

	public final Map<HeaderKey<?>, Object> entries = new HashMap<HeaderKey<?>, Object>();
	
	public interface HeaderKey<T> {
		
		Class<T> type();
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(HeaderKey<T> key) {
		return (T)entries.get(key);
	}
	
	public <T> void put(HeaderKey<T> key, T value) {
		entries.put(key, value);
	}
}

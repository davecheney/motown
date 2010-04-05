package net.cheney.motown.common.api;

public abstract class DataCoder {

	public abstract String encode(Object ob);

	public abstract <T> T decode(String data, Class<T> c);

	public abstract boolean supports(Class<?> c);

}

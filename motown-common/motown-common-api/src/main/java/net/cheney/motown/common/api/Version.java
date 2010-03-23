package net.cheney.motown.common.api;

import javax.annotation.Nonnull;

public enum Version {

	HTTP_0_9("HTTP/0.9"), HTTP_1_0("HTTP/1.0"), HTTP_1_1("HTTP/1.1");

	private final String value;

	private Version(@Nonnull String value) {
		this.value = value;
	}

	public static final Version parse(@Nonnull CharSequence version) {
		return valueOf(version.toString().replace('/', '_').replace('.', '_'));
	}

	@Override
	public final String toString() {
		return value;
	}

}
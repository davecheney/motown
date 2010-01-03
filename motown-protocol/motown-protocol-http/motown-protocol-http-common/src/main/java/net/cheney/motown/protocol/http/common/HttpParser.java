package net.cheney.motown.protocol.http.common;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import net.cheney.motown.api.Header;
import net.cheney.motown.api.Message;

import com.google.common.collect.Multimap;

public abstract class HttpParser<V extends Message> extends ParserSupport {

	static final Charset US_ASCII = Charset.forName("US-ASCII");

	public abstract V parse(ByteBuffer buffer);

	abstract Multimap<Header, String> headers();

	final boolean isWhitespace(byte t) {
		return (t == ' ' || t == '\t');
	}

	final boolean isVisibleCharacter(byte t) {
		return (t >= '\u0021' && t <= '\u007E');
	}

	public abstract void reset();
}

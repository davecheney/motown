package net.cheney.motown.protocol.http.common;

abstract class ParserSupport {

	final boolean isTokenChar(byte b) {
		return ((b >= '\u0030' && b <= '\u0039')
				|| (b >= '\u0041' && b <= '\u005A')
				|| (b >= '\u0061' && b <= '\u007a') || b == '!' || b == '#'
				|| b == '$' || b == '%' || b == '&' || b == '\'' || b == '*'
				|| b == '+' || b == '-' || b == '.' || b == '^' || b == '_'
				|| b == '`' || b == '|' || b == '~');
	}

}

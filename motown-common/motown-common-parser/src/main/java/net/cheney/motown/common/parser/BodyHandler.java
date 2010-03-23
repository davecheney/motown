package net.cheney.motown.common.parser;

import java.nio.ByteBuffer;

public interface BodyHandler {
	
	void bodyReceived(final ByteBuffer buffer);
}
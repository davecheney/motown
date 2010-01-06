package net.cheney.motown.protocol.http.common;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Deque;

import net.cheney.motown.api.Header;
import net.cheney.motown.api.Request;
import net.cheney.motown.api.RequestLine;

public class RequestParser extends HttpParser<Request> {

	private enum State {
		REQUEST_LINE, REQUEST_END, HEADER_KEY, HEADER_DELIMITER, HEADER_VALUE, HEADER_VALUE_END, HEADERS, WHITESPACE
	}
	
	private final Deque<State> stateStack = new ArrayDeque<State>();
	private RequestLineParser requestLineParser;
	private int offset;
	private Request request;
	private Header headerKey;
	
	public RequestParser() {
		reset();
	}

	public void reset() {
		stateStack.push(State.REQUEST_END);
		resetForHeader();
		stateStack.push(State.REQUEST_LINE);
		requestLineParser = new RequestLineParser();
		this.request = null;
	}
	
	private void resetForHeader() {
		stateStack.push(State.HEADER_VALUE_END);
		stateStack.push(State.HEADER_VALUE);
		stateStack.push(State.HEADER_KEY);
		this.headerKey = null;
	}
	
	public Request parse(final ByteBuffer buffer) {
		offset = buffer.position();
		while(buffer.hasRemaining()) {
			switch (stateStack.peek()) {
			case REQUEST_LINE:
				RequestLine requestLine = requestLineParser.parse(buffer);
				if(requestLine != null) {
					request = new Request(requestLine);
					offset = buffer.position();
					stateStack.pop();
				}
				break;
				
			case HEADER_KEY:
				byte b = buffer.get();
				if(isTokenChar(b)) {
					continue;
				} else if (b == ':') {
					int length = buffer.position() - offset;
					String s = new String(buffer.array(), buffer.arrayOffset() + offset, --length, US_ASCII);
					headerKey = Header.parse(s);
					offset = buffer.position();
					stateStack.pop();
				} else if (b == '\r') {
					stateStack.pop();
					stateStack.pop();
					stateStack.pop();
				} else {
					throw new IllegalArgumentException(String.format("Illegal character '%s' in %s", (char)b, stateStack.peek()));
				}
				break;
				
			case HEADER_VALUE:
				byte t = buffer.get();
				if(isVisibleCharacter(t) || isWhitespace(t)) {
					continue;
				} else if( t == '\r') {
					if(headerKey != null) {
						// skip headers with no known key
						int length = buffer.position() - offset;
						String value = new String(buffer.array(), buffer.arrayOffset() + offset, --length, US_ASCII);
						request.headers().put(headerKey, value.trim());
					}
					offset = buffer.position();
					stateStack.pop();
				} else {
					throw new IllegalArgumentException(String.format("Illegal character '%x' in %s", t, stateStack.peek()));
				}
				break;
				
			case WHITESPACE:
				if(!isWhitespace(buffer.get())) {
					stateStack.pop();
				}
				break;
				
			case HEADER_VALUE_END:
				byte k = buffer.get();
				if( k == '\n') {
					offset = buffer.position();
					stateStack.pop();
					resetForHeader();
				} else {
					throw new IllegalArgumentException(String.format("Illegal character '%h' in %s", k, stateStack.peek()));
				}
				break;
				
			case REQUEST_END:
				byte o = buffer.get();
				if( o == '\n') {
					stateStack.pop();
					return request;
				}
				
			default:
				throw new IllegalArgumentException(String.format("Illegal state %s", stateStack.peek()));
			}
		}
		buffer.position(offset);
		return null;
	}
	
}

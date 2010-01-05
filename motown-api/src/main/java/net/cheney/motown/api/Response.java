package net.cheney.motown.api;

import java.nio.ByteBuffer;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import static net.cheney.motown.api.Status.*;

public class Response extends Message {
	
	private final StatusLine statusLine;

	private Response(StatusLine statusLine, Multimap<Header, String> headers, ByteBuffer body) {
		super(headers, body);
		this.statusLine = statusLine;
	}

	public Response(Status status) {
		this(new StatusLine(Version.HTTP_1_1, status), ArrayListMultimap.<Header, String>create(), ByteBuffer.allocate(0));
	}
	
	public Response(Status status, Multimap<Header, String> headers, ByteBuffer body) {
		this(new StatusLine(Version.HTTP_1_1, status), headers, body);
	}

	public Status status() {
		return this.statusLine.status();
	}
	
	@Override
	public Version version() {
		return this.statusLine.version();
	}
	
	public static Builder builder(Status status) {
		return new Builder(status);
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	@Override
	public boolean equals(Object that) {
		return EqualsBuilder.reflectionEquals(this, that);
	}
	
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
	public static class Builder extends Message.Builder {

		private Multimap<Header, String> headers = ArrayListMultimap.create();
		private ByteBuffer body = null;
		private final Status status;

		private Builder(Status status) {
			this.status = status;
		}
		
		Status status() {
			return status;
		}
		
		@Override
		Version version() {
			return Version.HTTP_1_1;
		}
		
		@Override
		Multimap<Header, String> headers() {
			return headers;
		}
		
		@Override
		ByteBuffer body() {
			return body;
		}

		@Override
		public Response build() {
			return new Response(status(), headers(), body());
		}

		public Builder setHeader(Header key, String value) {
			headers().put(key, value);
			return this;
		}

		@Override
		public Builder setBody(ByteBuffer buffer) {
			this.body = buffer;
			return this;
		}
		
	}
	
	@Override
	public Response setBody(final ByteBuffer body) {
		return new Builder(status()) {
			@Override
			ByteBuffer body() {
				return body;
			}
		}.build();
	}
	
	public static Response successCreated() {
		return Response.builder(Status.SUCCESS_CREATED).build();
	}
	
	public static Response successNoContent() {
		return Response.builder(Status.SUCCESS_NO_CONTENT).build();
	}

	public static Response serverErrorInternal() {
		return Response.builder(Status.SERVER_ERROR_INTERNAL).build();
	}

	public static Response serverErrorNotImplemented() {
		return Response.builder(Status.SERVER_ERROR_NOT_IMPLEMENTED).build();
	}

	public static Response clientErrorNotFound() {
		return Response.builder(CLIENT_ERROR_NOT_FOUND).build();
	}

	public static Response clientErrorPreconditionFailed() {
		return Response.builder(CLIENT_ERROR_PRECONDITION_FAILED).build();
	}
	
	public static Response clientErrorMethodNotAllowed() {
		return Response.builder(CLIENT_ERROR_METHOD_NOT_ALLOWED).build();
	}
	
	public static Response clientErrorConflict() {
		return Response.builder(CLIENT_ERROR_CONFLICT).build();
	}
	
	public static Response clientErrorLocked() {
		return Response.builder(CLIENT_ERROR_LOCKED).build();
	}

	public static Response clientErrorUnsupportedMediaType() {
		return Response.builder(CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE).build();
	}
	
	public static Response redirectionNotModified() {
		return Response.builder(Status.REDIRECTION_NOT_MODIFIED).build();
	}
	

	public static Response success(MimeType mime, ByteBuffer buffer) {
		return Response.builder(SUCCESS_OK).setHeader(Header.CONTENT_TYPE, mime.toString()).setBody(buffer).build();
	}

	public boolean mayContainBody() {
		// http://tools.ietf.org/html/draft-ietf-httpbis-p1-messaging-08#section-3.4
		return (!status().isInformational() && !status().equals(SUCCESS_NO_CONTENT) && !status().equals(REDIRECTION_NOT_MODIFIED));
	}
}

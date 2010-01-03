package net.cheney.motown.webservice.controller;

import static com.google.common.collect.Iterables.getOnlyElement;
import static net.cheney.motown.api.Response.clientErrorConflict;
import static net.cheney.motown.api.Response.clientErrorLocked;
import static net.cheney.motown.api.Response.clientErrorMethodNotAllowed;
import static net.cheney.motown.api.Response.clientErrorNotFound;
import static net.cheney.motown.api.Response.redirectionNotModified;
import static net.cheney.motown.api.Response.serverErrorInternal;
import static net.cheney.motown.api.Response.serverErrorNotImplemented;
import static net.cheney.motown.api.Response.successCreated;
import static net.cheney.motown.api.Response.successNoContent;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.cheney.motown.api.Header;
import net.cheney.motown.api.Method;
import net.cheney.motown.api.MimeType;
import net.cheney.uri.Path;
import net.cheney.motown.api.Request;
import net.cheney.motown.api.Response;
import net.cheney.motown.dispatcher.dynamic.Context;
import net.cheney.motown.dispatcher.dynamic.DELETE;
import net.cheney.motown.dispatcher.dynamic.GET;
import net.cheney.motown.dispatcher.dynamic.OPTIONS;
import net.cheney.motown.dispatcher.dynamic.PUT;

public abstract class StaticController {

	@GET
	public Response get(@Context Request request) throws IOException {
		final Path path = Path.fromString(request.uri().getPath());
		final Resource resource = resolveResource(path);
		if (resource.exists()) {
			if(resource.isCollection()) {
				return clientErrorNotFound();
			} else {
				return getResource(request);
			}
		} else {
			return Response.clientErrorNotFound();
		}
	}
	
	@PUT
	public Response put(@Context Request request) throws IOException {
		final Path path = Path.fromString(request.uri().getPath());
		final Resource resource = resolveResource(path);
		if (!resource.parent().exists()) {
			return clientErrorConflict();
		}

		if (resource.exists() && resource.isCollection()) {
			return clientErrorMethodNotAllowed();
		}

		ByteBuffer entity = request.body();
		resource.put(entity);
		return successCreated();
	}
	
	@OPTIONS
	public Response options(@Context Request request) {
		final Path path = Path.fromString(request.uri().getPath());
		final Resource resource = resolveResource(path);
		
		Response response = Response.successNoContent();
		for(Method method : resource.supportedMethods()) {
			response.headers().put(Header.ALLOW, method.name());
		}
		
		return response;
	}
	
	private final Response getResource(final Request request) throws IOException {
		if (request.containsHeader(Header.IF_MATCH)) {
			return handleRequestWithIfMatch(request);
		} else {
			return handleRequestWithoutIfMatch(request);
		}
	}
	
	private Response handleRequestWithIfMatch(Request request) throws IOException {
		final Path path = Path.fromString(request.uri().getPath());
		final Resource resource = resolveResource(path);

		final String ifMatch = getOnlyElement(request.headers().get(Header.IF_MATCH));
		
		if (ifMatch.equals("*") || !ifMatch.equals(resource.etag())) {
			return handleRequestWithoutIfMatch(request);
		} else {
			return Response.clientErrorPreconditionFailed();
		}
	}
	
	private Response handleRequestWithoutIfMatch(Request request) throws IOException {
		if (request.containsHeader(Header.IF_UNMODIFIED_SINCE)) {
			return handleRequestWithIfUnmodifiedSince();
		} else {
			return handleRequestWithoutIfUnmodifiedSince(request);
		}
	}
	
	private Response handleRequestWithIfUnmodifiedSince() {
		return Response.serverErrorNotImplemented();
	}

	private Response handleRequestWithoutIfUnmodifiedSince(Request request) throws IOException {
		if (request.containsHeader(Header.IF_NONE_MATCH)) {
			return handleRequestWithIfNoneMatch();
		} else {
			return handleRequestWithoutIfNoneMatch(request);
		}
	}
	
	private Response handleRequestWithIfNoneMatch() {
		return serverErrorNotImplemented();
	}

	private Response handleRequestWithoutIfNoneMatch(Request request) throws IOException {
		if (request.containsHeader(Header.IF_MODIFIED_SINCE)) {
			return handleRequestWithIfModifiedSince(request);
		} else {
			return handleRequestWithoutIfModifiedSince(request);
		}
	}
	
	private Response handleRequestWithoutIfModifiedSince(final Request request) throws IOException {
		switch (request.method()) {
		case POST:

		case PUT:

//		case DELETE:
//			return delete(request);

		case GET:
		case HEAD:
			if (request.headers().containsKey(Header.ACCEPT)) {
				return handleRequestWithAccept(request);
			} else {
				return handleRequestWithoutAccept(request);
			}

		default:
			return clientErrorMethodNotAllowed();
		}
	}
	
	private Response handleRequestWithAccept(final Request request) {
		return handleRequestWithoutAccept(request);
	}

	private Response handleRequestWithoutAccept(final Request request) {
		if (request.containsHeader(Header.ACCEPT_LANGUAGE)) {
			return handleRequestWithAcceptLanguage();
		} else {
			return handleRequestWithoutAcceptLanguage(request);
		}
	}

	private Response handleRequestWithAcceptLanguage() {
		return serverErrorNotImplemented();
	}

	private Response handleRequestWithoutAcceptLanguage(final Request request) {
		if (request.containsHeader(Header.ACCEPT_CHARSET)) {
			return handleRequestWithAcceptCharset();
		} else {
			return handleRequestWithoutAcceptCharset(request);
		}
	}

	private Response handleRequestWithAcceptCharset() {
		return serverErrorNotImplemented();
	}

	private Response handleRequestWithoutAcceptCharset(final Request request) {
		final Path path = Path.fromString(request.uri().getPath());
		final Resource resource = resolveResource(path);
		
		try {
			return Response.success(MimeType.APPLICATION_OCTET_STREAM, resource.entity());
		} catch (IOException e) {
			return serverErrorInternal();
		}
	}

	private Response handleRequestWithIfModifiedSince(final Request request) throws IOException {
		final Path path = Path.fromString(request.uri().getPath());
		final Resource resource = resolveResource(path);

		try {
			String date = getOnlyElement(request.headers().get(Header.IF_MODIFIED_SINCE));
			Date ifModifiedSince = new SimpleDateFormat().parse(date);
			if (!ifModifiedSince.after(new Date())
					&& !(resource.lastModified().after(new Date()))) {
				return redirectionNotModified();
			} else {
				return handleRequestWithoutIfModifiedSince(request);
			}
		} catch (ParseException e) {
			return handleRequestWithoutIfModifiedSince(request);
		}
	}
	
	@DELETE
	public Response delete(@Context Request request) throws IOException {
		final Path path = Path.fromString(request.uri().getPath());
		final Resource resource = resolveResource(path);
		
		if (request.uri().getFragment() != null) {
			return clientErrorMethodNotAllowed();
		} else {
			if (resource.isLocked()) {
				return clientErrorLocked();
			}
			if (resource.exists()) {
				if (resource.isLocked()) {
					return clientErrorLocked();
				} else {
					if (resource.isCollection()) {
						return (resource.delete() ? successNoContent() : serverErrorInternal());
					} else {
						return (resource.delete() ? successNoContent() : serverErrorInternal());
					}
				}
			} else {
				return clientErrorNotFound();
			}
		}
	}

	protected abstract Resource resolveResource(Path path);
	
	protected abstract <T extends Resource> URI relativizeResource(T resource); 
}
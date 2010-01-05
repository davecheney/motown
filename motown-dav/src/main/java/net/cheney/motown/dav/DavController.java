package net.cheney.motown.dav;

import static com.google.common.collect.Iterables.getOnlyElement;
import static net.cheney.motown.api.Response.clientErrorConflict;
import static net.cheney.motown.api.Response.clientErrorLocked;
import static net.cheney.motown.api.Response.clientErrorMethodNotAllowed;
import static net.cheney.motown.api.Response.clientErrorNotFound;
import static net.cheney.motown.api.Response.clientErrorPreconditionFailed;
import static net.cheney.motown.api.Response.clientErrorUnsupportedMediaType;
import static net.cheney.motown.api.Response.redirectionNotModified;
import static net.cheney.motown.api.Response.serverErrorInternal;
import static net.cheney.motown.api.Response.serverErrorNotImplemented;
import static net.cheney.motown.api.Response.successCreated;
import static net.cheney.motown.api.Response.successNoContent;
import static net.cheney.motown.resource.api.Elements.activeLock;
import static net.cheney.motown.resource.api.Elements.href;
import static net.cheney.motown.resource.api.Elements.lockDiscovery;
import static net.cheney.motown.resource.api.Elements.multistatus;
import static net.cheney.motown.resource.api.Elements.prop;
import static net.cheney.motown.resource.api.Elements.propertyStatus;
import static net.cheney.motown.resource.api.Elements.response;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import net.cheney.motown.api.Depth;
import net.cheney.motown.api.Header;
import net.cheney.motown.api.Method;
import net.cheney.motown.api.MimeType;
import net.cheney.motown.api.Request;
import net.cheney.motown.api.Response;
import net.cheney.motown.api.Status;
import net.cheney.motown.api.Response.Builder;
import net.cheney.motown.dispatcher.dynamic.COPY;
import net.cheney.motown.dispatcher.dynamic.Context;
import net.cheney.motown.dispatcher.dynamic.DELETE;
import net.cheney.motown.dispatcher.dynamic.GET;
import net.cheney.motown.dispatcher.dynamic.LOCK;
import net.cheney.motown.dispatcher.dynamic.MKCOL;
import net.cheney.motown.dispatcher.dynamic.MOVE;
import net.cheney.motown.dispatcher.dynamic.OPTIONS;
import net.cheney.motown.dispatcher.dynamic.PROPFIND;
import net.cheney.motown.dispatcher.dynamic.PROPPATCH;
import net.cheney.motown.dispatcher.dynamic.PUT;
import net.cheney.motown.dispatcher.dynamic.UNLOCK;
import net.cheney.motown.resource.api.Elements;
import net.cheney.motown.resource.api.Lock;
import net.cheney.motown.resource.api.Property;
import net.cheney.motown.resource.api.Resource;
import net.cheney.motown.resource.api.ResourceProvidor;
import net.cheney.motown.resource.api.Elements.ACTIVE_LOCK;
import net.cheney.motown.resource.api.Elements.MULTISTATUS;
import net.cheney.motown.resource.api.Elements.PROPSTAT;
import net.cheney.motown.resource.api.Elements.RESPONSE;
import net.cheney.motown.resource.api.Lock.Scope;
import net.cheney.motown.resource.api.Lock.Type;
import net.cheney.motown.resource.api.Resource.ComplianceClass;
import net.cheney.snax.model.Document;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.ProcessingInstruction;
import net.cheney.snax.model.QName;
import net.cheney.snax.parser.XMLBuilder;
import net.cheney.snax.writer.XMLWriter;
import net.cheney.uri.Path;

import org.apache.log4j.Logger;

import com.google.common.base.Function;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;

public class DavController {
	
	private static final Logger LOG = Logger.getLogger(DavController.class);

	private static final List<QName> ALL_PROPS = Arrays.asList(new QName[] {
			Property.CREATION_DATE, Property.DISPLAY_NAME,
			Property.GET_CONTENT_LENGTH, Property.GET_LAST_MODIFIED,
			Property.RESOURCE_TYPE });
	
	private static final Charset CHARSET_UTF_8 = Charset.forName("UTF-8");

	private final ResourceProvidor providor;	
	
	public DavController(ResourceProvidor providor) {
		this.providor = providor;
	}

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
			return handleRequestWithAcceptLanguage(request);
		} else {
			return handleRequestWithoutAcceptLanguage(request);
		}
	}

	private Response handleRequestWithAcceptLanguage(final Request request) {
		return handleRequestWithoutAcceptLanguage(request);
	}

	private Response handleRequestWithoutAcceptLanguage(final Request request) {
		if (request.containsHeader(Header.ACCEPT_CHARSET)) {
			return handleRequestWithAcceptCharset(request);
		} else {
			return handleRequestWithoutAcceptCharset(request);
		}
	}

	private Response handleRequestWithAcceptCharset(final Request request) {
		return handleRequestWithoutAcceptCharset(request);
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
	
	@LOCK
	public Response lock(@Context Request request) throws IOException {
		final Path path = Path.fromString(request.uri().getPath());
		final Resource resource = resolveResource(path);

		if (resource.exists()) {
			final Lock lock = resource.lock(Type.WRITE, Scope.EXCLUSIVE);
			
			final ACTIVE_LOCK activelock = activeLock(lock, request.getDepth(Depth.INFINITY), relativizeResource(resource));
			final Element lockDiscovery = lockDiscovery(activelock);
			final Element prop = prop(lockDiscovery);
			final Document doc = new Document(ProcessingInstruction.XML_DECLARATION, prop);
			Builder builder = Response.builder(Status.SUCCESS_OK);
			builder.setHeader(Header.LOCK_TOKEN, "<" + lock.token() + ">");
			builder.setBody(Charset.defaultCharset().encode(XMLWriter.write(doc)));
			return builder.build();
		} else {
			return clientErrorNotFound();
		}
	}
	
	@OPTIONS
	public Response options(@Context Request request) {
		final Path path = Path.fromString(request.uri().getPath());
		final Resource resource = resolveResource(path);

		Response response = Response.successNoContent();
		for(Method method : resource.supportedMethods()) {
			response.headers().put(Header.ALLOW, method.name());
		}
		
		for(ComplianceClass value : resource.davOptions()) {
			response.headers().put(Header.DAV, value.toString());
		}
		
		return response;
	}
	
	@UNLOCK
	public Response unlock(@Context Request request) throws IOException {
		final Path path = Path.fromString(request.uri().getPath());
		final Resource resource = resolveResource(path);
		
		if (resource.exists()) {
			if (resource.isLocked()) {
				resource.unlock();
				return successNoContent();
			} else {
				return clientErrorLocked();
			}
		} else {
			return clientErrorNotFound();
		}
	}
	
	@MOVE
	public Response move(@Context Request request) throws IOException {
		final Path path = Path.fromString(request.uri().getPath());
		final Resource source = resolveResource(path);

		final URI destinationUri = request.getDestination();
		final Resource destination = resolveResource(Path.fromString(destinationUri.getPath()));
		
		if (destination.isLocked()) {
			return Response.clientErrorLocked();
		}
		
		final boolean overwrite = request.isOverwrite();
		
		if (source.exists()) {
			if (source.isCollection()) { // source exists
				if (destination.exists()) { // source exists and is a collection
					if (destination.isCollection()) {
						return (overwrite ? moveCollectionToCollection(source,
								destination, overwrite)
								: Response.clientErrorPreconditionFailed());
					} else {
						return (overwrite ? moveCollectionToResource(source,
								destination, overwrite)
								: Response.clientErrorPreconditionFailed());
					}
				} else {
					return (destination.parent().exists() ? moveCollectionToCollection(
							source, destination, false)
							: Response.clientErrorPreconditionFailed());
				}
			} else {
				if (destination.exists()) { // source exists
					if (destination.isCollection()) { // source exists,
						return (overwrite ? moveResourceToCollection(source,
								destination, overwrite)
								: Response.clientErrorPreconditionFailed());
					} else {
						return (overwrite ? moveResourceToResource(source,
								destination, overwrite)
								: Response.clientErrorPreconditionFailed());
					}
				} else {
					if (destination.parent().exists()) {
						return (overwrite ? Response.clientErrorPreconditionFailed()
								: moveResourceToCollection(source, destination,
										overwrite));
					} else {
						return clientErrorConflict();
					}
				}
			}
		} else {
			return clientErrorNotFound();
		}
	}

	@COPY
	public Response copy(@Context Request request) throws IOException {
		final Path path = Path.fromString(request.uri().getPath());
		final Resource source = resolveResource(path);

		final URI destinationUri = request.getDestination();
		final Resource destination = resolveResource(Path.fromString(destinationUri.getPath()));
		
		if (destination.isLocked()) {
			return clientErrorLocked();
		}
		
		final boolean overwrite = request.isOverwrite();
		
		if (source.exists()) {
			if (source.isCollection()) { // source exists
				if (destination.exists()) { // source exists and is a collection
					if (destination.isCollection()) {
						return (overwrite ? copyCollectionToCollection(source,
								destination, overwrite)
								: clientErrorPreconditionFailed());
					} else {
						return (overwrite ? copyCollectionToResource(source,
								destination, overwrite)
								: clientErrorPreconditionFailed());
					}
				} else {
					return (destination.parent().exists() ? copyCollectionToCollection(
							source, destination, false)
							: clientErrorPreconditionFailed());
				}
			} else {
				if (destination.exists()) { // source exists
					if (destination.isCollection()) { // source exists,
						return (overwrite ? copyResourceToCollection(source,
								destination, overwrite)
								: clientErrorPreconditionFailed());
					} else {
						return (overwrite ? copyResourceToResource(source,
								destination, overwrite)
								: clientErrorPreconditionFailed());
					}
				} else {
					if (destination.parent().exists()) {
						return (overwrite ? clientErrorPreconditionFailed()
								: copyResourceToCollection(source, destination,
										overwrite));
					} else {
						return clientErrorConflict();
					}
				}
			}
		} else {
			return clientErrorNotFound();
		}
	}

	private Response copyResourceToResource(final Resource source, final Resource destination, final boolean overwrite) throws IOException {
		source.copyTo(destination);
		return successNoContent();
	}

	private Response copyResourceToCollection(final Resource source, final Resource destination, final boolean overwrite) throws IOException {
		if (overwrite) {
			destination.delete();
		}
		source.copyTo(destination);
		return overwrite ? successNoContent() : successCreated();
	}

	private Response copyCollectionToResource(final Resource source, final Resource destination, final boolean overwrite) throws IOException {
		source.copyTo(destination);
		return successNoContent();
	}

	private Response copyCollectionToCollection(final Resource source, final Resource destination, final boolean overwrite) throws IOException {
		source.copyTo(destination);
		return successNoContent();
	}
	
	private Response moveResourceToResource(final Resource source, final Resource destination, final boolean overwrite) throws IOException {
		source.moveTo(destination);
		return successNoContent();
	}

	private Response moveResourceToCollection(final Resource source, final Resource destination, final boolean overwrite) throws IOException {
		if (overwrite) {
			destination.delete();
		}
		source.moveTo(destination);
		return overwrite ? successNoContent() : successCreated();
	}

	private Response moveCollectionToResource(final Resource source, final Resource destination, final boolean overwrite) throws IOException {
		source.moveTo(destination);
		return successNoContent();
	}

	private Response moveCollectionToCollection(final Resource source, final Resource destination, final boolean overwrite) throws IOException {
		source.moveTo(destination);
		return successNoContent();
	}
	
	@MKCOL
	public Response mkcol(@Context Request request) {
		final Path path = Path.fromString(request.uri().getPath());
		final Resource resource = resolveResource(path);
		
		if (request.hasBody()) {
			return clientErrorUnsupportedMediaType();
		} else {
			if (resource.exists()) {
				return clientErrorMethodNotAllowed();
			} else {
				if (resource.parent().exists()) {
					return (resource.mkcol() ? successCreated()
							: serverErrorInternal());
				} else {
					return clientErrorConflict();
				}
			}
		}
	}
	
	@PROPPATCH
	public Response proppatch(@Context Request request) throws IOException {
		final Path path = Path.fromString(request.uri().getPath());
		final Resource resource = resolveResource(path);
		final Iterable<QName> properties = getProperties(request.body());
		final List<RESPONSE> responses = propfind(properties, resource, request.getDepth(Depth.INFINITY));
		return successMultiStatus(multistatus(responses));
	}

	private Response successMultiStatus(MULTISTATUS multiStatus) {
		final Document doc = new Document(ProcessingInstruction.XML_DECLARATION, multiStatus);
		return new Response(Status.SUCCESS_MULTI_STATUS, ArrayListMultimap.<Header, String>create(), CHARSET_UTF_8.encode(XMLWriter.write(doc)));
	}

	@PROPFIND
	public Response propfind(@Context Request request) throws IOException {
		return propfind(Path.fromString(request.uri().getPath()), request.getDepth(Depth.INFINITY), request.body());
	}

	private Response propfind(Path path, Depth depth, ByteBuffer body) throws IOException {
		final Resource resource = resolveResource(path);

			final Iterable<QName> properties = getProperties(body);
			final List<RESPONSE> responses = propfind(properties, resource, depth);
			if (resource.exists()) {
				return successMultiStatus( multistatus(responses));
			} else {
				return clientErrorNotFound();
			}
	}

	private final List<RESPONSE> propfind(final Iterable<QName> properties, final Resource resource, final Depth depth) {
		final List<RESPONSE> responses = new ArrayList<RESPONSE>();
		
		responses.add(response(href(relativizeResource(resource)), getProperties(resource, properties)));
		if (depth != Depth.ZERO) {
			final Depth newdepth = depth.decreaseDepth();
			for (final Resource child : resource.members()) {
				responses.addAll(propfind(properties, child, newdepth));
			}
		}
		return responses;
	}

	private final List<PROPSTAT> getProperties(final Resource resource, final Iterable<QName> properties) {
		final List<PROPSTAT> propstats = new ArrayList<PROPSTAT>(2);
		final List<Element> foundProps = new ArrayList<Element>();
		final List<Element> notFoundProps = new ArrayList<Element>();
		for (final QName property : properties) {
			final Element prop = resource.getProperty(property);
			if(prop == null) {
				notFoundProps.add(new Element(property));
			} else {
				foundProps.add(prop);
			}
		}
		if(!foundProps.isEmpty()) {
			propstats.add(propertyStatus(prop(foundProps), Status.SUCCESS_OK));
		} 
		if(!notFoundProps.isEmpty()) {
			propstats.add(propertyStatus(prop(notFoundProps), Status.CLIENT_ERROR_NOT_FOUND));
		}
		return propstats;
	}

	private final Iterable<QName> getProperties(final ByteBuffer buffer) throws IOException {
		final Document doc = getPropfind(buffer);
		final Element propfind = doc.rootElement();
		final Element props = propfind.getFirstChild(Elements.PROP);
		if (props == null || !props.hasChildren()) {
			return ALL_PROPS;
		} else {
			return Iterables.transform(props.childElements(), new Function<Element, QName>() {
				@Override
				public QName apply(Element property) {
					return property.qname();
				}
			});
		}
	}

	private final Document getPropfind(final ByteBuffer buffer) throws IOException {
		XMLBuilder builder = new XMLBuilder();
		Document document = builder.build(buffer, CHARSET_UTF_8);
		LOG.debug("Request Body: "+XMLWriter.write(document));
		return document;
	}
	
	protected URI relativizeResource(Resource resource) {
		return providor.relativizeResource(resource);
	}
	
	protected Resource resolveResource(Path path) {
		return providor.resolveResource(path);
	}
}

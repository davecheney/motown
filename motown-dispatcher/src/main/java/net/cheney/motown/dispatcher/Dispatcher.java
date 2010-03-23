package net.cheney.motown.dispatcher;

import net.cheney.motown.common.api.Request;
import net.cheney.motown.common.api.Response;
import net.cheney.motown.protocol.common.HttpRequestHandler;
import net.cheney.motown.protocol.common.HttpResponseHandler;

import org.apache.log4j.Logger;

public class Dispatcher implements HttpRequestHandler {
	private static final Logger LOG = Logger.getLogger(Dispatcher.class);
	
	private final ResourceHandler dispatchable;

	public Dispatcher(ResourceHandler dispatchable) {
		this.dispatchable = dispatchable;
	}
	
	public void handleRequest(Request request, HttpResponseHandler responseHandler) {
		try {
			final Response response = dispatchable.dispatch(request);
			boolean close = request.closeRequested();
			if (!close) {
				close = response.closeRequested();
			}
			responseHandler.sendResponse(response, close);
		} catch (Throwable e) {
			LOG.error("Unable to handle request: " + request.toString(), e);
			responseHandler.sendResponse(Response.serverErrorInternal(), true);
		}
	}
	
}

package net.cheney.motown.server;

import org.apache.log4j.Logger;

import net.cheney.motown.api.Request;
import net.cheney.motown.api.Response;
import net.cheney.motown.api.Status;
import net.cheney.motown.protocol.common.HttpRequestHandler;
import net.cheney.motown.protocol.common.HttpResponseHandler;

public class DefaultRequestHandler implements HttpRequestHandler {
	
	private static final Logger LOG = Logger.getLogger(DefaultRequestHandler.class);

	@Override
	public void handleRequest(Request request, HttpResponseHandler responseHandler) {
		LOG.info(String.format("%s %s %s %s", request.method(), request.uri(), request.version(), request.headers()));
		responseHandler.sendResponse(new Response(Status.SUCCESS_NO_CONTENT), request.closeRequested());
	}

}

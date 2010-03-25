package net.cheney.motown.protocol.http.async;

import javax.annotation.Nonnull;

import org.apache.log4j.Logger;

import net.cheney.motown.common.api.Request;
import net.cheney.motown.common.api.Response;
import net.cheney.motown.server.api.Application;
import net.cheney.motown.server.api.Environment;

public class Adapter {
	private static final Logger LOG = Logger.getLogger(Adapter.class);

	private final Application application;
	private final HttpServerProtocol protocol;

	public Adapter(Application app, HttpServerProtocol protocol) {
		this.application = app;
		this.protocol = protocol;
	}
	
	public void handleRequest(@Nonnull Request req) {
		Environment env = Environment.fromRequest(req);
		try {
			Response response = application.call(env);
			boolean close = req.closeRequested();
			if (!close) {
				close = response.closeRequested();
			}
			sendResponse(response, close);
		} catch (Throwable e) {
			LOG.error("Unable to handle request: " + req.toString(), e);
			sendResponse(Response.serverErrorInternal(), true);
		}
	}

	private void sendResponse(Response response, boolean close) {
		protocol.sendResponse(response, close);
	}

}

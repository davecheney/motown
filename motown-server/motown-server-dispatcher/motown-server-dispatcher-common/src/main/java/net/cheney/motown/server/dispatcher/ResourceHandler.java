package net.cheney.motown.server.dispatcher;

import java.io.IOException;

import net.cheney.motown.common.api.Request;
import net.cheney.motown.common.api.Response;

public interface ResourceHandler {

	Response dispatch(Request request) throws IOException;

}

package net.cheney.motown.dispatcher;

import java.io.IOException;

import net.cheney.motown.api.Request;
import net.cheney.motown.api.Response;

public interface ResourceHandler {

	Response dispatch(Request request) throws IOException;

}

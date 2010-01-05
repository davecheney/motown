package net.cheney.motown.dispatcher;

import net.cheney.motown.api.Request;
import net.cheney.motown.api.Response;

public interface ResourceMethod {

	Response invoke(Object resource, Request request);

}

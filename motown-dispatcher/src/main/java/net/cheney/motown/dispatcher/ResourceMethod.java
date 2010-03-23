package net.cheney.motown.dispatcher;

import net.cheney.motown.common.api.Request;
import net.cheney.motown.common.api.Response;

public interface ResourceMethod {

	Response invoke(Object resource, Request request);

}

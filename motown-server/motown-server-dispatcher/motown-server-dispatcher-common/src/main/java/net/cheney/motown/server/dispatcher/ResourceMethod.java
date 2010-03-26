package net.cheney.motown.server.dispatcher;

import net.cheney.motown.common.api.Response;
import net.cheney.motown.server.api.Environment;

public interface ResourceMethod {

	Response invoke(Object resource, Environment env);

}

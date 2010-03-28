package net.cheney.motown.mvn.dispatcher;

import net.cheney.motown.common.api.Response;
import net.cheney.motown.server.api.Environment;

public interface ResourceMethod {

	Response invoke(Object resource, Environment env);

}

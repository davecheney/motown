package net.cheney.motown.dispatcher;

import net.cheney.motown.api.Message;
import net.cheney.motown.api.Response;

public interface ResourceMethod {

	Response invoke(Object resource, Message request);

}

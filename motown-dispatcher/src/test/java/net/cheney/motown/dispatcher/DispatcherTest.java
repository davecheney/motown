package net.cheney.motown.dispatcher;

import static net.cheney.motown.common.api.Version.HTTP_1_1;
import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;

import net.cheney.motown.common.api.Message;
import net.cheney.motown.common.api.Request;
import net.cheney.motown.common.api.Response;
import net.cheney.motown.common.api.Status;
import net.cheney.motown.common.api.Version;
import net.cheney.motown.common.api.Message.Method;
import net.cheney.motown.dispatcher.dynamic.DynamicResourceHandler;
import net.cheney.motown.dispatcher.dynamic.GET;
import net.cheney.motown.protocol.common.HttpResponseHandler;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.varia.NullAppender;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DispatcherTest {

	private static ResourceFactory factory;
	private DynamicResourceHandler handler;
	private Dispatcher dispatcher;
	private TestResponseHandler responseHandler;
	
	public static class Get {
		@GET
		public Message get() {
			return Response.successNoContent();
		}
	}
	
	public static class TestResponseHandler implements HttpResponseHandler {

		private Response response;

		@Override
		public void sendResponse(Response response, boolean close) {
			this.setResponse(response);
		}

		public void setResponse(Response response) {
			this.response = response;
		}

		public Response getResponse() {
			return response;
		}
		
	}
	
	@BeforeClass
	public static void init() {
		BasicConfigurator.configure(new NullAppender());
		factory = ResourceFactory.factoryForResource(new Get());
	}
	
	@Before
	public void setup() {
		this.handler = new DynamicResourceHandler(factory);
		this.dispatcher = new Dispatcher(handler);
		this.responseHandler = new TestResponseHandler();
	}
	
	@Test
	public void testGET() throws URISyntaxException {
		Request request = new Request(Method.GET, "/", HTTP_1_1);
		dispatcher.handleRequest(request, responseHandler);
		Response response = responseHandler.getResponse();
		
		assertEquals(Status.SUCCESS_NO_CONTENT, response.status());
	}
	
	@Test
	public void testNotImplemented() throws URISyntaxException {
		Request request = new Request(Method.PUT, "/", HTTP_1_1);
		dispatcher.handleRequest(request, responseHandler);
		Response response = responseHandler.getResponse();
		
		assertEquals(Status.SERVER_ERROR_NOT_IMPLEMENTED, response.status());
	}
}

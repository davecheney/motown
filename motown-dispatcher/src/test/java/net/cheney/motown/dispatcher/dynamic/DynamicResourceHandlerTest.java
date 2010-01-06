package net.cheney.motown.dispatcher.dynamic;

import static net.cheney.motown.api.Version.HTTP_1_1;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;

import net.cheney.motown.api.Message;
import net.cheney.motown.api.Method;
import net.cheney.motown.api.Request;
import net.cheney.motown.api.Response;
import net.cheney.motown.api.Status;
import net.cheney.motown.dispatcher.ResourceFactory;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DynamicResourceHandlerTest {

	private static ResourceFactory factory;
	private DynamicResourceHandler handler;
	
	@BeforeClass
	public static void init() {
		factory = ResourceFactory.factoryForResource(new Object() {
			
			@SuppressWarnings("unused")
			@GET
			public Message get() {
				return Response.successNoContent();
			}
		});
	}
	
	@Before
	public void setup() {
		this.handler = new DynamicResourceHandler(factory);
	}
	
	@Test
	public void testGET() throws URISyntaxException {
		Request request = new Request(Method.GET, "/", HTTP_1_1);
		Response response = handler.dispatch(request);
		
		assertTrue(response.status().equals(Status.SUCCESS_NO_CONTENT));
	}
	
	@Test
	public void testNotImplemented() throws URISyntaxException {
		Request request = new Request(Method.PUT, "/", HTTP_1_1);
		Response response = handler.dispatch(request);
		
		assertTrue(response.status().equals(Status.SERVER_ERROR_NOT_IMPLEMENTED));
	}
}

package net.cheney.motown.dispatcher.dynamic;

import static org.junit.Assert.assertTrue;

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
			
			@GET
			public Response get() {
				return Response.successNoContent();
			}
		});
	}
	
	@Before
	public void setup() {
		this.handler = new DynamicResourceHandler(factory);
	}
	
	@Test
	public void testGET() {
		Request request = Request.builder(Method.GET, "/").build();
		Response response = handler.dispatch(request);
		
		assertTrue(response.status().equals(Status.SUCCESS_NO_CONTENT));
	}
	
	@Test
	public void testNotFound() {
		Request request = Request.builder(Method.PUT, "/").build();
		Response response = handler.dispatch(request);
		
		assertTrue(response.status().equals(Status.SERVER_ERROR_NOT_IMPLEMENTED));
	}
}

package net.cheney.motown.dispatcher.dynamic;

import static org.junit.Assert.assertTrue;

import java.net.URI;

import net.cheney.motown.api.Method;
import net.cheney.motown.api.Request;
import net.cheney.motown.api.Response;
import net.cheney.motown.api.Status;
import net.cheney.motown.dispatcher.SingletonResourceFactory;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DynamicResourceHandlerTest {

	static class DynamicController {
		
		@GET
		public Response get() {
			return Response.successNoContent();
		}
	}

	private static SingletonResourceFactory factory;
	private DynamicResourceHandler handler;
	
	@BeforeClass
	public static void init() {
		factory = new SingletonResourceFactory(new DynamicController());
	}
	
	@Before
	public void setup() {
		this.handler = new DynamicResourceHandler(factory);
	}
	
	@Test
	public void testDynamicDispatch() {
		Request request = Request.builder(Method.GET, "/").build();
		Response response = handler.dispatch(request);
		
		assertTrue(response.status().equals(Status.SUCCESS_NO_CONTENT));
	}
}

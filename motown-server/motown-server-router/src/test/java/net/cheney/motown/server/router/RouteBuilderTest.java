package net.cheney.motown.server.router;

import static java.util.regex.Pattern.compile;
import static net.cheney.motown.server.api.Path.fromString;

import java.util.regex.Pattern;

import net.cheney.motown.server.api.NamedIntegerParameter;
import net.cheney.motown.server.api.Parameter;

import org.junit.Test;

public class RouteBuilderTest {
	
	private class PostsController { };
	
	private class AdminPostsController { };
	
	private class CommentsController { };
	
	private static final Parameter<?> ID = new NamedIntegerParameter("id");

	@Test public void testRouteBuilderSimple() {
		Router router = Router.builder().build();
	}
	
	@Test public void testRouteBuilderWithRootContext() {
		Router r = Router.builder().rootContent().build();
		Router p = Router.builder().rootContent().context(fromString("foo")).build();
		Router q = Router.builder().context(fromString("bar")).context(fromString("baz")).build();
	}
	
	@Test public void testRouteBuilderWithRootContextAndPaths() {
		Router r = Router.builder()
			.rootContent()
				.serve(fromString("posts")).with(PostsController.class)
				.serve(fromString("admin/posts")).with(AdminPostsController.class)
				.serve(compile("^/posts/\\d+$")).with(PostsController.class, ID)
				.done()
			.context(fromString("comments"))
				.serve(compile("^\\d+$")).with(CommentsController.class, ID)
				.done()
			.build();
	}
}

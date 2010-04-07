package net.cheney.motown.server.router;

public interface RootContextBuilder extends RouteBuilder, ContextBuilder {

	ContextBuilder rootContent();
}

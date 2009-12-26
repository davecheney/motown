package net.cheney.motown.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.log4j.BasicConfigurator;

import net.cheney.motown.protocol.http.async.HttpServerProtocolFactory;
import net.cheney.rev.reactor.Reactor;

public class DefaultServer {

	public static void main(String[] args) throws IOException {
		BasicConfigurator.configure();
		Reactor reactor = Reactor.open();
		reactor.listen(new InetSocketAddress(8080), new HttpServerProtocolFactory(new DefaultRequestHandler()));
	}

}

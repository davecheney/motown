package net.cheney.motown.webservice.controller.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import net.cheney.motown.api.MimeType;
import net.cheney.motown.api.Request;
import net.cheney.motown.api.Response;
import net.cheney.motown.dispatcher.dynamic.Context;
import net.cheney.motown.dispatcher.dynamic.GET;

public class StaticFileController {

	private final File root;

	public StaticFileController(File root) {
		this.root = root;
	}
	
	@GET Response doGet(@Context Request request) throws IOException {
		final File path = new File(root, request.uri().getPath());
		final FileChannel channel = new FileInputStream(path).getChannel();
		final ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
		return Response.success(MimeType.APPLICATION_OCTET_STREAM, buffer);
	}
}

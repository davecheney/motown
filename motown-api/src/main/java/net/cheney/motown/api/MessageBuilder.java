package net.cheney.motown.api;

public class MessageBuilder {

	public static ResponseBuilder newResponse(Status status) {
		return new ResponseBuilder(status);
	}
	
	public static class ResponseBuilder {

		private final StatusLine statusLine;

		public ResponseBuilder(Status status) {
			this.statusLine = new StatusLine(Version.HTTP_1_1, status);
		}
		
		public Response build() {
			return new Response(statusLine.status()) {};
		}
		
	}
}

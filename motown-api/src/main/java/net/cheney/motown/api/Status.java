package net.cheney.motown.api;

import javax.annotation.Nonnull;

public enum Status {
		INFO_CONTINUE(100, "Continue"), 
		INFO_SWITCHING_PROTOCOL(101, "Switching Protocols"), 
		INFO_PROCESSING(102, "Processing"),

		SUCCESS_OK(200, "OK"), 
		SUCCESS_CREATED(201, "Created"), 
		SUCCESS_ACCEPTED(202, "Accepted"), 
		SUCCESS_NON_AUTHORITATIVE(203, "Non-Authoritative Information"), 
		SUCCESS_NO_CONTENT(204, "No Content"), 
		SUCCESS_RESET_CONTENT(205, "Reset Content"), 
		SUCCESS_PARTIAL_CONTENT(206, "Partial Content"), 
		SUCCESS_MULTI_STATUS(207, "Multi-Status"),

		REDIRECTION_MULTIPLE_CHOICES(300, "Multiple Choices"), 
		REDIRECTION_MOVED_PERMANENTLY(301, "Moved Permanently"), 
		REDIRECTION_MOVED_TEMPORARILY(302, "Found"), 
		REDIRECTION_SEE_OTHER(303, "See Other"), 
		REDIRECTION_NOT_MODIFIED(304, "Not Modified"), 
		REDIRECTION_USE_PROXY(305, "Use Proxy"), 
		REDIRECTION_TEMPORARY_REDIRECT(307, "Temporary Redirect"),

		CLIENT_ERROR_BAD_REQUEST(400, "Bad Request"), 
		CLIENT_ERROR_UNAUTHORIZED(401, "Unauthorized"), 
		CLIENT_ERROR_PAYMENT_REQUIRED(402, "Payment Required"), 
		CLIENT_ERROR_FORBIDDEN(403, "Forbidden"), 
		CLIENT_ERROR_NOT_FOUND(404, "Not found"), 
		CLIENT_ERROR_METHOD_NOT_ALLOWED(405, "Method Not Allowed"), 
		CLIENT_ERROR_NOT_ACCEPTABLE(406, "Not Acceptable"), 
		CLIENT_ERROR_PROXY_AUTHENTIFICATION_REQUIRED(407, "Proxy Authentication Required"), 
		CLIENT_ERROR_REQUEST_TIMEOUT(408, "Request Time-out"), 
		CLIENT_ERROR_CONFLICT(409, "Conflict"), 
		CLIENT_ERROR_GONE(410, "Gone"), 
		CLIENT_ERROR_LENGTH_REQUIRED(411, "Length Required"), 
		CLIENT_ERROR_PRECONDITION_FAILED(412, "Precondition Failed"), 
		CLIENT_ERROR_REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"), 
		CLIENT_ERROR_REQUEST_URI_TOO_LONG(414, "Request-URI Too Large"), 
		CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"), 
		CLIENT_ERROR_REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested range not satisfiable"), 
		CLIENT_ERROR_EXPECTATION_FAILED(417, "Expectation Failed"), 
//		CLIENT_ERROR_UNPROCESSABLE_ENTITY(422), 
		CLIENT_ERROR_LOCKED(423, "Locked"), 
//		CLIENT_ERROR_FAILED_DEPENDENCY(424),

		SERVER_ERROR_INTERNAL(500, "Internal Server Error"), 
		SERVER_ERROR_NOT_IMPLEMENTED(501, "Not Implemented"), 
		SERVER_ERROR_BAD_GATEWAY(502, "Bad Gateway"), 
		SERVER_ERROR_SERVICE_UNAVAILABLE(503, "Service Unavailable"), 
		SERVER_ERROR_GATEWAY_TIMEOUT(504, "Gateway Time-out"), 
		SERVER_ERROR_HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version not supported");
//		SERVER_ERROR_INSUFFICIENT_STORAGE(507);

		private final int code;
		private final String reason;

		private Status(@Nonnull int code, @Nonnull String reason) {
			this.code = code;
			this.reason = reason;
		}

		public int code() {
			return code;
		}

		public String reason() {
			return reason;
		}
		
		public boolean isInformational() {
			return (compareTo(INFO_CONTINUE) >= 0 && compareTo(SUCCESS_OK) < 0);
		}
		
		public boolean isSuccess() {
			return (compareTo(SUCCESS_OK) >= 0 && compareTo(REDIRECTION_MULTIPLE_CHOICES) < 0);
		}
		
		public boolean isRedirect() {
			return (compareTo(REDIRECTION_MULTIPLE_CHOICES) >= 0 && compareTo(CLIENT_ERROR_BAD_REQUEST) < 0);
		}
		
		public boolean isError() {
			return (compareTo(CLIENT_ERROR_BAD_REQUEST) >= 0);
		}
		
		public boolean isClientError() {
			return (compareTo(CLIENT_ERROR_BAD_REQUEST) >= 0 && compareTo(SERVER_ERROR_INTERNAL) < 0);
		}
		
		public boolean isServerError() {
			return (compareTo(SERVER_ERROR_INTERNAL) >= 0);
		}
		
	}
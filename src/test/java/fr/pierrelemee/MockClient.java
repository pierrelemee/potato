package fr.pierrelemee;


import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;

public class MockClient {

    protected WebApplication app;

    public MockClient(WebApplication app) {
        this.app = app;
    }

    public MockExchange get(String path) throws IOException {
        MockExchange exchange = new MockExchange();

        exchange.uri = URI.create(path);
        exchange.method = HttpMethod.GET.name();

        this.app.handle(exchange);

        exchange.getResponseBody().flush();

        return exchange;
    }

    public MockExchange post(String path) throws IOException {
        MockExchange exchange = new MockExchange();

        exchange.uri = URI.create(path);
        exchange.method = HttpMethod.POST.name();

        this.app.handle(exchange);

        exchange.getResponseBody().flush();

        return exchange;
    }

    public static class MockExchange extends HttpExchange {

        protected String method;
        protected int status;
        protected long size;
        protected URI uri;
        protected Headers requestHeaders;
        protected Headers responseHeaders;
        protected ByteArrayOutputStream responseBody;

        public MockExchange() {
            this.responseHeaders = new Headers();
            this.responseBody = new ByteArrayOutputStream();
        }

        @Override
        public Headers getRequestHeaders() {
            return null;
        }

        @Override
        public Headers getResponseHeaders() {
            return this.requestHeaders;
        }

        @Override
        public URI getRequestURI() {
            return this.uri;
        }

        @Override
        public String getRequestMethod() {
            return this.method;
        }

        @Override
        public HttpContext getHttpContext() {
            return null;
        }

        @Override
        public void close() {

        }

        @Override
        public InputStream getRequestBody() {
            return null;
        }

        @Override
        public OutputStream getResponseBody() {
            return this.responseBody;
        }

        public String getResponseBodyString() {
            return new String(this.responseBody.toByteArray());
        }

        @Override
        public void sendResponseHeaders(int status, long size) throws IOException {
            this.status = status;
            this.size = size;
        }

        @Override
        public InetSocketAddress getRemoteAddress() {
            return null;
        }

        @Override
        public int getResponseCode() {
            return this.status;
        }

        @Override
        public InetSocketAddress getLocalAddress() {
            return null;
        }

        @Override
        public String getProtocol() {
            return null;
        }

        @Override
        public Object getAttribute(String s) {
            return null;
        }

        @Override
        public void setAttribute(String s, Object o) {

        }

        @Override
        public void setStreams(InputStream inputStream, OutputStream outputStream) {

        }

        @Override
        public HttpPrincipal getPrincipal() {
            return null;
        }
    }
}

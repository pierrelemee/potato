package fr.pierrelemee;


public class WebRequest {

    /*
    private static final String POST = "POST";

    private final String method;
    private final String uri;
    private final Map<String, String> variables;
    private final Map<String, List<String>> get;
    private final Map<String, List<String>> post;
    private Map<String, String> headers;
    private Map<String, String> cookies;
    private final Optional<ByteString> payload;

    public WebRequest(String uri) {
        this(uri, RequestValue.GET);
    }

    public WebRequest(String uri, String method) {
        this(uri, method, null);
    }

    public WebRequest(String uri, String method, ByteString payload) {
        this(uri, method, payload, Collections.emptyMap());
    }

    public WebRequest(String uri, String method, ByteString payload, Map<String, String> headers) {
        this(uri, Collections.emptyMap(), method, payload, headers);
    }

    public WebRequest(String uri, Map<String, String> variables, String method, ByteString payload, Map<String, String> headers) {
        this.method = method;
        this.uri = uri;
        this.variables = variables;
        this.payload = Optional.of(payload != null ? payload : ByteString.EMPTY);
        if (method.equalsIgnoreCase(POST)) {
            this.post = new QueryStringDecoder(this.payload().isPresent() ? this.payload().get().utf8() : "", false).parameters();
            this.get = Collections.emptyMap();
        } else {
            this.get = new QueryStringDecoder(this.uri).parameters();
            this.post = Collections.emptyMap();
        }
        this.headers = headers;
        this.cookies = new HashMap<>();

        if (this.header("Cookie").isPresent()) {
            for (String cookie: this.header("Cookie").get().split(";")) {
                cookie = cookie.trim();
                int index = cookie.indexOf('=');
                if (index >= 0) {
                    this.cookies.put(cookie.substring(0, index), cookie.substring(index + 1));
                }
            }
        }
    }

    @Override
    public String method() {
        return this.method;
    }

    @Override
    public String uri() {
        return this.uri;
    }

    public Map<String, String> variables() {
        return this.variables;
    }
    public Map<String, List<String>> get() {
        return this.get;
    }
    public Map<String, List<String>> post() {
        return this.post;
    }

    public Map<String, String> cookies() {
        return this.cookies;
    }

    public boolean hasCookie(String name) {
        return this.cookies.containsKey(name);
    }

    public String getCookie(String name) {
        return this.cookies.get(name);
    }

    @Override
    public Map<String, List<String>> parameters() {
        return this.method.equalsIgnoreCase(POST) ? this.post : this.get;
    }

    @Override
    public Map<String, String> headers() {
        return this.headers;
    }

    @Override
    public Optional<String> service() {
        return null;
    }

    @Override
    public Optional<ByteString> payload() {
        return this.payload;
    }

    @Override
    public WebRequest withUri(String uri) {
        return new WebRequest(uri);
    }

    @Override
    public WebRequest withService(String service) {
        return null;
    }

    @Override
    public WebRequest withHeader(String name, String value) {
        WebRequest request = new WebRequest(this.uri(), this.method());
        request.headers.put(name, value);
        return request;
    }

    @Override
    public WebRequest withHeaders(Map<String, String> additionalHeaders) {
        return null;
    }

    @Override
    public WebRequest clearHeaders() {
        return new WebRequest(this.uri(), this.method());
    }

    @Override
    public WebRequest withPayload(ByteString payload) {
        return new WebRequest(uri(), method(), payload);
    }
    */
}

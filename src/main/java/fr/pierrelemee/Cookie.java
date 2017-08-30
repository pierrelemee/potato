package fr.pierrelemee;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.Locale;

public class Cookie {

    private static final DateTimeFormatter EXPIRES_DATE_FORMAT = DateTimeFormatter.ofPattern("EEE, dd-MMM-yyyy HH:mm:ss zzz", Locale.ENGLISH);

    protected String key;
    protected String value;
    protected ZonedDateTime expires;
    protected String domain;
    protected String path;
    protected boolean secure = false;
    protected boolean httpOnly = false;

    Cookie(String key) {
        this(key, null);
    }

    Cookie(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getDomain() {
        return domain;
    }

    public String getPath() {
        return path;
    }

    public String getHeader() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.key);

        if (this.hasValue()) {
            buffer.append("=");
            buffer.append(this.value);
        }

        buffer.append(";");

        if (this.domain != null && !this.domain.trim().isEmpty()) {
            buffer.append(" Domain=");
            buffer.append(this.domain);
            buffer.append(";");
        }

        if (this.path != null && !this.path.trim().isEmpty()) {
            buffer.append(" Path=");
            buffer.append(this.path);
            buffer.append(";");
        }

        if (this.expires != null && this.expires.compareTo(ZonedDateTime.now(ZoneId.systemDefault())) > 0) {
            buffer.append(" Expires=");
            buffer.append(EXPIRES_DATE_FORMAT.format(this.expires));
            buffer.append(";");
        }

        if (this.secure) {
            buffer.append(" Secure;");
        }

        if (this.httpOnly) {
            buffer.append(" HttpOnly;");
        }

        return buffer.toString();
    }

    public boolean hasValue() {
        return this.value != null;
    }

    public String getValue() {
        return this.value;
    }

    public static class Builder {

        protected Cookie cookie;

        private Builder(String key) {
            this.cookie = new Cookie(key);
        }

        public Builder setValue(String value) {
            this.cookie.value = value;

            return this;
        }

        public Builder setExpires(int duration, TemporalUnit unit) {
            this.cookie.expires = ZonedDateTime.now(ZoneId.of("UTC")).plus(duration, unit);

            return this;
        }

        public Builder setDomain(String domain) {
            this.cookie.domain = domain;

            return this;
        }

        public Builder setPath(String path) {
            this.cookie.path = path;

            return this;
        }

        public Builder setSecure() {
            this.cookie.secure = true;

            return this;
        }

        public Builder setHttpOnly() {
            this.cookie.httpOnly = true;

            return this;
        }

        public Cookie build() {
            return this.cookie;
        }

        public static Builder create(String key) {
            return new Builder(key);
        }
    }
}

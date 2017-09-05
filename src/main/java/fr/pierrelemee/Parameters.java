package fr.pierrelemee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Parameters {

    protected Map<String, List<String>> values;

    public Parameters() {
        this(Collections.emptyMap());
    }

    public Parameters(Map<String, List<String>> values) {
        this.values = new LinkedHashMap<>();
        this.values.putAll(values);
    }

    public Map<String, List<String>> getValues() {
        return values;
    }

    public boolean has(String key) {
        return this.values.containsKey(key);
    }

    public boolean isScalar(String key) {
        return this.values.containsKey(key) && this.values.get(key).size() == 1;
    }

    public boolean isList(String key) {
        return this.values.containsKey(key) && this.values.get(key).size() > 1;
    }

    public String get(String key) {
        return this.get(key, null);
    }

    public String get(String key, String def) {
        return this.values.containsKey(key) ? this.values.get(key).get(0) : def;
    }

    public List<String> getList(String key) {
        return this.values.containsKey(key) ? this.values.get(key) : Collections.emptyList();
    }

    public Integer getInteger(String key) {
        return this.getInteger(key, 0);
    }

    public Integer getInteger(String key, Integer def) {
        try {
            return this.values.containsKey(key) ? Integer.parseInt(this.get(key)) : def;
        } catch (NumberFormatException nfe) {
            return def;
        }
    }

    public Long getLong(String key) {
        return this.getLong(key, 0L);
    }

    public Long getLong(String key, Long def) {
        try {
            return this.values.containsKey(key) ? Long.parseLong(this.get(key)) : def;
        } catch (NumberFormatException nfe) {
            return def;
        }
    }

    public Float getFloat(String key) {
        return this.getFloat(key, (float)0.);
    }

    public Float getFloat(String key, Float def) {
        try {
            return this.values.containsKey(key) ? Float.parseFloat(this.get(key)) : def;
        } catch (NumberFormatException nfe) {
            return def;
        }
    }

    public Double getDouble(String key) {
        return this.getDouble(key, 0.);
    }

    public Double getDouble(String key, Double def) {
        try {
            return this.values.containsKey(key) ? Double.parseDouble(this.get(key)) : def;
        } catch (NumberFormatException nfe) {
            return def;
        }
    }

    public static Parameters fromInputStream(InputStream input) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder buffer = new StringBuilder();

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            return fromString(buffer.toString());

        } catch (IOException e) {
            return new Parameters();
        }
    }

    public static Parameters fromString(String query) {
        Map<String, List<String>> parameters = new LinkedHashMap<>();

        if (query != null && !query.isEmpty()) {
            int index;
            for (String parameter : query.split("&")) {
                index = parameter.indexOf('=');
                if (index > -1) {
                    parameters.put(parameter.substring(0, index), Collections.singletonList(parameter.substring(index + 1)));
                } else {
                    parameters.put(parameter, Collections.singletonList("true"));
                }
            }
        }

        return new Parameters(parameters);
    }

    public static Parameters empty() {
        return new Parameters();
    }
}

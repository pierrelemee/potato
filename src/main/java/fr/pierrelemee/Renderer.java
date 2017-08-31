package fr.pierrelemee;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Renderer {

    public abstract String render(RenderTemplate template);

    public String render(String resource) {
        return this.render(resource, Collections.emptyMap());
    }

    public String render(String resource, Parameter ... parameters) {
        return this.render(resource, Arrays.stream(parameters).filter(parameter -> parameter.value != null).collect(Collectors.toMap(Parameter::getName, p -> p.value)));
    }

    public abstract String render(String resource, Map<String, Object> parameters);

    public static class Parameter {

        private Parameter(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        protected String name;
        protected Object value;

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }

        public static Parameter of(String name, Object value) {
            return new Parameter(name, value);
        }
    }
}

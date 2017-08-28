package fr.pierrelemee;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ControllerRequestProcess implements RequestProcess {

    protected Controller controller;
    protected Method method;
    protected int indexRequest;

    public ControllerRequestProcess(Controller controller, Method method) {
        this.controller = controller;
        this.method = method;
        this.indexRequest = -1;
        int index = 0;

        for (Parameter parameter: this.method.getParameters()) {
            if (parameter.getType().equals(WebRequest.class)) {
                this.indexRequest = index;
            }
            index++;
        }
    }

    @Override
    public WebResponse process(WebRequest request) throws Exception {
        Object[] parameters = new Object[this.method.getParameterCount()];
        if (this.indexRequest > -1) {
            parameters[this.indexRequest] = request;
        }

        return (WebResponse) this.method.invoke(this.controller, parameters);
    }
}

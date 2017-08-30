package fr.pierrelemee;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ControllerRequestProcess implements RequestProcess {

    protected Controller controller;
    protected Method method;
    protected int indexRequest;
    protected int indexSession;

    public ControllerRequestProcess(Controller controller, Method method) {
        this.controller = controller;
        this.method = method;
        this.indexRequest = -1;
        this.indexSession = -1;
        int index = 0;

        for (Parameter parameter : this.method.getParameters()) {
            if (parameter.getType().equals(WebRequest.class)) {
                this.indexRequest = index;
            }
            if (parameter.getType().isAssignableFrom(Session.class)) {
                this.indexSession = index;
            }
            index++;
        }
    }

    @Override
    public WebResponse process(WebRequest request, Session session) throws Exception {
        Object[] parameters = new Object[this.method.getParameterCount()];
        if (this.indexRequest > -1) {
            parameters[this.indexRequest] = request;
        }

        if (this.indexSession > -1) {
            parameters[this.indexRequest] = session;
        }

        return (WebResponse) this.method.invoke(this.controller, parameters);
    }
}

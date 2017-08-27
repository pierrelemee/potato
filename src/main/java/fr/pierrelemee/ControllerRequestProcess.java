package fr.pierrelemee;

import java.lang.reflect.Method;

public class ControllerRequestProcess implements RequestProcess {

    protected Controller controller;
    protected Method method;

    public ControllerRequestProcess(Controller controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    @Override
    public WebResponse process() throws Exception {
        return (WebResponse) this.method.invoke(this.controller);
    }
}

package org.example.streamresponsenetty.event;

import org.example.streamresponsenetty.dto.AppResponse;
import org.springframework.context.ApplicationEvent;

public class CustomEvent extends ApplicationEvent {
    private final AppResponse appResponse;

    public CustomEvent(Object source, AppResponse appResponse) {
        super(source);  // Calling the superclass constructor (ApplicationEvent)
        this.appResponse = appResponse;
    }

    public AppResponse getAppResponse() {
        return appResponse;
    }
}

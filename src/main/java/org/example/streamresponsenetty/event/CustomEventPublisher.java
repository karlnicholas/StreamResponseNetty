package org.example.streamresponsenetty.event;

import lombok.extern.slf4j.Slf4j;
import org.example.streamresponsenetty.dto.AppResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
@Slf4j
public class CustomEventPublisher {

    private final List<Consumer<CustomEvent>> listeners = new ArrayList<>();

    // Method to publish events
    public void publishEvent(AppResponse message) {
        log.info(message.toString());
        CustomEvent customEvent = new CustomEvent(this, message);
        notifyListeners(customEvent);
    }

    // Method to register event listeners
    public void registerListener(Consumer<CustomEvent> listener) {
        listeners.add(listener);
    }

    // Notify all registered listeners
    private void notifyListeners(CustomEvent event) {
        for (Consumer<CustomEvent> listener : listeners) {
            listener.accept(event);
        }
    }
}

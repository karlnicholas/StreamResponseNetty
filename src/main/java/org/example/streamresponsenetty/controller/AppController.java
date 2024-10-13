package org.example.streamresponsenetty.controller;

import lombok.RequiredArgsConstructor;
import org.example.streamresponsenetty.dto.AppResponse;
import org.example.streamresponsenetty.event.CustomEvent;
import org.example.streamresponsenetty.event.CustomEventPublisher;
import org.example.streamresponsenetty.service.AppService;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequiredArgsConstructor
public class AppController {

    private final AppService appService;
    private final CustomEventPublisher eventPublisher;
    private final ObjectMapper objectMapper; // For JSON conversion

    @GetMapping("/stream-events")
    public Mono<Void> streamCustomEvents(ServerHttpResponse response) {
        // Set response headers for streaming events
        response.getHeaders().setContentType(MediaType.TEXT_EVENT_STREAM); // Use SSE content type

        // Create a Flux to stream events and respond with AppResponse instances
        Flux<DataBuffer> dataBufferFlux = Flux.create(sink -> {
            AtomicBoolean first = new AtomicBoolean(false);
            eventPublisher.registerListener(event -> {
                if (event instanceof CustomEvent) {
                    AppResponse appResponse = ((CustomEvent) event).getAppResponse();
                    try {
                        // Convert AppResponse object to JSON
                        String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(appResponse.getData());  // Now serializing AppResponseData
                        byte[] bytes = createJsonChunk(first, appResponse, jsonResponse);
                        DataBuffer buffer = response.bufferFactory().wrap(bytes);
                        sink.next(buffer);  // Emit the DataBuffer for each AppResponse
                    } catch (Exception e) {
                        sink.error(e); // Handle serialization errors
                    }
                }
            });

            // Call the non-reactive AppService method to start publishing events
            appService.getAppResponse();

            // Complete the Flux after AppService finishes
            sink.complete();
        });

        // Write the data buffer and flush immediately for each event
        return response.writeAndFlushWith(dataBufferFlux.map(Mono::just));
    }

    // Helper method to create the JSON chunk
    private byte[] createJsonChunk(AtomicBoolean first, AppResponse appResponse, String jsonResponse) {
        String chunk;
        if (!first.get()) {
            // First item in the array
            chunk = appResponse.isLast() ? "[\n" + jsonResponse + "\n" : "[\n" + jsonResponse + ",\n";
            first.set(true);
        } else {
            // Subsequent items in the array
            chunk = appResponse.isLast() ? jsonResponse + "\n]\n" : jsonResponse + ",\n";
        }
        return chunk.getBytes(StandardCharsets.UTF_8);
    }
}

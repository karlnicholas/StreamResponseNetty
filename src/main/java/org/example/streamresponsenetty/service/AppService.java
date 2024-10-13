package org.example.streamresponsenetty.service;

import lombok.RequiredArgsConstructor;
import org.example.streamresponsenetty.dto.AppResponse;
import org.example.streamresponsenetty.dto.AppResponseData;
import org.example.streamresponsenetty.event.CustomEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppService {
    private final CustomEventPublisher customEventPublisher;

    public void getAppResponse() {
        int count = 5;
        for (int i = 0; i < count; ++i) {
            try {
                Thread.sleep(1000); // Simulate delay
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            AppResponse appResponse = AppResponse.builder()
                    .last(i == count - 1)
                    .data(AppResponseData.builder()
                            .status(i)
                            .message("Any Message")
                            .build())
                    .build();

            customEventPublisher.publishEvent(appResponse);
        }
    }
}

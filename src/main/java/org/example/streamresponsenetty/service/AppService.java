package org.example.streamresponsenetty.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.streamresponsenetty.dto.AppResponse;
import org.example.streamresponsenetty.dto.AppResponseData;
import org.example.streamresponsenetty.event.CustomEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppService {
    private final CustomEventPublisher customEventPublisher;

    public void getAppResponse() {
        int count = 5;
        for (int i = 0; i < count; ++i) {
            try {
                Thread.sleep(1000); // Simulate delay
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                /* Clean up whatever needs to be handled before interrupting  */
                Thread.currentThread().interrupt();
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

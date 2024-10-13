package org.example.streamresponsenetty.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppResponseData {
    private int status;
    private String message;
}

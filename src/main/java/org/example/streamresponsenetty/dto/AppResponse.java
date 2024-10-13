package org.example.streamresponsenetty.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppResponse {
    private boolean last;
    private AppResponseData data;
}

package org.example.streamresponsenetty.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class AppResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 7882722038842972768L;
    private boolean last;
    private AppResponseData data;
}

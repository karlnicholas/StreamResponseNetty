package org.example.streamresponsenetty.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class AppResponseData implements Serializable {
    @Serial
    private static final long serialVersionUID = 8810997945119041746L;
    private int status;
    private String message;
}

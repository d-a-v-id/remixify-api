package org.dvd.remixifyapi.email.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailResponseDto {

    private String messageId;
    private String status;
    private String recipient;
    
}
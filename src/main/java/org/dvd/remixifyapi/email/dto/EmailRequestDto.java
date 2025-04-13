package org.dvd.remixifyapi.email.dto;

import lombok.Data;

@Data
public class EmailRequestDto {

    private String to;
    private String subject;
    private String html;

}

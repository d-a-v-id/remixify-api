package org.dvd.remixifyapi.email.dto;

import lombok.Data;

@Data
public class ContactFormRequest {

    private String name;
    private String email;
    private String subject;
    private String message;

}

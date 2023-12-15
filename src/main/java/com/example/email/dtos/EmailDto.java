package com.example.email.dtos;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class EmailDto {

    @NotBlank
    private String ownerRef;
    @NotBlank
    @Email
    private String emailFrom  = "ricardo@dtmm.com.br";
    @NotBlank
    @Email
    private String emailTo;
    @NotBlank
    private String phone;
    @NotBlank
    private String subject = "Contato pelo site";
    @NotBlank
    private String text;

}

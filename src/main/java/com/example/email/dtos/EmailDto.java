package com.example.email.dtos;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class EmailDto {

    @NotBlank
    private String ownerRef;
    @NotBlank
    @Email
    private String emailFrom  = "ricardo@dtmm.com.br";
    @NotBlank
    @Email
    private String emailTo = "ricardo@colorsbee.com.br";
    @NotBlank
    @Email
    private String emailTo2;
    @NotBlank
    private String phone;
    @NotBlank
    private String subject = "Contato pelo site";
    @NotBlank
    private String text;

}

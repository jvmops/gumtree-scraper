package com.jvmops.gumtree.city;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class EmailDto {
    @Email
    private String value;
}

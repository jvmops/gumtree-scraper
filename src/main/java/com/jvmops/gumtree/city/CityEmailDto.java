package com.jvmops.gumtree.city;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CityEmailDto {
    @NotEmpty
    private String city;
    private String email;
}

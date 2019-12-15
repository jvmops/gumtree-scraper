package com.jvmops.gumtree.city;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CityEmailDto {
    @NotEmpty
    private String city;
    private String email;
}

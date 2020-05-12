package com.jvmops.gumtree.subscriptions.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {
    @NotEmpty
    private String city;
    @Email
    @NotEmpty
    private String email;
}

package com.livetalks.bookstore.model.receive.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpModel {

    @NotBlank(message = "Firstname cannot be empty.")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "Lastname cannot be empty.")
    @JsonProperty("last_name")
    private String lastName;

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;
}

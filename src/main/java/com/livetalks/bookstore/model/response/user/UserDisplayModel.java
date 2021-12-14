package com.livetalks.bookstore.model.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDisplayModel {
    private UUID id;
    private String firstName;
    private String lastName;
    private String username;
}

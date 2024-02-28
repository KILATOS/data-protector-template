package org.masterleonardo.usersapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @Size(min = 3)
    @NotEmpty
    private String name;
    @NotEmpty
    //@Email
    @Size(min = 3)
    private String login;
    @NotEmpty
    private String password;
}

package org.masterleonardo.usersapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "people")
public class User implements Serializable {


    @Serial
    private static final long serialVersionUID = -6608266695638077137L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    @Size(min = 3)
    @NotEmpty
    private String name;

    @Column(name = "login")
    @NotEmpty
    @Size(min = 3)
    private String login;

    @Column(name = "password")
    @NotEmpty
    private String password;

    @Column(name = "role")
    private String role;
}

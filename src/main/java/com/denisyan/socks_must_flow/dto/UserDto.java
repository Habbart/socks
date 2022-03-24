package com.denisyan.socks_must_flow.dto;


import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UserDto {

    private int id;
    private String name;
    private String surname;
    private String login;
    private String password;
    private String role;

}

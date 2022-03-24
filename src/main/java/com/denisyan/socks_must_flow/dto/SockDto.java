package com.denisyan.socks_must_flow.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SockDto {

    private int id;
    private String color;
    private int cottonPart;
    private int quantity;

}

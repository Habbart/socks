package com.denisyan.socks_must_flow.entity;


import com.denisyan.socks_must_flow.validators.color_validator.IColorValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@Entity
@Table(name = "socks")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Sock {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @IColorValidator(message = "color is incorrect, please check color with allowed colors")
    @Column(name = "color")
    private String color;

    @Min(value = 0, message = "cotton part can't be negative")
    @Max(value = 100, message = "cotton part can't be more that 100")
    @NumberFormat
    @Column(name = "cotton_part")
    private int cottonPart;

    @Min(value = 0, message = "cotton part can't be negative")
    @NumberFormat
    @Column(name = "quantity")
    private int quantity;

    public Sock(String color, int cottonPart, int quantity) {
        this.color = color;
        this.cottonPart = cottonPart;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Sock{" +
                "id=" + id +
                ", color=" + color +
                ", cottonPart=" + cottonPart +
                ", quantity=" + quantity +
                '}';
    }

}

package com.denisyan.socks_must_flow.entity;



import com.denisyan.socks_must_flow.validators.IColorValidator;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@Entity
@Table(name = "socks")
public class Sock {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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


    public Sock() {
    }

    public Sock(String color, int cottonPart, int quantity) {
        this.color = color;
        this.cottonPart = cottonPart;
        this.quantity = quantity;
    }

    public Sock(int id, String color, int cottonPart, int quantity) {
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getCottonPart() {
        return cottonPart;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCottonPart(int cottonPart) {
        this.cottonPart = cottonPart;
    }

}

package com.ronald.jwtrest.api.dto;

public class CreateProductCommand {

    private String nom;
    private double price;

    protected CreateProductCommand() {

    }

    public String getNom() {
        return nom;
    }

    public double getPrice() {
        return price;
    }
}

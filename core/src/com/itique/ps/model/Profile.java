package com.itique.ps.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Profile implements Serializable {

    private String name;
    private double budget;
    private String biography;
    private Inventory inventory;
    private Biology biology;

    public Profile(String name, double budget, Inventory inventory, Biology biology) {
        this.name = name;
        this.budget = budget;
        this.inventory = inventory;
        this.biology = biology;
        this.biography = name + ", " + biology.getSex().name().toLowerCase()
        + ", " + biology.getAge() + " years old. Has " + biology.getSkin().name().toLowerCase()
        + " skin tone, " + biology.getBody().name().toLowerCase() + (biology.getSex() == Sex.FEMALE ? " herself. " : " himself. ")
        + "Natural hair color: " + biology.getHair().name().toLowerCase();
    }

    public Profile() {
        // for serialization
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Biology getBiology() {
        return biology;
    }

    public void setBiology(Biology biology) {
        this.biology = biology;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("budget", budget)
                .append("biography", biography)
                .append("inventory", inventory)
                .append("biology", biology)
                .toString();
    }
}

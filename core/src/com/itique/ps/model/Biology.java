package com.itique.ps.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Biology implements Serializable {

    private Skin skin;
    private Sex sex;
    private NaturalHair hair;
    private Body body;
    private int age;

    public Biology(Skin skin, Sex sex, NaturalHair hair, Body body, int age) {
        this.skin = skin;
        this.sex = sex;
        this.hair = hair;
        this.body = body;
        this.age = age;
    }

    public Biology() {
        // for serialization
    }


    public Skin getSkin() {
        return skin;
    }

    public Biology setSkin(Skin skin) {
        this.skin = skin;
        return this;
    }

    public Sex getSex() {
        return sex;
    }

    public Biology setSex(Sex sex) {
        this.sex = sex;
        return this;
    }

    public NaturalHair getHair() {
        return hair;
    }

    public Biology setHair(NaturalHair hair) {
        this.hair = hair;
        return this;
    }

    public Body getBody() {
        return body;
    }

    public Biology setBody(Body body) {
        this.body = body;
        return this;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("skin", skin)
                .append("sex", sex)
                .append("hair", hair)
                .append("body", body)
                .append("age", age)
                .toString();
    }
}

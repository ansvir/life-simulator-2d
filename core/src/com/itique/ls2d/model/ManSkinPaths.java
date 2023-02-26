package com.itique.ls2d.model;

import java.io.Serializable;

public class ManSkinPaths implements Serializable {

    private String head;
    private String body;
    private String hair;

    public ManSkinPaths(String head, String body, String hair) {
        this.head = head;
        this.body = body;
        this.hair = hair;
    }

    public ManSkinPaths() {
        // for serialization
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getHair() {
        return hair;
    }

    public void setHair(String hair) {
        this.hair = hair;
    }

}

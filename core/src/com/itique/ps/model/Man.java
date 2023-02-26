package com.itique.ps.model;

import com.itique.ps.util.generate.PersonGeneratorUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public class Man implements Serializable {

    private String id;
    private Profile profile;
    private ManSkinPaths skin;

    public Man(Profile profile) {
        this.id = UUID.randomUUID().toString();
        this.profile = profile;
        Map<Head, String> headTextures = PersonGeneratorUtil.getHeadTexturesPaths();
        Map<Skin, String> skinTextures = PersonGeneratorUtil.getSkinTexturesPaths();
        Map<NaturalHair, String> hairTextures = PersonGeneratorUtil.getNaturalHairTexturesPaths();
        Biology manBiology = profile.getBiology();
        this.skin = new ManSkinPaths(
                manBiology.getSkin() == Skin.LIGHT ? headTextures.get(Head.LIGHT)
                        : manBiology.getSkin() == Skin.MEDIUM ? headTextures.get(Head.MEDIUM)
                        : headTextures.get(Head.DARK),
                skinTextures.get(manBiology.getSkin()),
                manBiology.getAge() > 65 ? hairTextures.get(NaturalHair.GRAY) :
                        hairTextures.get(manBiology.getHair())
        );
    }

    public Man() {
        // for serialization
    }

    public String getId() {
        return id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public ManSkinPaths getSkin() {
        return skin;
    }

    public void setSkin(ManSkinPaths skin) {
        this.skin = skin;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("profile", profile)
                .append("skin", skin)
                .toString();
    }
}

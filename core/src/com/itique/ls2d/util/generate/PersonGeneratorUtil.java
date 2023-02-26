package com.itique.ls2d.util.generate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.itique.ls2d.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.badlogic.gdx.math.MathUtils.random;
import static java.lang.String.format;

public class PersonGeneratorUtil {

    // 1 - body, 2 - skin
    private static final String BODY_PATH_PATTERN = "textures/body_%s_%s.png";

    public static Man generatePerson() {
        List<Item> items = new ArrayList<>();
        Inventory inventory = new Inventory(items);
        NameGeneratorUtil.Config nameConfig = NameGeneratorUtil.Config.builder()
                .capitalize().build();
        double skinRandom = random();
        double hairRandom = random();
        double bodyRandom = random();
        Biology biology = new Biology(
                skinRandom < 0.33 ? Skin.LIGHT
                        : skinRandom > 0.33 && skinRandom < 0.66 ? Skin.MEDIUM
                        : Skin.DARK,
                random() >= 0.5 ? Sex.MALE : Sex.FEMALE,
                hairRandom < 0.25 ? NaturalHair.BLACK
                        : hairRandom > 0.25 && hairRandom < 0.5 ? NaturalHair.BROWN
                        : hairRandom > 0.5 && hairRandom < 0.75 ? NaturalHair.BLOND
                        : NaturalHair.GRAY,
                bodyRandom < 0.33 ? Body.THIN
                        : skinRandom > 0.33 && skinRandom < 0.66 ? Body.MIDDLE
                        : Body.FAT,
                (int) (random() * 100 + 14));
        Profile profile = new Profile(
                NameGeneratorUtil.generateName(10, nameConfig),
                random() * 2000.0,
                inventory,
                biology
        );
        return new Man(profile);
    }

    public static Texture buildManTexture(Man man) {
        ManSkinPaths newSkinPaths = getTexturesPathByBiology(man.getProfile().getBiology());
        man.setSkin(newSkinPaths);
        Texture skin = new Texture(Gdx.files.internal(man.getSkin().getBody()));
        skin.getTextureData().prepare();
        Pixmap skinPixmap = skin.getTextureData().consumePixmap();
        Texture head = new Texture(Gdx.files.internal(man.getSkin().getHead()));
        head.getTextureData().prepare();
        Pixmap headPixmap = head.getTextureData().consumePixmap();
        Texture hair = new Texture(Gdx.files.internal(man.getSkin().getHair()));
        hair.getTextureData().prepare();
        Pixmap hairPixmap = hair.getTextureData().consumePixmap();
        skinPixmap.drawPixmap(headPixmap, 5, 1);
        skinPixmap.drawPixmap(hairPixmap, 4, 1);

        Texture result = new Texture(skinPixmap);

        skinPixmap.dispose();
        headPixmap.dispose();
        hairPixmap.dispose();

        return result;
    }

    public static ManSkinPaths getTexturesPathByBiology(Biology biology) {
        return new ManSkinPaths(
                getHeadTexturesPaths().get(biology.getSkin() == Skin.LIGHT ? Head.LIGHT
                : biology.getSkin() == Skin.MEDIUM ? Head.MEDIUM : Head.DARK),
                buildBodyTexturePath(biology.getBody(), biology.getSkin()),
                getNaturalHairTexturesPaths().get(biology.getHair()));
    }

    public static Map<Skin, String> getSkinTexturesPaths() {
        return Map.of(
                Skin.LIGHT, "textures/body_light.png",
                Skin.MEDIUM, "textures/body_medium.png",
                Skin.DARK, "textures/body_dark.png");
    }

    public static Map<NaturalHair, String> getNaturalHairTexturesPaths() {
        return Map.of(
                NaturalHair.BLACK, "textures/hair_icon_dark.png",
                NaturalHair.BROWN, "textures/hair_icon_brown.png",
                NaturalHair.BLOND, "textures/hair_icon_blond.png",
                NaturalHair.GRAY,  "textures/hair_icon_gray.png");
    }

    public static Map<Head, String> getHeadTexturesPaths() {
        return Map.of(
                Head.LIGHT, "textures/head_icon_light.png",
                Head.MEDIUM, "textures/head_icon_medium.png",
                Head.DARK, "textures/head_icon_dark.png");
    }

    private static String buildBodyTexturePath(Body body, Skin skin) {
        return format(BODY_PATH_PATTERN, body.name(), skin.name()).toLowerCase();
    }

}

package com.itique.ls2d.util.generate;

import com.badlogic.gdx.utils.Disposable;
import com.itique.ls2d.model.Body;
import com.itique.ls2d.model.NaturalHair;
import com.itique.ls2d.model.Skin;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.itique.ls2d.model.Skin.DARK;
import static com.itique.ls2d.model.Skin.LIGHT;
import static com.itique.ls2d.model.Skin.MEDIUM;

public class BodyCustomizatorUtil implements Disposable {

    private Queue<Skin> skinQueue;
    private Queue<NaturalHair> hairQueue;
    private Queue<Body> bodyQueue;

    public BodyCustomizatorUtil() {
        skinQueue = new ConcurrentLinkedQueue<>();
        skinQueue.add(LIGHT);
        skinQueue.add(MEDIUM);
        skinQueue.add(DARK);
        hairQueue = new ConcurrentLinkedQueue<>();
        hairQueue.add(NaturalHair.BLACK);
        hairQueue.add(NaturalHair.BROWN);
        hairQueue.add(NaturalHair.BLOND);
        hairQueue.add(NaturalHair.GRAY);
        bodyQueue = new ConcurrentLinkedQueue<>();
        bodyQueue.add(Body.THIN);
        bodyQueue.add(Body.MIDDLE);
        bodyQueue.add(Body.FAT);
    }

    public Skin nextSkin() {
        Skin next = this.skinQueue.remove();
        this.skinQueue.add(next);
        return next;
    }

    public NaturalHair nextHair() {
        NaturalHair next = this.hairQueue.remove();
        this.hairQueue.add(next);
        return next;
    }

    public Body nextBody() {
        Body next = this.bodyQueue.remove();
        this.bodyQueue.add(next);
        return next;
    }

    @Override
    public void dispose() {
        this.skinQueue.clear();
        this.skinQueue = null;
        this.hairQueue.clear();
        this.hairQueue = null;
        this.bodyQueue.clear();
        this.bodyQueue = null;
    }

}

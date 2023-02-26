package com.itique.ps.custom.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.itique.ps.custom.AnimationWrap;

import java.util.Map;

public class HumanActor extends Actor {

    private Map<Movement, AnimationWrap> animations;
    private TextureRegion currentRegion;
    private Movement currentMovement;
    float stateTime;

    public HumanActor() {
        this.animations = Map.of(
                Movement.STAND, createAnimation("textures/anim/man_stand.png", 1, 1),
                Movement.WALK, createAnimation("textures/anim/man_walk.png", 2, 1));
        this.currentRegion = animations.get(Movement.STAND).getAnimation().getKeyFrame(0, true);
        this.currentMovement = Movement.STAND;
        this.setWidth(this.currentRegion.getRegionWidth());
        this.setHeight(this.currentRegion.getRegionHeight());
        this.stateTime = 0f;
    }

    @Override
    public void act(float delta) {
        this.stateTime += delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        AnimationWrap wrap = this.animations.get(currentMovement);
        this.currentRegion = wrap.getAnimation().getKeyFrame(this.stateTime, true);
        batch.draw(currentRegion.getTexture(), getX(), getY(),
                getWidth() / 2, getHeight() / 2, getWidth(), getHeight(),
                getScaleX(), getScaleY(), getRotation(), currentRegion.getRegionX(),
                currentRegion.getRegionY(), currentRegion.getRegionWidth(),
                currentRegion.getRegionHeight(), false, false);
    }

    private AnimationWrap createAnimation(String path, int cols, int rows) {
        Texture animationSheet = new Texture(Gdx.files.internal(path));
        TextureRegion[][] textureRegionAll = TextureRegion.split(animationSheet,
                animationSheet.getWidth() / cols,
                animationSheet.getHeight() / rows);
        TextureRegion[] textureRegionLine = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                textureRegionLine[index++] = textureRegionAll[i][j];
            }
        }
        return new AnimationWrap(animationSheet, textureRegionLine,
                new Animation<>(0.5f, textureRegionLine), cols, rows);
    }

    public void setMovement(Movement movement) {
        this.currentMovement = movement;
    }

    public enum Movement {
        STAND, WALK
    }

}

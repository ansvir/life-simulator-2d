package com.itique.ps.custom;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class AnimationWrap {
    private Texture animationSheet;
    private TextureRegion[] line;
    private Animation<TextureRegion> animation;
    private int cols;
    private int rows;

    public AnimationWrap(Texture animationSheet, TextureRegion[] line, Animation<TextureRegion> animation, int cols, int rows) {
        this.animationSheet = animationSheet;
        this.line = line;
        this.animation = animation;
        this.cols = cols;
        this.rows = rows;
    }

    public Texture getAnimationSheet() {
        return animationSheet;
    }

    public void setAnimationSheet(Texture animationSheet) {
        this.animationSheet = animationSheet;
    }

    public TextureRegion[] getLine() {
        return line;
    }

    public void setLine(TextureRegion[] line) {
        this.line = line;
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("animationSheet", animationSheet)
                .append("line", line)
                .append("animation", animation)
                .append("cols", cols)
                .append("rows", rows)
                .toString();
    }
}

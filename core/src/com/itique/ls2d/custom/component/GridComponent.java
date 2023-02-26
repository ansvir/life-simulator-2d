package com.itique.ls2d.custom.component;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.itique.ls2d.model.ImageItem;
import com.itique.ls2d.util.ResourceUtil;

import java.util.List;

public class GridComponent extends Window {

    private final List<ImageItem> items;
    private final int itemsPerRow;
    private final Runnable task;

    public GridComponent(String title, List<ImageItem> items, Runnable task, int itemsPerRow) {
        super(title, ResourceUtil.getDefaultSkin());
        super.setMovable(false);
        super.setModal(true);
        this.items = items;
        this.task = task;
        this.itemsPerRow = itemsPerRow;
        ImageButton close = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("textures/close.png"))));
        super.getTitleTable().add(close);
        buildGrid();
    }

    private void buildGrid() {
        for (int i = 0; i < this.items.size(); i++) {
            for (int j = i; j < i + this.itemsPerRow && j < this.items.size(); j++) {
                ImageButton button = new ImageButton(new SpriteDrawable(new Sprite(
                        this.items.get(j).getImage())));
                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        task.run();
                    }
                });
                super.add();
            }
            super.row();
        }
    }

}

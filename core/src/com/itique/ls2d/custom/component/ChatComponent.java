package com.itique.ls2d.custom.component;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.itique.ls2d.custom.actor.HumanActor;
import com.itique.ls2d.model.Man;
import com.itique.ls2d.service.event.DialogEvent;
import com.itique.ls2d.util.ResourceUtil;
import com.itique.ls2d.util.generate.PersonGeneratorUtil;

import java.util.concurrent.atomic.AtomicBoolean;

public class ChatComponent extends Window {

    private Table dialogTable;
    private final Skin skin;
    private final ImageButton close;

    public ChatComponent(String title) {
        super(title, ResourceUtil.getDefaultSkin());
        skin = ResourceUtil.getDefaultSkin();
        super.setMovable(false);
        super.setModal(true);
        close = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("textures/close.png"))));
        super.getTitleTable().add(close);
        dialogTable = new Table();
    }

    public void startChat(DialogEvent dialog, Man opponent) {
        Table table = new Table();
        table.setFillParent(true);
        Texture opponentTexture = PersonGeneratorUtil.buildManTexture(opponent);
        Image image = new Image(new Sprite(opponentTexture));
        table.add(image).pad(50).left();
        dialog.startDialog();
        dialogTable.setFillParent(true);
        dialogTable.add(new Label(dialog.hasNext() ? dialog.next() : "", skin))
                .pad(50).right();
        table.add(dialogTable);
    }

    public void appendMessage(String message) {
        dialogTable.row();
        dialogTable.add(new Label(message, skin));
    }

    @Override
    public void act(float delta) {
        AtomicBoolean visible = new AtomicBoolean(isVisible());
        close.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                visible.set(false);
            }
        });
        setVisible(visible.get());
    }
}

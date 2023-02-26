package com.itique.ls2d.custom.component;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.itique.ls2d.util.ResourceUtil;

public class DialogComponent extends Window {

    public DialogComponent(String title) {
        super(title, ResourceUtil.getDefaultSkin());
    }

}

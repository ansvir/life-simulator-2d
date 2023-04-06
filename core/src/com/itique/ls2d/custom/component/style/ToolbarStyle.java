package com.itique.ls2d.custom.component.style;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.List;
import java.util.stream.Collectors;

public class ToolbarStyle {

    public static class Editor implements Style {
        private final List<TextButton> buttons;

        public Editor(List<TextButton> buttons) {
            this.buttons = buttons;
        }

        public List<TextButton> getButtons() {
            return buttons;
        }

        @Override
        public List<Actor> getActors() {
            return buttons.stream().map(b -> (Actor) b).collect(Collectors.toList());
        }

    }

}

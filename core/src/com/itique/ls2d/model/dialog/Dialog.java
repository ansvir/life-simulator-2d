package com.itique.ls2d.model.dialog;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.itique.ls2d.model.Identifiable;

import java.io.Serializable;

public class Dialog extends Identifiable implements Serializable {

    private String pattern;
    @JsonProperty("accent")
    private AccentType accentType;
    @JsonProperty("type")
    private DialogType dialogType;

    public Dialog(String pattern, AccentType accentType, DialogType dialogType) {
        this.pattern = pattern;
        this.accentType = accentType;
        this.dialogType = dialogType;
    }

    public Dialog() {
        // serialization constructor
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public AccentType getAccentType() {
        return accentType;
    }

    public void setAccentType(AccentType accentType) {
        this.accentType = accentType;
    }

    public DialogType getDialogType() {
        return dialogType;
    }

    public void setDialogType(DialogType dialogType) {
        this.dialogType = dialogType;
    }

}

package com.itique.ls2d.service.event;

import com.itique.ls2d.model.dialog.Dialog;
import com.itique.ls2d.model.dialog.DialogType;
import com.itique.ls2d.service.file.DialogFileDao;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DialogEvent {

    private DialogFileDao dialogDao;
    private String aiMessage;
    private boolean aiStep;

    public DialogEvent() {
        this.dialogDao = new DialogFileDao();
        this.aiStep = true;
    }

    public void startDialog() {
        if (aiStep) {
            List<Dialog> dialogs = this.dialogDao.findAll().stream()
                    .filter(d -> d.getDialogType().equals(DialogType.GREETING))
                    .collect(Collectors.toList());
            int index = new Random().nextInt(dialogs.size());
            String pattern = dialogs.get(index).getPattern();
            if (pattern.contains("%s")) {
                this.aiMessage = String.format(pattern, "SOME PLACEHOLDER");
            } else {
                this.aiMessage = pattern;
            }
            aiStep = false;
        }
    }

    public boolean hasNext() {
        return this.aiStep;
    }

    public String next() {
        this.aiStep = !this.aiStep;
        return this.aiMessage;
    }

}

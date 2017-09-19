package com.bot.api.dialog;

import java.util.Map;

public class DialogMapper {

    private Map<String, Dialogable> dialogMap;

    public void setDialogMap(Map dialogMap) {
        this.dialogMap = dialogMap;
    }

    public Dialogable getDialog(String key) {
        return dialogMap.get(key);
    }
}

package com.bot.api.model.dialog;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Hyunwoo on 2017-07-31.
 */
@Data
@AllArgsConstructor(staticName = "valueOf")
public class Dialog {
    public static final String defaultDialogId = "None";
    public static final int defaultDialogStatusCode = 10000000;;
    private String userId;
    private String dialogId;
    private int dialogStatusCode;
}

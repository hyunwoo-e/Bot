package com.bot.api.model.dialog;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Hyunwoo on 2017-07-31.
 */
@Data
@AllArgsConstructor(staticName = "valueOf")
public class Dialog {
    public static final String none = "None";
    private String dialogId;
    private String dialogStatusCode;
}

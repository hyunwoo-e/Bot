package com.bot.api.model.dialog;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Hyunwoo on 2017-07-31.
 */
@Data
@AllArgsConstructor(staticName = "valueOf")
public class Dialog {
    private String userId;
    private String dialogId;
    private String dialogStatus;
}

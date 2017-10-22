package com.bot.api.core;

import com.bot.api.model.luis.Value;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Hyunwoo on 2017-07-31.
 */
@Data
@AllArgsConstructor(staticName = "valueOf")
public class Conversation {
    private String intent;
    private HashMap<String, ArrayList<Value>> entityMap;
    private String dialog;
    private Boolean isForceInto;
    private Integer tryCount;
}
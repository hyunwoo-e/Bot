package com.bot.api.core;

import com.bot.api.model.luis.Entity;
import com.bot.api.model.luis.Resolution;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Hyunwoo on 2017-07-31.
 */
@Data
@AllArgsConstructor(staticName = "valueOf")
public class Conversation {
    public static final String none = "None";
    private String intent;
    private String dialog;
    private HashMap<String, Resolution> entityMap;
}
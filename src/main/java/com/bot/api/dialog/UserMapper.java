package com.bot.api.dialog;

import com.bot.api.model.dialog.Dialog;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
public class UserMapper {
    public HashMap<String, Dialog> userMap;

    @PostConstruct
    public void init(){
        userMap = new HashMap<String, Dialog>();
    }

    public void put(String userKey, Dialog dialog) {
        userMap.put(userKey, dialog);
    }

    public Dialog get(String userKey) {
        return userMap.get(userKey);
    }

    public boolean containsKey(String userKey) {
        return userMap.containsKey(userKey);
    }
}
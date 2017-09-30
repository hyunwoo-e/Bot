package com.bot.api.core;

import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class UserMapper {
    public HashMap<String, Conversation> userMap;

    @PostConstruct
    public void init(){
        userMap = new HashMap<String, Conversation>();
    }

    public void put(String userKey, Conversation conversation) {
        userMap.put(userKey, conversation);
    }

    public Conversation get(String userKey) {
        return userMap.get(userKey);
    }

    public boolean containsKey(String userKey) {
        return userMap.containsKey(userKey);
    }
}
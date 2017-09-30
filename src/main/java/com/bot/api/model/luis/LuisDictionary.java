package com.bot.api.core;

import com.bot.api.conversation.profile.NoneBO;
import com.bot.api.conversation.profile.PartyBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class LuisDictionary {
    private Map<String, String> dictionary;

    @PostConstruct
    public void init(){
        dictionary = new HashMap<String, String>();
        dictionary.put("연락처", "telephone");
        dictionary.put("주소", "address");
        dictionary.put("메일", "email");
    }

    public String get(String key) {
        return dictionary.get(key);
    }
}

package com.bot.api.conversation.transport;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
public class SubwayTable {

    HashMap<String, String> subwayTable;


    @PostConstruct
    public void init(){
        subwayTable = new HashMap<String, String>();
        subwayTable.put("어린이대공원","어린이대공원(세종대)");
    }

    public String get(String key){
        return subwayTable.get(key);
    }
}

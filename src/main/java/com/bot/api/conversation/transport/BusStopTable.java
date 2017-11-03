package com.bot.api.conversation.transport;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
public class BusStopTable {
    HashMap<String, String> busStopTable = new HashMap<String, String>();

    //장소를 지하철 장소로 맞게 변경
    @PostConstruct
    public void init(){
        busStopTable.put("건대입구", "건대입구역사거리, 건대병원");
        busStopTable.put("어린이대공원(세종대)", "건대앞");
    }

    public String get(String key){
        return busStopTable.get(key);
    }

    public boolean containsKey(String key){
        return busStopTable.containsKey(key);
    }

}

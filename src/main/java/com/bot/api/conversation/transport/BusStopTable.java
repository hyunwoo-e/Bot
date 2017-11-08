package com.bot.api.conversation.transport;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
public class BusStopTable {
    HashMap<String, String> busStopTable;

    /*

   건입 , 건대입구역 -> @@건대입구@@ -> 건대입구역사거리.건대병원

   어대역, 어대정류장, 어대정류소, 어대 ->@@어린이대공원@@ -> 건대앞
   */
    @PostConstruct
    public void init(){
        busStopTable = new HashMap<String, String>();
        busStopTable.put("건대입구", "건대입구역사거리.건대병원");
        busStopTable.put("어린이대공원", "건대앞");
    }

    public String get(String key){
        return busStopTable.get(key);
    }

//    public boolean containsKey(String key){
//        return busStopTable.containsKey(key);
//    }

}

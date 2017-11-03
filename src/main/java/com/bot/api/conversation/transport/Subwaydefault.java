package com.bot.api.conversation.transport;


import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@Component
public class Subwaydefault {
    ArrayList<String> station_default;

    // default station
    @PostConstruct
    public void init(){
        station_default.add("건대입구");
        station_default.add("어린이대공원");
    }

    public String get(int index){
        return station_default.get(index);
    }

    public int size(){
        return station_default.size();
    }
}

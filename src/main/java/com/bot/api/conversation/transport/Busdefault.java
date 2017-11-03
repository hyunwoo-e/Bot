package com.bot.api.conversation.transport;


import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@Component
public class Busdefault {

    //default busStop
    ArrayList<String> station_default = new ArrayList<String>();

    @PostConstruct
    public void init(){
        station_default.add("104000139");
        station_default.add("104000136");
        station_default.add("104000138");
        station_default.add("104000050");
    }

    public int size(){
        return station_default.size();
    }
    public String get(int index){
        return station_default.get(index);
    }

}

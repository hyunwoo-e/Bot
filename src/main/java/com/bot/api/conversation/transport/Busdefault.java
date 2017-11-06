package com.bot.api.conversation.transport;


import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@Component
public class Busdefault {

    //default busStop
    ArrayList<String> station_default = new ArrayList<String>();

    /*

    정문  건대입구역사거리, 건대병원 : 104000139 - 05232 위로           --> 건입!
          건대입구역사거리, 건대병원 : 104000136 - 05229 아래로         --> 건입!
    후문  건대앞 : 104000138 - 05231 >>                                 --> 어대
          건대앞 : 104000050 - 05143 <<                                 --> 어대
    */


    @PostConstruct
    public void init(){
        station_default = new ArrayList<String>();
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

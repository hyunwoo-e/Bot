package com.bot.api.conversation.transport;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;

@Component
public class SubwayInfo {
    HashMap<String, String> subwayIdMaps;

    // subwayIdList
    @PostConstruct
    public void init(){
        subwayIdMaps = new HashMap<String, String>();
        subwayIdMaps.put("1001","1호선");
        subwayIdMaps.put("1002","2호선");
        subwayIdMaps.put("1003","3호선");
        subwayIdMaps.put("1004","4호선");
        subwayIdMaps.put("1005","5호선");
        subwayIdMaps.put("1006","6호선");
        subwayIdMaps.put("1007","7호선");
        subwayIdMaps.put("1008","8호선");
        subwayIdMaps.put("1075","분당선");
        subwayIdMaps.put("1077","신분당선");
        subwayIdMaps.put("1063","경의중앙선");
        subwayIdMaps.put("1067","경춘선");
        subwayIdMaps.put("1065","공항철도");
        subwayIdMaps.put("1071","수인선");
    }
    public String get(String key){
        return subwayIdMaps.get(key);
    }
    public boolean containsKey(String key){
        return subwayIdMaps.containsKey(key);
    }


}

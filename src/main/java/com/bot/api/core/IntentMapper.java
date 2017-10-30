package com.bot.api.core;

import com.bot.api.conversation.none.NoneBO;
import com.bot.api.conversation.member.MemberBO;
import com.bot.api.conversation.transport.SubwayBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class IntentMapper {
    private Map<String, Conversable> intentMap;

    @Autowired
    private NoneBO noneBO;

    @Autowired
    private MemberBO memberBO;

    @Autowired
    private SubwayBO subwayBO;


    @PostConstruct
    public void init(){
        intentMap = new HashMap<String, Conversable>();
        intentMap.put("None", noneBO);
        intentMap.put("관계자조회", memberBO);
        intentMap.put("지하철조회", subwayBO);
    }

    public Conversable getIntent(String key) {
        return intentMap.get(key);
    }
}

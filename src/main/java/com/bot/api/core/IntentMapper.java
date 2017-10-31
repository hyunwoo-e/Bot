package com.bot.api.core;

import com.bot.api.conversation.map.MapBO;
import com.bot.api.conversation.meal.MealBO;
import com.bot.api.conversation.none.NoneBO;
import com.bot.api.conversation.member.MemberBO;
import com.bot.api.conversation.schedule.ScheduleBO;
import com.bot.api.conversation.timetable.TimeTableBO;
import com.bot.api.conversation.transport.BusBO;
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

    @Autowired
    private MapBO mapBO;

    @Autowired
    private TimeTableBO timeTableBO;

    @Autowired
    private MealBO mealBO;

    @Autowired
    private ScheduleBO scheduleBO;

    @Autowired
    private BusBO busBO;

    @PostConstruct
    public void init(){
        intentMap = new HashMap<String, Conversable>();
        intentMap.put("None", noneBO);
        intentMap.put("관계자조회", memberBO);
        intentMap.put("지하철조회", subwayBO);
        intentMap.put("버스조회", busBO);
        intentMap.put("위치조회", mapBO);
        intentMap.put("학사일정조회", scheduleBO);
        intentMap.put("시간표조회", timeTableBO);
        intentMap.put("학식조회", mealBO);
    }

    public Conversable getIntent(String key) {
        return intentMap.get(key);
    }
}

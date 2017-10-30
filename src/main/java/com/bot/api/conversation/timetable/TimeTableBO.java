package com.bot.api.conversation.timetable;

import com.bot.api.core.Common;
import com.bot.api.core.Conversable;
import com.bot.api.core.Conversation;
import com.bot.api.core.UserMapper;
import com.bot.api.model.common.Schedule;
import com.bot.api.model.common.Timetable;
import com.bot.api.model.common.User;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Keyboard;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.luis.LUIS;
import com.bot.api.user.UserBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class TimeTableBO extends Conversable {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserBO userBO;

    public KakaoResponse makeKakaoResponse(String userKey, LUIS luisResponse) {
        Message message = new Message();
        Keyboard keyboard = new Keyboard();

        final String[] week = { "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일" };
        HashMap<String, Integer> dayMap = new HashMap<String, Integer>();
        dayMap.put("일요일",0);
        dayMap.put("월요일",1);
        dayMap.put("화요일",2);
        dayMap.put("수요일",3);
        dayMap.put("목요일",4);
        dayMap.put("금요일",5);
        dayMap.put("토요일",6);

        Calendar calendar = Calendar.getInstance( );  // 현재 날짜/시간 등의 각종 정보 얻기
        Integer LT_DAY = dayMap.get(week[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
        if(userMapper.get(userKey).getEntityMap().containsKey("요일")) {
            LT_DAY = dayMap.get(userMapper.get(userKey).getEntityMap().get("요일").get(0).getValue());
        }

        User user = userBO.selectUser(userKey);

        ResponseEntity<List<Timetable>> responseEntity = new RestTemplate().exchange("http://" + Common.konkuk_server_ip + ":" + Common.konkuk_server_port + "/konkuk/api/timetable.json?STD_NO="
                + user.getUserStudentNumber() + "&LT_DAY=" + LT_DAY, HttpMethod.GET, null, new ParameterizedTypeReference<List<Timetable>>() {
        });
        List<Timetable> timetables = responseEntity.getBody();

        String text = "";
        int i = 0;
        for(Timetable timetable : timetables) {
            if(timetable.getKOR_NM() != null) {
                i++;
                text += timetable.getTM_NO() + " " + timetable.getFRTOTIME() + "\n";
                if(timetable.getTYPL_KOR_NM() != null) text += timetable.getTYPL_KOR_NM();
                text += "\n";
                if(timetable.getKOR_NM() != null) text += timetable.getKOR_NM();
                text += "\n";
            }
        }

        if(i == 0) {
            text = "수업이 없습니다.\n";
        }

        message.setText(text);
        userMapper.put(userKey, Conversation.valueOf("None",null,"None", false,0));
        return KakaoResponse.valueOf(message, null);
    }
}
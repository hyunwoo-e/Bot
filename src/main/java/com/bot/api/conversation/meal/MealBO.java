package com.bot.api.conversation.meal;

import com.bot.api.core.Common;
import com.bot.api.core.Conversable;
import com.bot.api.core.Conversation;
import com.bot.api.core.UserMapper;
import com.bot.api.model.common.Meal;
import com.bot.api.model.common.Schedule;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Keyboard;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.kakao.MessageButton;
import com.bot.api.model.luis.LUIS;
import com.bot.api.model.luis.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MealBO extends Conversable {

    @Autowired
    private UserMapper userMapper;

    public KakaoResponse makeKakaoResponse(String userKey, LUIS luisResponse) {
        Message message = new Message();
        Keyboard keyboard = new Keyboard();
        String text = "";

        // 중복 제거
        HashSet<String> locationSet = new HashSet<String>();
        if(userMapper.get(userKey).getEntityMap().containsKey("장소")) {
            for(Value value : userMapper.get(userKey).getEntityMap().get("장소")) {
                locationSet.add(value.getValue());
            }
        }

        if(!locationSet.contains("학생회관") && !locationSet.contains("도서관")) {
            ArrayList<String> buttons = new ArrayList<String>();
            buttons.add("학생회관");
            buttons.add("도서관");
            keyboard.setType("buttons");
            keyboard.setButtons(buttons);
            message.setText("식당을 선택해주세요.");
            return KakaoResponse.valueOf(message, keyboard);
        }

        text += selectMeals(userKey, locationSet);
        message.setText(text);
        userMapper.put(userKey, Conversation.valueOf("None",null,"None", false,0));
        return KakaoResponse.valueOf(message, null);
    }

    private String selectMeals(String userKey, HashSet<String> locationSet) {
        HttpHeaders headers = new HttpHeaders();;
        HttpEntity<String> httpEntity = new HttpEntity<String>(headers);
        String text = "";

        HashSet<String> dateSet = new HashSet<String>();
        if(userMapper.get(userKey).getEntityMap().containsKey("날짜")) {
            for(Value value : userMapper.get(userKey).getEntityMap().get("날짜")) {
                dateSet.add(value.getValue());
            }
        }

        if(locationSet.contains("학생회관")) {
            //Meal[] meals = new RestTemplate().getForObject("http://"+ Common.konkuk_server_ip+":"+Common.konkuk_server_port+"/konkuk/api/meal.json?REST_NUM=3", Meal[].class);
            ResponseEntity<List<Meal>> responseEntity = new RestTemplate().exchange("http://"+ Common.konkuk_server_ip+":"+Common.konkuk_server_port+"/konkuk/api/meal.json?REST_NUM=3",
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Meal>>() {
                    });
            List<Meal> meals = responseEntity.getBody();


            text += "학생회관의 ";
            if(dateSet.contains("내일")) {
                text += selectTomorrowMeals(meals);
            }
            if(!dateSet.contains("내일") || dateSet.contains("오늘")) {
                text += selectTodayMeals(meals);
            }
        }

        if(locationSet.contains("도서관")) {
            //Meal[] meals = new RestTemplate().getForObject("http://"+ Common.konkuk_server_ip+":"+Common.konkuk_server_port+"/konkuk/api/meal.json?REST_NUM=2", Meal[].class);
            ResponseEntity<List<Meal>> responseEntity = new RestTemplate().exchange("http://"+ Common.konkuk_server_ip+":"+Common.konkuk_server_port+"/konkuk/api/meal.json?REST_NUM=3",
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Meal>>() {
                    });
            List<Meal> meals = responseEntity.getBody();

            text += "도서관의 ";
            if(dateSet.contains("내일")) {
                text += selectTomorrowMeals(meals);
            }
            if(!dateSet.contains("내일") || dateSet.contains("오늘")) {
                text += selectTodayMeals(meals);
            }
        }
        return text;
    }

    private String selectTodayMeals(List<Meal> meals) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();

        String text = "";
        int i = 0;
        text += "오늘 식단은 ";
        for(Meal meal : meals) {
            if(meal.getSALES_DT().equals(simpleDateFormat.format(calendar.getTime()))) {
                if(meal.getFOOD_NAME() != null)
                    text += meal.getFOOD_NAME().replaceFirst("\r\n",",");
                if(meal.getPRICE() != null) text += "(" + meal.getPRICE() + ")";
                text += "\n";
                i++;
            }
        }
        text += "입니다\n";

        if(i == 0) {
            text = "오늘은 식단이 없습니다.\n";
        }

        return text;
    }

    private String selectTomorrowMeals(List<Meal> meals) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,1);

        String text = "";
        int i = 0;

        text += "내일 식단은 ";
        for(Meal meal : meals) {
            if(meal.getSALES_DT().equals(simpleDateFormat.format(calendar.getTime()))) {
                if(meal.getFOOD_NAME() != null)
                    text += meal.getFOOD_NAME().replaceFirst("\r\n",",");
                if(meal.getPRICE() != null) text += "(" + meal.getPRICE() + ")";
                text += "\n";
                i++;
            }
        }
        text += "입니다\n";

        if(i == 0) {
            text = "내일은 식단이 없습니다.\n";
        }

        return text;
    }
}
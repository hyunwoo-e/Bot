package com.bot.api.core;

import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.luis.LUIS;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public abstract class Conversable {

    @Autowired
    private UserMapper userMapper;

    public abstract KakaoResponse makeKakaoResponse(String userKey, LUIS luisResponse);

    public Message findNullEntity (String userKey, HashMap<String, String> slots) {
        Message message = new Message();
        for (String key : slots.keySet()) {
            if (!userMapper.get(userKey).getEntityMap().containsKey(key)) {
                String text = "";
                if(userMapper.get(userKey).getTryCount() > 0) {
                    text += "요청하신 " + key + "를 찾을 수 없었습니다. 다시한번 ";
                }
                text += slots.get(key);
                message.setText(text);
                userMapper.get(userKey).setTryCount(userMapper.get(userKey).getTryCount() + 1);
                //System.out.println(userMapper.get(userKey).getTryCount());
                return message;
            }
        }
        return null;
    }


}


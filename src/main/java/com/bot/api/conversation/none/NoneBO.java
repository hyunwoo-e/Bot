package com.bot.api.conversation.profile;

import com.bot.api.core.Conversable;
import com.bot.api.core.UserMapper;
import com.bot.api.core.Conversation;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Keyboard;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.luis.Entity;
import com.bot.api.model.luis.LuisResponse;
import com.bot.api.model.luis.Resolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class NoneBO implements Conversable {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PartyDAO telephoneDAO;

    public KakaoResponse recvLuisResponse(String userKey, LuisResponse luisResponse) {
        setEntity(userKey, luisResponse);
        return getKakaoResponse(userKey, luisResponse);
    }

    private void setEntity(String userKey, LuisResponse luisResponse) {
        if(userMapper.get(userKey).getEntityMap() == null) {
            userMapper.get(userKey).setEntityMap(new HashMap<String, Resolution>());
        }

        for(Entity entity : luisResponse.getEntities()) {
            if(!userMapper.get(userKey).getEntityMap().containsKey(entity.getType())) {
                userMapper.get(userKey).getEntityMap().put(entity.getType(), entity.getResolution());
            } else {
                userMapper.get(userKey).getEntityMap().get(entity.getType()).getValues().add(entity.getResolution().getValues().get(0));
            }
        }
    }

    private KakaoResponse getKakaoResponse(String userKey, LuisResponse luisResponse) {
        String text = "";
        Message message = new Message();
        Keyboard keyboard = new Keyboard();

        message.setText(text);
        userMapper.put(userKey, Conversation.valueOf(Conversation.none, Conversation.none, null));

        return KakaoResponse.valueOf(message, keyboard);
    }
}
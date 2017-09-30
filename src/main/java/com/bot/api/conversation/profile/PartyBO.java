package com.bot.api.conversation.profile;

import com.bot.api.core.Conversable;
import com.bot.api.core.LuisDictionary;
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
public class PartyBO implements Conversable {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PartyDAO partyDAO;

    @Autowired
    private LuisDictionary luisDictionary;

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
                if(!userMapper.get(userKey).getEntityMap().get(entity.getType()).getValues().contains(entity.getResolution().getValues().get(0))) {
                    userMapper.get(userKey).getEntityMap().get(entity.getType()).getValues().add(entity.getResolution().getValues().get(0));
                }
            }
        }
    }

    private KakaoResponse getKakaoResponse(String userKey, LuisResponse luisResponse) {
        String text = "";
        Message message = new Message();
        Keyboard keyboard = new Keyboard();

        if(!userMapper.get(userKey).getEntityMap().containsKey("관계자")) {
            userMapper.get(userKey).setDialog("관계자 입력요청");
            text = "아래의 관계자를 안내드릴 수 있습니다.";
        } else {
            for(String partyName : userMapper.get(userKey).getEntityMap().get("관계자").getValues()) {
                HashMap <String, String> map = partyDAO.selectPartyByName(partyName);
                if(!userMapper.get(userKey).getEntityMap().containsKey("프로필")) {
                    //모두
                } else {
                    text += map.get("partyName") + " " + map.get("partyType") + "의";
                    for(String info : userMapper.get(userKey).getEntityMap().get("프로필").getValues()) {
                        text += " " + info + "은(는) " + map.get(luisDictionary.get(info));
                    }
                    text += " 입니다.";
                }
            }
            userMapper.put(userKey, Conversation.valueOf(Conversation.none, Conversation.none, null));
        }

        message.setText(text);
        return KakaoResponse.valueOf(message, keyboard);
    }
}
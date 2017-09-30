package com.bot.api.conversation.profile;

import com.bot.api.core.Conversable;
import com.bot.api.core.LuisDictionary;
import com.bot.api.core.UserMapper;
import com.bot.api.core.Conversation;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Keyboard;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.luis.LuisResponse;
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
        return getKakaoResponse(userKey, luisResponse);
    }

    private KakaoResponse getKakaoResponse(String userKey, LuisResponse luisResponse) {
        String text = "";
        Message message = new Message();
        Keyboard keyboard = new Keyboard();

        if(!userMapper.get(userKey).getEntityMap().containsKey("관계자")) {
            userMapper.get(userKey).setDialog("관계자 입력요청");
            text = "어떤 관계자의 정보를 안내드릴까요?";
        } else {
            for(String partyName : userMapper.get(userKey).getEntityMap().get("관계자")) {
                HashMap <String, String> map = partyDAO.selectPartyByName(partyName);
                if(!userMapper.get(userKey).getEntityMap().containsKey("프로필")) {
                    //모두
                } else {
                    text += map.get("partyName") + " " + map.get("partyType") + "의";
                    for(String info : userMapper.get(userKey).getEntityMap().get("프로필")) {
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
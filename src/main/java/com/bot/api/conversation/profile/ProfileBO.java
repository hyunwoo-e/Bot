package com.bot.api.conversation.profile;

import com.bot.api.core.Conversable;
import com.bot.api.core.LuisDictionary;
import com.bot.api.core.UserMapper;
import com.bot.api.core.Conversation;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Keyboard;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.luis.Entity;
import com.bot.api.model.luis.LUIS;
import com.bot.api.model.luis.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;

@Service
public class ProfileBO implements Conversable {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProfileDAO profileDAO;

    @Autowired
    private LuisDictionary luisDictionary;

    public KakaoResponse makeKakaoResponse(String userKey, LUIS luisResponse) {
        String text = "";
        Message message = new Message();
        Keyboard keyboard = new Keyboard();

        if(!userMapper.get(userKey).getEntityMap().containsKey("관계자")) {
            userMapper.get(userKey).setDialog("관계자 입력요청");
            text = "어떤 관계자의 정보를 안내드릴까요?";
        } else {
            for(Value value : userMapper.get(userKey).getEntityMap().get("관계자")) {
                String profileName = value.getValue();
                text = "관계자 정보";
                /*
                HashMap <String, String> map = profileDAO.selectProfileByName(profileName);
                if(!userMapper.get(userKey).getEntityMap().containsKey("프로필")) {
                    //모두
                } else {
                    text += map.get("profileName") + " " + map.get("profileType") + "의";
                    for(String infomation : userMapper.get(userKey).getEntityMap().get("프로필")) {
                        text += " " + infomation + "은(는) " + map.get(luisDictionary.get(infomation));
                    }
                    text += " 입니다.";
                }
                */
            }
            userMapper.put(userKey, Conversation.valueOf("None","None", null));
        }

        message.setText(text);
        return KakaoResponse.valueOf(message, keyboard);
    }
}
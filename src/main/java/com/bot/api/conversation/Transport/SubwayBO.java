package com.bot.api.conversation.Transport;

import com.bot.api.conversation.profile.ProfileDAO;
import com.bot.api.core.Conversable;
import com.bot.api.core.Conversation;
import com.bot.api.core.LuisDictionary;
import com.bot.api.core.UserMapper;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Keyboard;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.luis.LUIS;
import com.bot.api.model.luis.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubwayBO implements Conversable {

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

        text = "지하철 조회";
        for(Value value : userMapper.get(userKey).getEntityMap().get("지하철역")) {
            text += " " + value.getValue();
        }
        message.setText(text);
        return KakaoResponse.valueOf(message, keyboard);
    }
}
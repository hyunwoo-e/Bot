package com.bot.api.conversation.transport;

import com.bot.api.core.Conversable;
import com.bot.api.core.Conversation;
import com.bot.api.core.UserMapper;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Keyboard;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.luis.LUIS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusBO extends Conversable {

    @Autowired
    private UserMapper userMapper;

    public KakaoResponse makeKakaoResponse(String userKey, LUIS luisResponse) {
        Message message = new Message();
        Keyboard keyboard = new Keyboard();

        //TODO: 기능 요약
        String text =
                "버스 조회 기능은 추가될 예정입니다.\n";

        message.setText(text);
        userMapper.put(userKey, Conversation.valueOf("None",null,"None", false,0));
        return KakaoResponse.valueOf(message, null);
    }
}
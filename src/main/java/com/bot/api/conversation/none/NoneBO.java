package com.bot.api.conversation.none;

import com.bot.api.core.Conversable;
import com.bot.api.core.UserMapper;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Keyboard;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.luis.LUIS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoneBO implements Conversable {

    @Autowired
    private UserMapper userMapper;

    public KakaoResponse makeKakaoResponse(String userKey, LUIS luisResponse) {
        Message message = new Message();
        Keyboard keyboard = new Keyboard();

        //TODO: 기능 요약
        String text =
                "죄송합니다. 잘 알아듣지 못했습니다. 저는 다음과 같은 서비스를 제공해드릴 수 있습니다.\n";

        message.setText(text);
        return KakaoResponse.valueOf(message, keyboard);
    }
}
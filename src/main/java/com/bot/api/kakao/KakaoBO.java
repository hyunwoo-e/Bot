package com.bot.api.kakao;

import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Keyboard;
import org.springframework.stereotype.Service;

@Service
public class KakaoBO {
    public Keyboard enterRoom() {
        Keyboard keyboard;
        keyboard = new Keyboard();
        keyboard.setType("text");
        return keyboard;
    }
}
package com.bot.api.dialog.none;

import com.bot.api.dialog.Dialogable;
import com.bot.api.model.dialog.Dialog;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.luis.LuisResponse;
import org.springframework.stereotype.Service;

@Service
public class NoneBO implements Dialogable {
    public KakaoResponse recvLuisResponse(Dialog dialog, LuisResponse luisResponse) {
        return KakaoResponse.valueOf("None",null,null);
    }
}
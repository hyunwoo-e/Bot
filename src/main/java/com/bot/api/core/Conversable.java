package com.bot.api.core;

import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.luis.LuisResponse;

public interface Conversable {
    public KakaoResponse recvLuisResponse(String userKey, LuisResponse luisResponse);
}

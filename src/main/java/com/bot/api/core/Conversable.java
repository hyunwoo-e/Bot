package com.bot.api.core;

import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.luis.LUIS;

public interface Conversable {
    public KakaoResponse makeKakaoResponse(String userKey, LUIS luisResponse);
}

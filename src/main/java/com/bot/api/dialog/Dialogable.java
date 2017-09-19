package com.bot.api.dialog;

import com.bot.api.model.dialog.Dialog;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.luis.LuisResponse;

public interface Dialogable {
    public KakaoResponse recvLuisResponse(Dialog dialog, LuisResponse luisResponse);
}

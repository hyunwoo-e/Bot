package com.bot.api.dialog.none;

import com.bot.api.dialog.Dialogable;
import com.bot.api.dialog.UserMapper;
import com.bot.api.model.dialog.Dialog;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.luis.LuisResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoneBO implements Dialogable {

    @Autowired
    private UserMapper userMapper;

    public KakaoResponse recvLuisResponse(String userKey, LuisResponse luisResponse) {
        Dialog dialog = userMapper.get(userKey);

        dialog.setDialogId(Dialog.none);
        dialog.setDialogStatusCode(Dialog.none);
        userMapper.put(userKey, dialog);

        Message message;
        message = new Message();
        message.setText("None");
        return KakaoResponse.valueOf(message,null);
    }
}
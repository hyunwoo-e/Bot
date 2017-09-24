package com.bot.api.dialog.exchange;

import com.bot.api.dialog.Dialogable;
import com.bot.api.dialog.DialogDAO;
import com.bot.api.dialog.UserMapper;
import com.bot.api.model.dialog.Dialog;
import com.bot.api.model.dialog.dialog.Exchange;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Keyboard;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.luis.Entity;
import com.bot.api.model.luis.LuisResponse;
import com.bot.api.dialog.order.OrderDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ExchangeBO implements Dialogable {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ExchangeDAO exchangeDAO;

    public KakaoResponse recvLuisResponse(String userKey, LuisResponse luisResponse) {
        Dialog dialog = userMapper.get(userKey);

        dialog.setDialogId(Dialog.defaultDialogId);
        dialog.setDialogStatusCode(Dialog.defaultDialogStatusCode);
        userMapper.put(userKey, dialog);

        Message message;
        message = new Message();
        message.setText("교환");
        return KakaoResponse.valueOf(message,null);
    }
}
package com.bot.api.dialog.order;

import com.bot.api.dialog.Dialogable;
import com.bot.api.dialog.DialogDAO;
import com.bot.api.dialog.UserMapper;
import com.bot.api.model.dialog.Dialog;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.luis.LuisResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderBO implements Dialogable {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderDAO orderDAO;

    public KakaoResponse recvLuisResponse(String userKey, LuisResponse luisResponse) {
        Dialog dialog = userMapper.get(userKey);

        dialog.setDialogId(Dialog.none);
        dialog.setDialogStatusCode(Dialog.none);
        userMapper.put(userKey, dialog);

        Message message;
        message = new Message();
        message.setText("주문");
        return KakaoResponse.valueOf(message,null);
    }
}

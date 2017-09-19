package com.bot.api.dialog.exchange;

import com.bot.api.dialog.Dialogable;
import com.bot.api.dialog.DialogDAO;
import com.bot.api.model.dialog.Dialog;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.luis.LuisResponse;
import com.bot.api.dialog.order.OrderDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExchangeBO implements Dialogable {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private DialogDAO dialogDAO;

    public KakaoResponse recvLuisResponse(Dialog dialog, LuisResponse luisResponse) {
        dialogDAO.updateUserDialog(dialog.getUserId(), "None", "None");
        return KakaoResponse.valueOf("교환",null,null);
    }
}

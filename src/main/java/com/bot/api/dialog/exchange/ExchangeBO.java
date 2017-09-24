package com.bot.api.dialog.exchange;

import com.bot.api.dialog.Dialogable;
import com.bot.api.dialog.DialogDAO;
import com.bot.api.dialog.ResponseMapper;
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
    private ResponseMapper responseMapper;

    @Autowired
    private ExchangeDAO exchangeDAO;

    public KakaoResponse recvLuisResponse(String userKey, LuisResponse luisResponse) {
        setEntity(userKey, luisResponse);
        setNextDialog(userKey, luisResponse);
        return getResponseForNextDialog(userKey);
    }

    private void setEntity(String userKey, LuisResponse luisResponse) {
        HashMap<String, Object> entityMap = new HashMap<String, Object>();
        for(Entity entity : luisResponse.getEntities()) {
            entityMap.put(entity.getType(), entity.getEntity().replaceAll(" ", ""));
        }
    }

    private void setNextDialog(String userKey, LuisResponse luisResponse) {
        Dialog dialog = userMapper.get(userKey);
        /*
        Exchange exchange = exchangeDAO.selectUserExchange(userKey);
        if(exchange.getExchangeType() == null) {
            dialog.setDialogStatusCode("교환>사유");
        } else if(exchange.getTraceYn() == null) {
            dialog.setDialogStatusCode("교환>운송장번호여부");
        } else if(exchange.getTraceYn() == true) {
            dialog.setDialogStatusCode("교환>운송장번호있음");
        } else {
            dialog.setDialogId(Dialog.none);
            dialog.setDialogStatusCode(dialog.getDialogId() + ">" + Dialog.none);
        }
        */
        userMapper.put(userKey, dialog);
    }

    //[TODO] userKey에 따라 동적 응답 생성하기
    private KakaoResponse getResponseForNextDialog(String userKey) {
        KakaoResponse kakaoResponse = responseMapper.get(userMapper.get(userKey).getDialogStatusCode());
        return kakaoResponse;
    }
}
package com.bot.api.dialog;

import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Keyboard;
import com.bot.api.model.kakao.Message;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class ResponseMapper {
    public HashMap<String, KakaoResponse> responseMap;

    @PostConstruct
    public void init(){
        responseMap = new HashMap<String, KakaoResponse>();

        Message message;
        Keyboard keyboard;
        List<String> buttons;

        //None
        responseMap.put("None>None",KakaoResponse.valueOf(null, null));


        //교환
        buttons = new ArrayList<String>();
        keyboard = new Keyboard();
        message = new Message();
        buttons.add("사이즈 교환");
        buttons.add("불량 및 파손 교환");
        buttons.add("물품 교환");
        keyboard.setType("buttons");
        keyboard.setButtons(buttons);
        message.setText("교환 사유를 선택해 주세요.");
        responseMap.put("교환>None",KakaoResponse.valueOf(message, keyboard));
        responseMap.put("교환>사유",KakaoResponse.valueOf(message, keyboard));

        message = new Message();
        message.setText("현재 운송장 번호를 가지고 계십니까?");
        responseMap.put("교환>운송장번호여부",KakaoResponse.valueOf(message, null));

        message.setText("교환 안내 드리겠습니다. 1588-2121 연결 후 1번을 입력하세요. 운송장번호 입력 후 * 을 입력해주세요.");
        responseMap.put("교환>운송장번호있음",KakaoResponse.valueOf(message, null));


        //주문


        //배송


    }

    public KakaoResponse get(String dialogStatusCode) {
        return responseMap.get(dialogStatusCode);
    }
}
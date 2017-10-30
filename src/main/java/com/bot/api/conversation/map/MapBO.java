package com.bot.api.conversation.map;

import com.bot.api.core.*;
import com.bot.api.model.common.KonkukMap;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Keyboard;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.kakao.MessageButton;
import com.bot.api.model.luis.LUIS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class MapBO extends Conversable {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MapDAO mapDAO;

    public KakaoResponse makeKakaoResponse(String userKey, LUIS luis) {
        return makeDefaultResponse(userKey, luis);
    }

    public KakaoResponse makeDefaultResponse(String userKey, LUIS luis) {
        String text = "";
        Message message = new Message();
        Keyboard keyboard = new Keyboard();

        // SLOT 정의
        HashMap<String, String> slots = new HashMap<String, String>();
        slots.put("장소", "조회하실 장소를 말씀해주세요.");

        // NULL 엔티티 요청
        Message entityMessage;
        if((entityMessage = super.findNullEntity(userKey,slots)) != null)
            return KakaoResponse.valueOf(entityMessage, null);

        // 지도 조회
        MessageButton messageButton = new MessageButton();
        messageButton.setLabel("지도보기");

        KonkukMap konkukMap = mapDAO.selectMap(userMapper.get(userKey).getEntityMap().get("장소").get(0).getValue());
        if(konkukMap == null) {
            String query = luis.getQuery().replaceAll(" ","+");
            message.setText("요청하신 장소에 대한 정보를 찾을 수 없어 검색한 결과입니다.");
            messageButton.setUrl("https://www.google.co.kr/search?q="+query);
        } else {
            message.setText("요청하신 장소에 대한 정보입니다. 링크를 통해 확인해 주세요!");
            messageButton.setUrl("http://"+ Common.konkuk_server_ip+":"+Common.konkuk_server_port+"/konkuk/map?mapLatitude="+konkukMap.getMapLatitude()+"+&mapLongitude="+konkukMap.getMapLongitude()+"&mapName="+konkukMap.getMapName());
        }

        message.setMessage_button(messageButton);
        userMapper.put(userKey, Conversation.valueOf("None", null, "None", false, 0));
        return KakaoResponse.valueOf(message, null);
    }
}
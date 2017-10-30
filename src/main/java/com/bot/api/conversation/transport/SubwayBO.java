package com.bot.api.conversation.transport;

import com.bot.api.core.Conversable;
import com.bot.api.core.Conversation;
import com.bot.api.core.UserMapper;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Keyboard;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.luis.LUIS;
import com.bot.api.model.luis.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubwayBO extends Conversable {

    @Autowired
    private UserMapper userMapper;

    public KakaoResponse makeKakaoResponse(String userKey, LUIS luisResponse) {
        String text = "";
        Message message = new Message();
        Keyboard keyboard = new Keyboard();

        if(userMapper.get(userKey).getEntityMap().containsKey("지하철역")) {
            for(Value value : userMapper.get(userKey).getEntityMap().get("지하철역")) {
                text += value.getValue() + " A호선 B방면: n분전\n";
                text += value.getValue() + " A호선 C방면: n분전\n";
            }
        } else {
            text += "건대입구 2호선 A방면: a분전\n";
            text += "건대입구 2호선 B방면: b분전\n";
            text += "건대입구 7호선 C방면: c분전\n";
            text += "건대입구 7호선 D방면: d분전\n";
            text += "어린이대공원 2호선 E방면: e분전\n";
            text += "어린이대공원 7호선 F방면: f분전";
        }

        userMapper.put(userKey, Conversation.valueOf("None",null,"None", false,0));

        message.setText(text);
        return KakaoResponse.valueOf(message, null);
    }
}
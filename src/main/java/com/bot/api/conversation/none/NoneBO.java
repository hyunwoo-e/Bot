package com.bot.api.conversation.none;

import com.bot.api.core.Common;
import com.bot.api.core.Conversable;
import com.bot.api.core.Conversation;
import com.bot.api.core.UserMapper;
import com.bot.api.model.common.Schedule;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Keyboard;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.luis.Generation;
import com.bot.api.model.luis.LUIS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

@Service
public class NoneBO extends Conversable {

    @Autowired
    private UserMapper userMapper;

    public KakaoResponse makeKakaoResponse(String userKey, LUIS luis) {
        Message message = new Message();
        Keyboard keyboard = new Keyboard();

        String text = makeGeneration(luis);
        System.out.println(text);

        message.setText(text);
        userMapper.put(userKey, Conversation.valueOf("None",null,"None", false,0));
        return KakaoResponse.valueOf(message, null);
    }

    public String makeGeneration(LUIS luis) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        HttpEntity<String> httpEntity = new HttpEntity<String>("{\"text\":\"" + luis.getQuery() + "\"}", headers);

        Generation generation = new RestTemplate().postForObject("http://"+ Common.nlu_server_ip+":"+Common.nlu_server_port+"/generate", httpEntity, Generation.class);
        return generation.getText();
    }
}
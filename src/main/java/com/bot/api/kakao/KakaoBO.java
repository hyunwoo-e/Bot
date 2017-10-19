package com.bot.api.kakao;

import com.bot.api.core.IntentMapper;
import com.bot.api.core.UserMapper;
import com.bot.api.core.Conversation;
import com.bot.api.model.kakao.KakaoRequest;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Keyboard;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.luis.Entity;
import com.bot.api.model.luis.LUIS;
import com.bot.api.model.luis.Value;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class KakaoBO {

    private static final Logger log = LogManager.getRootLogger();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IntentMapper intentMapper;

    public Keyboard enterRoom() {
        Keyboard keyboard;
        keyboard = new Keyboard();
        keyboard.setType("text");
        return keyboard;
    }

    public KakaoResponse addFriends(KakaoRequest kakaoRequest) {
        Message message = new Message();
        message.setText("안녕하세요! 저는 건국대학교 봇입니다. 무엇을 도와드릴까요?");
        return KakaoResponse.valueOf(message, null);
    }

    public KakaoResponse recvMessage(KakaoRequest kakaoRequest) {
        Conversation conversation;
        LUIS luis;

        if(!userMapper.containsKey(kakaoRequest.getUser_key())) {
            userMapper.put(kakaoRequest.getUser_key(), Conversation.valueOf("None","None", null));
        }

        //현재 다이얼로그 조회
        conversation = userMapper.get(kakaoRequest.getUser_key());

        //자연어 분석(Intent가 None이면 가장 최근의 Intent로 설정)
        luis = makeLUISResponse(kakaoRequest);
        log.debug(luis.getIntent());
        if(luis.getIntent().getIntent().equals("None")) {
            luis.getIntent().setIntent(conversation.getIntent());
        }
        conversation.setIntent(luis.getIntent().getIntent());
        userMapper.put(kakaoRequest.getUser_key(), conversation);

        //인텐트별 엔티티 삽입 후 응답(null값 요청 / 최종 응답)
        setEntities(kakaoRequest.getUser_key(), luis);
        return intentMapper.getIntent(conversation.getIntent()).makeKakaoResponse(kakaoRequest.getUser_key(), luis);
    }

    public LUIS makeLUISResponse(KakaoRequest kakaoRequest) {
        String url = "http://localhost:8000";

        RestTemplate restTemplate;
        HttpHeaders headers;
        HttpEntity<String> httpEntity;

        headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        httpEntity = new HttpEntity<String>("{\"text\":\"" + kakaoRequest.getContent() + "\"}", headers);
        return new RestTemplate().postForObject(url + "/recognize", httpEntity, LUIS.class);
    }

    private void setEntities(String userKey, LUIS luis) {
        if(userMapper.get(userKey).getEntityMap() == null) {
            userMapper.get(userKey).setEntityMap(new HashMap<String, ArrayList<Value>>());
        }

        for(Entity entity : luis.getEntities()) {
            if(userMapper.get(userKey).getEntityMap().get(entity.getEntity()) == null) {
                userMapper.get(userKey).getEntityMap().put(entity.getEntity(), new ArrayList<Value>());
            }
            for(int i = 0 ; i < entity.getValues().size() ; i++) {
                userMapper.get(userKey).getEntityMap().get(entity.getEntity()).add(entity.getValues().get(i));
            }
        }
    }
}
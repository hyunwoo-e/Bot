package com.bot.api.kakao;

import com.bot.api.core.IntentMapper;
import com.bot.api.core.UserMapper;
import com.bot.api.core.Conversation;
import com.bot.api.model.kakao.KakaoRequest;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Keyboard;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.luis.LuisResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

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
        userMapper.put(kakaoRequest.getUser_key(), Conversation.valueOf(Conversation.none,
                Conversation.none, null));

        Message message = new Message();
        message.setText("안녕하세요! 저는 건국대학교 봇입니다. 무엇을 도와드릴까요?");
        return KakaoResponse.valueOf(message, null);
    }

    public KakaoResponse recvMessage(KakaoRequest kakaoRequest) {
        Conversation conversation;
        LuisResponse luisResponse;

        if(!userMapper.containsKey(kakaoRequest.getUser_key())) {
            userMapper.put(kakaoRequest.getUser_key(), Conversation.valueOf(Conversation.none,
                    Conversation.none, null));
        }

        //현재 다이얼로그 조회
        conversation = userMapper.get(kakaoRequest.getUser_key());

        //자연어 분석(Intent가 None이면 가장 최근의 Intent로 설정)
        luisResponse = getLuisResponse(kakaoRequest);
        if(luisResponse.getTopScoringIntent().getIntent().equals(Conversation.none)) {
            luisResponse.getTopScoringIntent().setIntent(conversation.getIntent());
        }
        conversation.setIntent(luisResponse.getTopScoringIntent().getIntent());
        userMapper.put(kakaoRequest.getUser_key(), conversation);

        //인텐트별 엔티티 삽입 후 응답(null값 요청 / 최종 응답)
        return intentMapper.getIntent(conversation.getIntent()).recvLuisResponse(kakaoRequest.getUser_key(), luisResponse);
    }

    public LuisResponse getLuisResponse(KakaoRequest kakaoRequest) {
        String url = "https://westus.api.cognitive.microsoft.com/luis/v2.0/apps/";
        String appId = "930f605b-a5aa-4592-99b6-e869cb4fb57c";
        String subscriptionKey = "4092d942caeb4719aab875ae81dc90b1";

        RestTemplate restTemplate;
        HttpHeaders headers;
        HttpEntity<String> httpEntity;

        headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        headers.set("Ocp-Apim-Subscription-Key", subscriptionKey);

        httpEntity = new HttpEntity<String>("\"" + kakaoRequest.getContent() + "\"", headers);
        return new RestTemplate().postForObject(url + appId, httpEntity, LuisResponse.class);
    }
}
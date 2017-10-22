package com.bot.api.kakao;

import com.bot.api.user.UserBO;
import com.bot.api.core.IntentMapper;
import com.bot.api.core.UserMapper;
import com.bot.api.core.Conversation;
import com.bot.api.model.kakao.*;
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

@Service
public class KakaoBO {

    private static final Logger log = LogManager.getRootLogger();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IntentMapper intentMapper;

    @Autowired
    private UserBO userBO;

    public Keyboard enterRoom() {
        Keyboard keyboard;
        keyboard = new Keyboard();
        keyboard.setType("text");
        return keyboard;
    }

    public void addFriends(KakaoRequest kakaoRequest) {

    }

    public KakaoResponse recvMessage(KakaoRequest kakaoRequest) {
        LUIS luis;

        //현재 다이얼로그가 없을 경우 다이얼로그 생성
        if(!userMapper.containsKey(kakaoRequest.getUser_key())) {
            //학생 인증
            if(userBO.selectUser(kakaoRequest.getUser_key()) == null) {
                return makeJoinMessage();
            }
            userMapper.put(kakaoRequest.getUser_key(), Conversation.valueOf("None",null,"None", false,0));
        }

        //현재 다이얼로그 조회
        String intent = userMapper.get(kakaoRequest.getUser_key()).getIntent();
        Boolean isForceInto = userMapper.get(kakaoRequest.getUser_key()).getIsForceInto();
        Integer tryCount = userMapper.get(kakaoRequest.getUser_key()).getTryCount();

        //자연어 분석(Intent가 None이거나, 강제입력의 경우 가장 최근의 Intent로 설정)
        luis = makeLUISResponse(kakaoRequest);
        if(luis.getIntent().getIntent().equals("None") || isForceInto) {
            //3회 실패시 Intent 초기화
            if(tryCount > 3) {
                userMapper.put(kakaoRequest.getUser_key(), Conversation.valueOf("None",null,"None", false,0));
            }
            luis.getIntent().setIntent(intent);
        }
        userMapper.get(kakaoRequest.getUser_key()).setIntent(luis.getIntent().getIntent());

        //인텐트별 엔티티 삽입 후 응답(null값 요청 / 최종 응답)
        setEntities(kakaoRequest.getUser_key(), luis);
        return intentMapper.getIntent(userMapper.get(kakaoRequest.getUser_key()).getIntent()).makeKakaoResponse(kakaoRequest.getUser_key(), luis);
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

    public KakaoResponse makeJoinMessage() {
        Message message = new Message();
        MessageButton messageButton = new MessageButton();
        messageButton.setLabel("등록하기");
        messageButton.setUrl("http://");
        message.setMessage_button(messageButton);
        message.setText("건국대학교 학생 인증이 필요합니다. 아래 링크를 통해 인증해 주세요!");
        return KakaoResponse.valueOf(message, null);
    }
}
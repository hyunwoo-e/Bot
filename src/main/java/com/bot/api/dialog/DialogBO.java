package com.bot.api.dialog;

import com.bot.api.model.dialog.Dialog;
import com.bot.api.model.kakao.KakaoRequest;
import com.bot.api.model.kakao.KakaoResponse;
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
import java.util.HashMap;
import java.util.Map;

@Service
public class DialogBO {
    private static final Logger log = LogManager.getRootLogger();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DialogMapper dialogMapper;

    @Autowired
    private DialogDAO dialogDAO;

    public KakaoResponse recvMessage(KakaoRequest kakaoRequest) {
        Dialog dialog;
        LuisResponse luisResponse;

        //현재 다이얼로그 조회
        if(!userMapper.containsKey(kakaoRequest.getUser_key())) {
            userMapper.put(kakaoRequest.getUser_key(), Dialog.valueOf(Dialog.none,
                    Dialog.none + ">" + Dialog.none));
        }
        dialog = userMapper.get(kakaoRequest.getUser_key());

        //자연어 분석
        luisResponse = getLuisResponse(kakaoRequest);
        if(dialog.getDialogId().equals(Dialog.none)) {
            dialog.setDialogId(luisResponse.getTopScoringIntent().getIntent());
            dialog.setDialogStatusCode(dialog.getDialogId() + ">" + Dialog.none);
        }

        //인텐트별 엔티티 삽입 후 응답(null값 요청 / 최종 응답)
        return dialogMapper.getDialog(dialog.getDialogId()).recvLuisResponse(kakaoRequest.getUser_key(), luisResponse);
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
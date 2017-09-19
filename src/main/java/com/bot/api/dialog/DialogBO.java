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

@Service
public class DialogBO {
    private static final Logger log = LogManager.getRootLogger();

    @Autowired
    private DialogDAO dialogDAO;

    @Autowired
    private DialogMapper dialogMapper;

    public KakaoResponse recvMessage(KakaoRequest kakaoRequest) {
        Dialog dialog;
        LuisResponse luisResponse;

        //현재 다이얼로그 조회
        dialog = dialogDAO.selectUserDialog(kakaoRequest.getUser_key());
        if(dialog == null) {
            dialogDAO.insertUserDialog(kakaoRequest.getUser_key(), "None", "None");
            dialog = Dialog.valueOf(kakaoRequest.getUser_key(), "None", "None");
        }

        //자연어 분석
        luisResponse = getLuisResponse(kakaoRequest);

        //다이얼로그가 존재하지 않으면 다이얼로그 생성
        if(dialog.getDialogId().equals("None")) {
            dialogDAO.updateUserDialog(dialog.getUserId(), luisResponse.getTopScoringIntent().getIntent(), "None");
            dialog.setDialogId(luisResponse.getTopScoringIntent().getIntent());
        }

        //인텐트별 엔티티 삽입 후 응답(null값 요청 / 최종 응답)
        return dialogMapper.getDialog(dialog.getDialogId()).recvLuisResponse(dialog, luisResponse);
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
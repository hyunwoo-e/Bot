package com.bot.api.conversation.transport;

import com.bot.api.core.Conversable;
import com.bot.api.core.UserMapper;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Keyboard;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.luis.LUIS;
import com.bot.api.model.transportation.Bus.BusArrival;
import com.bot.api.model.transportation.Bus.BusStop;
import com.bot.api.model.transportation.Subway.Subway;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

@Service
public class BusBO implements Conversable{

    private static final int INFOSIZE = 4;

    @Autowired
    private UserMapper userMapper;

    public KakaoResponse makeKakaoResponse(String userKey, LUIS luisResponse)  {
        String text = "";
        Message message = new Message();
        Keyboard keyboard = new Keyboard();

        text += "test";


        message.setText(text);
        return KakaoResponse.valueOf(message,null);

    }

    public BusArrival getInfo(String station){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders;
        HttpEntity<String> httpEntity;

        //url종류가 두 곳 양방향 - bus 4곳 url이 필요

        String busStop = station;
        String url_bus= "http://ws.bus.go.kr/api/rest/arrive/getLowArrInfoByStId?serviceKey=ihXrHl%2F6vXM4XArXesQyaEYv3SiIEYJwW9bevTFoVdZ0ZNIGRVAMU%2FN2G9kHohYLUgakPcXnWI6knmQcul1u7Q%3D%3D&stId=";
        String key = "ihXrHl%2F6vXM4XArXesQyaEYv3SiIEYJwW9bevTFoVdZ0ZNIGRVAMU%2FN2G9kHohYLUgakPcXnWI6knmQcul1u7Q%3D%3D";


        BusArrival busArrival = restTemplate.getForObject(url_bus, BusArrival.class);
      //  httpHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
      //  httpEntity = new HttpEntity<String>(null, httpHeaders);

        return busArrival;
    }

    public BusStop getBusStop(String station){

        String url_bus="";
        String key = "";



        return;




    }


}

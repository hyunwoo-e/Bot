package com.bot.api.Test;

import com.bot.api.model.transportation.Subway.Subway;
import org.springframework.web.client.RestTemplate;

import java.util.StringTokenizer;

public class SubwayTest {
    public static void main(String[] args) {


        RestTemplate restTemplate = new RestTemplate();

        //subway
        String key = "526f4f714e786b613730566c676145";
        String url_subway = null;
        String station = "건대입구";

        url_subway = "http://swopenapi.seoul.go.kr/api/subway/" + key + "/json/realtimeStationArrival/1/8/" + station;


        String url_raw = "http://swopenapi.seoul.go.kr/api/subway/526f4f714e786b613730566c676145/json/realtimeStationArrival/1/8/건대입구";


//        String subway = restTemplate.getForObject(url_subway, String.class);
//        System.out.println(subway);
        System.out.println("성공");

        Subway subwayObject = restTemplate.getForObject(url_subway, Subway.class);

//        StringTokenizer st = new StringTokenizer(subwayObject.getRealtimeArrivalList().get(0).getSubwayList(),", ");
//        while(st.hasMoreTokens()){
//            System.out.println(st.nextToken());
//        }
        for(int i=0; i<subwayObject.getRealtimeArrivalList().size(); i++) {
            System.out.println((i+1)+" 번째");
          //  System.out.println(subwayObject.getRealtimeArrivalList().get(i).getSubwayId());
            System.out.println(subwayObject.getRealtimeArrivalList().get(i).getTrainLineNm());
            System.out.println(subwayObject.getRealtimeArrivalList().get(i).getBarvlDt());
            System.out.println(subwayObject.getRealtimeArrivalList().get(i).getArvlMsg3());
        }
    }
}

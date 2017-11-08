package com.bot.api.Test;

import com.bot.api.model.transportation.Subway.Subway;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import javax.management.ListenerNotFoundException;
import javax.xml.ws.Response;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

public class SubwayTest {
    public static void main(String[] args) {


        RestTemplate restTemplate = new RestTemplate();
        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
        //subway
        String key = "526f4f714e786b613730566c676145";
        String url_subway = null;
        String station = "건대입구";

        url_subway = "http://swopenapi.seoul.go.kr/api/subway/" + key + "/json/realtimeStationArrival/1/8/" + station;


        String url_raw = "http://swopenapi.seoul.go.kr/api/subway/526f4f714e786b613730566c676145/json/realtimeStationArrival/1/8/건대입구";


        Subway subway = null;

        long start2 = System.currentTimeMillis();
        for(int t=0; t<20; t++){
            Subway subwayObject = restTemplate.getForObject(url_subway, Subway.class);
        }
        long end2 = System.currentTimeMillis();
        System.out.println("동기 "+(end2-start2)/1000+"초");


        long start = System.currentTimeMillis();
        for(int t=0; t<20; t++) {
            ListenableFuture<ResponseEntity<Subway>> responseEntityListenableFuture = asyncRestTemplate.getForEntity(url_subway, Subway.class);

            try {
                subway = responseEntityListenableFuture.get().getBody();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

//            for (int i = 0; i < subway.getRealtimeArrivalList().size(); i++) {
//                System.out.println((i + 1) + " 번째");
//                //  System.out.println(subwayObject.getRealtimeArrivalList().get(i).getSubwayId());
//                System.out.println(subway.getRealtimeArrivalList().get(i).getTrainLineNm());
//                System.out.println(subway.getRealtimeArrivalList().get(i).getBarvlDt());
//                System.out.println(subway.getRealtimeArrivalList().get(i).getArvlMsg3());
//            }
        }
        long end = System.currentTimeMillis();
        System.out.println("비동기 "+(end-start)/1000+"초");
    }
}

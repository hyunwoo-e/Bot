package com.bot.api.Test;

import com.bot.api.model.transportation.Bus.ArrivalInfo.BusArrival;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class BusTest {

    public static void main(String[] args){

        RestTemplate restTemplate = new RestTemplate();

        String key = "ihXrHl%2F6vXM4XArXesQyaEYv3SiIEYJwW9bevTFoVdZ0ZNIGRVAMU%2FN2G9kHohYLUgakPcXnWI6knmQcul1u7Q%3D%3D";
        String decoding_key = null;
        try {
            decoding_key = URLDecoder.decode(key,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //busstop url
//        String st = "수원역";
//        String url_busStop = "http://openapi.gbis.go.kr/ws/rest/busstationservice?serviceKey="+decoding_key+"&keyword="+st;
//        String busStop = restTemplate.getForObject(url_busStop,String.class);
//        BusStop busStope = restTemplate.getForObject(url_busStop,BusStop.class);
//        System.out.println(busStope.getMsgBody().getBusStationList().get(0).getStationId());




        //busarrival url
        String s = "104000139";
        String url_busArrival = "http://ws.bus.go.kr/api/rest/arrive/getLowArrInfoByStId?ServiceKey="+decoding_key+"&stId="+s;
        String busArrival = restTemplate.getForObject(url_busArrival,String.class);
        System.out.println(busArrival);
        BusArrival busArrivalObject = restTemplate.getForObject(url_busArrival, BusArrival.class);
        System.out.println(busArrivalObject.getMsgBody().getItemList().get(0).getRtNm());
        System.out.println(busArrivalObject.getMsgBody().getItemList().get(0).getArrmsg1());
        System.out.println(busArrivalObject.getMsgBody().getItemList().get(0).getArrmsg2());

    }
}

package com.bot.api.Test;

import com.bot.api.model.transportation.Subway.Subway;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


public class Main {

    public static void main(String[] args) {


        RestTemplate restTemplate = new RestTemplate();

        //subway
        String key = "526f4f714e786b613730566c676145";
        String url_subway = null;
        String station = "건대입구";

        url_subway = "http://swopenapi.seoul.go.kr/api/subway/" + key + "/json/realtimeStationArrival/1/8/" + station;


        String url_raw = "http://swopenapi.seoul.go.kr/api/subway/526f4f714e786b613730566c676145/json/realtimeStationArrival/1/8/건대입구";


        String subway = restTemplate.getForObject(url_subway, String.class);
        System.out.println(subway);
        Subway subwayObject = restTemplate.getForObject(url_subway, Subway.class);
        System.out.println("성공");
        String urlTest=null;
        System.out.println(subwayObject.getRealtimeArrivalList().get(0).getSubwayId());
        System.out.println(subwayObject.getRealtimeArrivalList().get(0).getTrainLineNm());
        System.out.println(subwayObject.getRealtimeArrivalList().get(0).getBarvlDt());
        System.out.println(subwayObject.getRealtimeArrivalList().get(0).getArvlMsg3());



//        text = a.makeSubwayInfo(subwayObject.getRealtimeArrivalList(), null);
//        System.out.println(text);


        String key2 = "ihXrHl%2F6vXM4XArXesQyaEYv3SiIEYJwW9bevTFoVdZ0ZNIGRVAMU%2FN2G9kHohYLUgakPcXnWI6knmQcul1u7Q%3D%3D";
        String encoding_key = null;
        String decoding_key = null;
        String original = null;
        try {
            decoding_key = URLDecoder.decode(key2,"utf-8");
            encoding_key = URLEncoder.encode(key2,"utf-8");
            original = URLEncoder.encode(decoding_key,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        System.out.println(encoding_key);
//        System.out.println(decoding_key);
//        System.out.println(original);


        //busstop url
//        String st = "수원역";
//        String url_busStop = "http://openapi.gbis.go.kr/ws/rest/busstationservice?serviceKey="+decoding_key+"&keyword="+st;
//        String busStop = restTemplate.getForObject(url_busStop,String.class);
//        BusStop busStope = restTemplate.getForObject(url_busStop,BusStop.class);
//        System.out.println(busStope.getMsgBody().getBusStationList().get(0).getStationId());




        //busarrival url
//        String s = "104000050";
//        String url_busArrival = "http://ws.bus.go.kr/api/rest/arrive/getLowArrInfoByStId?ServiceKey="+decoding_key+"&stId="+s;
//        String busArrival = restTemplate.getForObject(url_busArrival,String.class);
//        System.out.println(busArrival);
//        BusArrival busArrivalObject = restTemplate.getForObject(url_busArrival, BusArrival.class);
//        System.out.println(busArrivalObject.getMsgBody().getItemList().get(0).getRtNm());
//        System.out.println(busArrivalObject.getMsgBody().getItemList().get(0).getArrmsg1());
//        System.out.println(busArrivalObject.getMsgBody().getItemList().get(0).getArrmsg2());
//        System.out.println("bustArrivalObject created!!!");


    }
}

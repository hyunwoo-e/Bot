package com.bot.api.Test;

import com.bot.api.model.transportation.Bus.ArrivalInfo.BusArrival;
import com.bot.api.model.transportation.Bus.StopInfo.BusStop;
import com.bot.api.model.transportation.Subway.Subway;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BusTest {

    public static void main(String[] args){

        RestTemplate restTemplate = new RestTemplate();
        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
        long start = 0;
        long end = 0;
        long first = 0;
        long second = 0;

        String key = "ihXrHl%2F6vXM4XArXesQyaEYv3SiIEYJwW9bevTFoVdZ0ZNIGRVAMU%2FN2G9kHohYLUgakPcXnWI6knmQcul1u7Q%3D%3D";
        String decoding_key = null;
        try {
            decoding_key = URLDecoder.decode(key,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //busstop url
        int t = 1;
//        String st = "수원역";
//        String url_busStop = "http://openapi.gbis.go.kr/ws/rest/busstationservice?serviceKey="+decoding_key+"&keyword="+st;
//        BusStop busStop = null;
//        start = System.currentTimeMillis();
//        for(int i=0; i<t; i++){
//            busStop = restTemplate.getForObject(url_busStop, BusStop.class);
//        }
//        end = System.currentTimeMillis();
//        System.out.println("동기 "+ (end-start)/1000 + "초");
//
//        start = System.currentTimeMillis();
//        for(int i=0; i<t; i++) {
//            ListenableFuture<ResponseEntity<BusStop>> responseEntityListenableFuture = asyncRestTemplate.getForEntity(url_busStop, BusStop.class);
//
//            try {
//                busStop = responseEntityListenableFuture.get().getBody();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//        }
//        end = System.currentTimeMillis();
//        System.out.println("비동기"+ (end-start)/1000+"초");


//        String busStop = restTemplate.getForObject(url_busStop,String.class);
//        BusStop busStope = restTemplate.getForObject(url_busStop,BusStop.class);
//        System.out.println(busStope.getMsgBody().getBusStationList().get(0).getStationId());



        //busarrival url
        String s = "104000139";
        String url_busArrival = "http://ws.bus.go.kr/api/rest/arrive/getLowArrInfoByStId?ServiceKey="+decoding_key+"&stId="+s;
//        BusArrival busArrival;
//        start = System.currentTimeMillis();
//        for(int i=0; i<t; i++){
//            busArrival = restTemplate.getForObject(url_busArrival, BusArrival.class);
//         //   System.out.println("동기 받음");
//            for(int j=0; j<busArrival.getMsgBody().getItemList().size(); j++) {
//                System.out.println(busArrival.getMsgBody().getItemList().get(j).getArrmsg1());
//            }
//        }
//        end = System.currentTimeMillis();
//        first = (end-start)/1000;
    //    System.out.println("동기 "+ (end-start)/1000 + "초");

        start = System.currentTimeMillis();
        ArrayList<ListenableFuture<ResponseEntity<BusArrival>>> list = new ArrayList<ListenableFuture<ResponseEntity<BusArrival>>>();
        for(int i=0; i<t; i++){
            ListenableFuture<ResponseEntity<BusArrival>> responseEntityListenableFuture = asyncRestTemplate.getForEntity(url_busArrival, BusArrival.class);
            list.add(responseEntityListenableFuture);
        }
        for(int i=0; i<list.size(); i++){
            try {
                for(int j=0; j<list.get(i).get().getBody().getMsgBody().getItemList().size(); j++){
                    System.out.println(list.get(i).get().getBody().getMsgBody().getItemList().get(j).getArrmsg1());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        end = System.currentTimeMillis();
        second = (end-start)/1000;
        System.out.println("동기 "+ first + "초");
        System.out.println("비동기"+ second +"초");

//        for(int i=0; i<t; i++) {
//            ListenableFuture<ResponseEntity<BusArrival>> responseEntityListenableFuture = asyncRestTemplate.getForEntity(url_busArrival, BusArrival.class);
//          //  System.out.println("비동기 받음");
//
//
//            try {
//                busArrival = responseEntityListenableFuture.get().getBody();
//                for(int j=0; j<busArrival.getMsgBody().getItemList().size(); j++) {
//                    System.out.println(busArrival.getMsgBody().getItemList().get(j).getArrmsg1());
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//                    System.out.println(responseEntityListenableFuture.get().getBody().getMsgBody().getItemList().get(i).getStNm());
//                    System.out.println(responseEntityListenableFuture.get().getBody().getMsgBody().getItemList().get(i).getArrmsg1());
//                    System.out.println(responseEntityListenableFuture.get().getBody().getMsgBody().getItemList().get(i).getArrmsg2());



//            try {
//                busArrival = responseEntityListenableFuture.get().getBody();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
        }


//        String busArrival = restTemplate.getForObject(url_busArrival,String.class);
//        BusArrival busArrivalObject = restTemplate.getForObject(url_busArrival, BusArrival.class);
//        System.out.println(busArrivalObject.getMsgBody().getItemList().get(0).getRtNm());
//        System.out.println(busArrivalObject.getMsgBody().getItemList().get(0).getArrmsg1());
//        System.out.println(busArrivalObject.getMsgBody().getItemList().get(0).getArrmsg2());

    }


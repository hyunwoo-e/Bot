package com.bot.api.conversation.transport;

import com.bot.api.core.Conversable;
import com.bot.api.core.Conversation;
import com.bot.api.core.UserMapper;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Keyboard;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.luis.LUIS;
import com.bot.api.model.luis.Value;
import com.bot.api.model.transportation.Bus.ArrivalInfo.BusArrival;
import com.bot.api.model.transportation.Bus.ArrivalInfo.ItemList;
import com.bot.api.model.transportation.Bus.StopInfo.BusStop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@Service
public class BusBO extends Conversable {

    private final int STANDARD = 0;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Busdefault busdefault;

    @Autowired
    private BusStopTable busStopTable;

    public KakaoResponse makeKakaoResponse(String userKey, LUIS luisResponse){
        String text = "";
        Message message = new Message();
      //  Keyboard keyboard = new Keyboard();

     //   BusStop busStop;
     //   BusArrival busArrival;

        //변환 전
        ArrayList<Value> station;
        ArrayList<Value> busNumberList;

        //변환 후 정류소 정보 -> 실제 사용할 정류소 명
        ArrayList<String> stationList;

        //변환 후 정류소 정보 -> 정류소 Id
        ArrayList<String> stationIdList;

        //도착정보들 리스트 정보 ->


       // ArrayList<ListenableFuture<ResponseEntity<BusArrival>>> busArrivalListfuture;
        ArrayList<ListenableFuture<ResponseEntity<BusStop>>> busStopListfuture;

        //장소와 버스 번호가 없을 경우
        if(!userMapper.get(userKey).getEntityMap().containsKey("장소") && !userMapper.get(userKey).getEntityMap().containsKey("숫자")){
            //station_default 제공
            try {
                text += this.makeBusArrivalInfo(this.getBusArrival(busdefault.getStation_default()),null);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if(userMapper.get(userKey).getEntityMap().containsKey("장소")){
            //Entity 정류소 장소 정보 받기
            station = userMapper.get(userKey).getEntityMap().get("장소");

            //건대입구나 어린이대공원(세종대)가 있을 경우 정류소 이름으로 맵핑해주어야함.
            //장소 -> 정류소에 맞게 맵핑
            stationList = new ArrayList<String>();
            for(int i=0; i<station.size(); i++){
                if(station.get(i).getValue().equals("건대입구")){
                    stationList.add(busStopTable.get("건대입구"));
                }else if(station.get(i).getValue().equals("어린이대공원")){
                    stationList.add(busStopTable.get("어린이대공원"));
                }else {
                    stationList.add(station.get(i).getValue());
                }
            }

            //이제 이거를 busId로 바꿔야함.

            //버스정류소 정보를 스트링 맵핑
            stationIdList = new ArrayList<String>();
            busStopListfuture = this.getBusStop(stationList);

            for(int i=0; i<busStopListfuture.size(); i++){
                try {
                    for(int j=0; j<busStopListfuture.get(i).get().getBody().getMsgBody().getBusStationList().size(); j++){
                        stationIdList.add(busStopListfuture.get(i).get().getBody().getMsgBody().getBusStationList().get(j).getStationId());
                    }
                } catch (Exception e){
//                    e.printStackTrace();
                }
            }
            if(!userMapper.get(userKey).getEntityMap().containsKey("숫자")){
                //버스번호가 없다면 해당정류소의 모든 버스 정보제공
                try {
                    text += this.makeBusArrivalInfo(this.getBusArrival(stationIdList),null);
                } catch (Exception e) {
                  //  e.printStackTrace();
                }
            }else{
                //버스번호가 있다면!? 그 버스정류소에서 해당하는 숫자의 버스 정보만 제공하기
                busNumberList = userMapper.get(userKey).getEntityMap().get("숫자");
                for(int i=0; i<busNumberList.size(); i++){
                    try {
                        text += this.makeBusArrivalInfo(this.getBusArrival(stationIdList),busNumberList.get(i).getValue());
                    } catch (Exception e) {
                      //  e.printStackTrace();
                    }
                }
            }
        }else if(userMapper.get(userKey).getEntityMap().containsKey("숫자")){
            //slot 사용해서 물어보기
            HashMap<String, String> slots = new HashMap<String, String>();
            slots.put("장소", "정류소 명을 입력해주세요");

            Message entityMessage;
            if((entityMessage = super.findNullEntity(userKey,slots)) != null)
                return KakaoResponse.valueOf(entityMessage, null);
        }else{}

        message.setText(text);
        userMapper.put(userKey, Conversation.valueOf("None",null,"None", false,0));
        return KakaoResponse.valueOf(message,null);
    }


    //정류소 id로 도착하는 버스 정보
    public ArrayList<ArrayList<ItemList>> getBusArrival(ArrayList<String> stationIdList) throws Exception{
        BusArrival busArrival = null;
        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();

        //decoding key
        String key = "ihXrHl/6vXM4XArXesQyaEYv3SiIEYJwW9bevTFoVdZ0ZNIGRVAMU/N2G9kHohYLUgakPcXnWI6knmQcul1u7Q==";
        String url_busArrival = "http://ws.bus.go.kr/api/rest/arrive/getLowArrInfoByStId?ServiceKey="+key+"&stId=";

        //정류소 id로 도착 버스 요청
        ArrayList<ListenableFuture<ResponseEntity<BusArrival>>> busArrivalList = new ArrayList<ListenableFuture<ResponseEntity<BusArrival>>>();
        for(int i=0; i<stationIdList.size(); i++){
            ListenableFuture<ResponseEntity<BusArrival>> responseEntity = asyncRestTemplate.getForEntity(url_busArrival+stationIdList.get(i), BusArrival.class);
            busArrivalList.add(responseEntity);
        }
        ArrayList<ArrayList<ItemList>> busArrivals = new ArrayList<ArrayList<ItemList>>();
        for(int i=0; i<busArrivalList.size(); i++){
            busArrivals.add(busArrivalList.get(i).get().getBody().getMsgBody().getItemList());
        }

        return busArrivals;
    }

    //역 입력하여 정류소 id 얻음 ..  input = 역이름(100퍼일치하게해야함) output = BusStop객체
    public ArrayList<ListenableFuture<ResponseEntity<BusStop>>> getBusStop(ArrayList<String> stationList){

    //    BusStop busStop = null;
        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
        //   String key = "ihXrHl%2F6vXM4XArXesQyaEYv3SiIEYJwW9bevTFoVdZ0ZNIGRVAMU%2FN2G9kHohYLUgakPcXnWI6knmQcul1u7Q%3D%3D";

        //decoding key
        String key = "ihXrHl/6vXM4XArXesQyaEYv3SiIEYJwW9bevTFoVdZ0ZNIGRVAMU/N2G9kHohYLUgakPcXnWI6knmQcul1u7Q==";
        String url_busStop = "http://openapi.gbis.go.kr/ws/rest/busstationservice?serviceKey="+key+"&keyword=";

        //입력받은 역명'들'을 async - rest 요청
        ArrayList<ListenableFuture<ResponseEntity<BusStop>>> busStopList = new ArrayList<ListenableFuture<ResponseEntity<BusStop>>>();
        for(int i=0; i<stationList.size(); i++) {
            ListenableFuture<ResponseEntity<BusStop>> responseEntity = asyncRestTemplate.getForEntity(url_busStop+stationList.get(i), BusStop.class);
            busStopList.add(responseEntity);
        }
        return busStopList;
    }

    //정류소 정보 텍스트 출력
    @Async
    public String makeBusArrivalInfo(ArrayList<ArrayList<ItemList>> busArrivalList, String busNumber) throws Exception{
        String text = "";
        int busArrivaListSize = busArrivalList.size();

        //  ArrayList<ItemList> busArrival = busArrival.getMsgBody().getItemList();
        ArrayList<ItemList> busArrival = null;

        for (int i = 0; i < busArrivaListSize; i++) {
            //busArrival 각각의 정보를 담는다
            busArrival = busArrivalList.get(i);

            //숫자 정보가 없다면
            if (busNumber == null) {
                text += busArrival.get(STANDARD).getStNm() + " 정류소\n";
                for (int j = 0; j < busArrival.size(); j++) {
                    text += busArrival.get(j).getRtNm() + " 버스\n" +
                            "첫번째 버스 " + busArrival.get(j).getArrmsg1() + "\n" +
                            "두번째 버스 " + busArrival.get(j).getArrmsg2() + "\n";
                }
            } else {
                //숫자 정보가 있다면
                for (int j = 0; j < busArrival.size(); j++) {
                    if (busNumber.equals(busArrival.get(j).getRtNm())) {
                        text += busArrival.get(STANDARD).getStNm() + " 정류소\n" +
                                busArrival.get(j).getRtNm() + " 버스\n" +
                                "첫번째 버스 " + busArrival.get(j).getArrmsg1() + "\n" +
                                "두번째 버스 " + busArrival.get(j).getArrmsg2() + "\n";
                        break;
                    } else {
                        if (j == busArrival.size() - 1) {
                            text += busArrival.get(STANDARD).getStNm() + " 정류소\n" +
                                    busNumber + "버스 정보가 없습니다.\n";
                        }
                    }
                }

            }
        }
        return text;
    }

//body end
}

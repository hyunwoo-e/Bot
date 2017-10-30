package com.bot.api.conversation.transport;

import com.bot.api.core.Conversable;
import com.bot.api.core.UserMapper;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Keyboard;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.luis.LUIS;
import com.bot.api.model.luis.Value;
import com.bot.api.model.transportation.Bus.ArrivalInfo.BusArrival;
import com.bot.api.model.transportation.Bus.ArrivalInfo.ItemList;
import com.bot.api.model.transportation.Bus.StopInfo.BusStationList;
import com.bot.api.model.transportation.Bus.StopInfo.BusStop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

        ArrayList<BusStop> busStopList;
        BusArrival busArrival;

        ArrayList<Value> station;
        ArrayList<Value> subwayId;

        ArrayList<String> station_default = new ArrayList<String>();
        station_default.add("104000139");
        station_default.add("104000136");
        station_default.add("104000138");
        station_default.add("104000050");
        /*

        정문  건대입구역사거리, 건대병원 : 104000139 - 05232 위로           --> 건입!
              건대입구역사거리, 건대병원 : 104000136 - 05229 아래로         --> 건입!
        후문  건대앞 : 104000138 - 05231 >>                                 --> 어대
              건대앞 : 104000050 - 05143 <<                                 --> 어대
         */

        /*
        1) 버스 언제와 -> default 값으로 건입, 어대역 버스 제공.. 양방향 쫌많은거 같은데?
        2) @@역에 버스 언제와? -> 해당역이 존재할 경우 역에 해당하는 버스 정보 제공 or 역 정보가 옳지 않다고 말하기
        3) 302번 버스 언제와? -> 정류소 위치 묻기 -> 정류소 정보가 제공되었을 때 정류소가 존재하는지 확인. 존재하면 @@역 버스 정보 제공 아니면 정류소 없다고말하기
        4) @@역 ㅇㅇ버스 ㅁㅁ역 ㅂㅂ버스 언제와? 모든 역 버스 조회후..
         */

        //장소와 버스 번호가 없을 경우
        if(!userMapper.get(userKey).getEntityMap().containsKey("장소") && !userMapper.get(userKey).getEntityMap().containsKey("버스번호")){

            //station_default 제공
            for(int i=0; i<station_default.size(); i++){
                if(i==0)
                    text += "정문\n";
                else if(i==2)
                    text += "후문\n";
                else

                text += this.makeBusArrivalInfo(this.getBusArrival(station_default.get(i)));
            }
        }else if(userMapper.get(userKey).getEntityMap().containsKey("장소")){
            //건입ㅁㄴㅇ -> 건대입구로 맵핑해야함..
            station = userMapper.get(userKey).getEntityMap().get("장소");
            busStopList = new ArrayList<BusStop>();
            for(int i=0; i<station.size(); i++){
                busStopList.add(this.getBusStop(station.get(i).getValue()));
            }

            if(!userMapper.get(userKey).getEntityMap().containsKey("버스번호")){
                //버스번호가 없다면 해당정류소의 모든 버스 정보제공
                for(int i=0; i<busStopList.size(); i++){
                    ArrayList<BusStationList> busStationLists = busStopList.get(i).getMsgBody().getBusStationList();
                    for(int j=0; j<busStationLists.size(); j++){
                        text += this.makeBusArrivalInfo(this.getBusArrival(busStationLists.get(j).getStationId()));
                    }
                }
                  //      busStopList.get(0).getResponse().getMsgBody().getBusStationList().get(0).getStationId();
            }else{

            }


        }else if(userMapper.get(userKey).getEntityMap().containsKey("버스번호")){

        }else{
            //뺴도될듯
        }


        message.setText(text);
        return KakaoResponse.valueOf(message,null);

    }

    //정류소 id로 도착하는 버스 정보 제공
    public BusArrival getBusArrival(String stationId){
        RestTemplate restTemplate = new RestTemplate();
        //orginal key
        // String key = "ihXrHl%2F6vXM4XArXesQyaEYv3SiIEYJwW9bevTFoVdZ0ZNIGRVAMU%2FN2G9kHohYLUgakPcXnWI6knmQcul1u7Q%3D%3D";

        //decoding key
        String key = "ihXrHl/6vXM4XArXesQyaEYv3SiIEYJwW9bevTFoVdZ0ZNIGRVAMU/N2G9kHohYLUgakPcXnWI6knmQcul1u7Q==";
        String url_busArrival = "http://ws.bus.go.kr/api/rest/arrive/getLowArrInfoByStId?ServiceKey="+key+"&stId="+stationId;


        BusArrival busArrival = restTemplate.getForObject(url_busArrival, BusArrival.class);


        return busArrival;
    }


    //역 입력하여 정류소 id 얻음 ..  input = 역이름(100퍼일치하게해야함) output = BusStop객체
    public BusStop getBusStop(String station){

        RestTemplate restTemplate = new RestTemplate();
     //   String key = "ihXrHl%2F6vXM4XArXesQyaEYv3SiIEYJwW9bevTFoVdZ0ZNIGRVAMU%2FN2G9kHohYLUgakPcXnWI6knmQcul1u7Q%3D%3D";

        //decoding key
          String key = "ihXrHl/6vXM4XArXesQyaEYv3SiIEYJwW9bevTFoVdZ0ZNIGRVAMU/N2G9kHohYLUgakPcXnWI6knmQcul1u7Q==";

        String url_busStop = "http://openapi.gbis.go.kr/ws/rest/busstationservice?serviceKey="+key+"&keyword="+station;

        BusStop busStop = restTemplate.getForObject(url_busStop,BusStop.class);

        return busStop;

    }

    //정류소 정보 만들기
    public String makeBusArrivalInfo(BusArrival busArrival){
        String text = "";

        text += this.getBusArrivalInfoText(busArrival);


        return text;
    }

    //정류소 정보 텍스트 출력
    public String getBusArrivalInfoText(BusArrival busArrival){
        String text = "";

        ArrayList<ItemList> busArrivalList = busArrival.getMsgBody().getItemList();

        for(int i=0; i<busArrivalList.size(); i++){
            text += busArrivalList.get(i).getRtNm() + "버스\n"+
                    "첫번째 버스 " + busArrivalList.get(i).getArrmsg1()+"\n"+
                    "두번째 버스 " + busArrivalList.get(i).getArrmsg2()+"\n";
        }

        return text;
    }


}

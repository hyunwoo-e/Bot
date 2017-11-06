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
import java.util.HashMap;

@Service
public class BusBO extends Conversable {

    private final int STANDARD = 0;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Busdefault busdefault;

    @Autowired
    private BusStopTable busStopTable;

    public KakaoResponse makeKakaoResponse(String userKey, LUIS luisResponse)  {
        String text = "";
        Message message = new Message();
        Keyboard keyboard = new Keyboard();

        //변환 전
        ArrayList<Value> station;
        ArrayList<Value> busNumberList;

        //변환 후 정류소 정보 -> 실제 사용할 정류소 명
        ArrayList<String> stationList;

        //변환 후 정류소 정보 -> 정류소 Id
        ArrayList<BusStop> busStopList;

        /*
        1) 버스 언제와 -> default 값으로 건입, 어대역 버스 제공.. 양방향 쫌많은거 같은데?
        2) @@역에 버스 언제와? -> 해당역이 존재할 경우 역에 해당하는 버스 정보 제공 or 역 정보가 옳지 않다고 말하기
        3) 302번 버스 언제와? -> 정류소 위치 묻기 -> 정류소 정보가 제공되었을 때 정류소가 존재하는지 확인. 존재하면 @@역 버스 정보 제공 아니면 정류소 없다고말하기
        4) @@역 ㅇㅇ버스 ㅁㅁ역 ㅂㅂ버스 언제와? 모든 역 버스 조회후..
         */

        //장소와 버스 번호가 없을 경우
        if(!userMapper.get(userKey).getEntityMap().containsKey("장소") && !userMapper.get(userKey).getEntityMap().containsKey("숫자")){
            //station_default 제공
            for(int i=0; i<busdefault.size(); i++){
                if(i==0)
                    text += "정문\n";
                else if(i==2)
                    text += "후문\n";
                else

                    text += this.makeBusArrivalInfo(this.getBusArrival(busdefault.get(i)), null);
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

            //버스정류소 정보 맵핑
            busStopList = new ArrayList<BusStop>();
            for(int i=0; i<stationList.size(); i++){
                busStopList.add(this.getBusStop(stationList.get(i)));
            }

            ArrayList<BusStationList> busStationLists;
            if(!userMapper.get(userKey).getEntityMap().containsKey("숫자")){
                //버스번호가 없다면 해당정류소의 모든 버스 정보제공
                for(int i=0; i<busStopList.size(); i++){
                    busStationLists = busStopList.get(i).getMsgBody().getBusStationList();
                    for(int j=0; j<busStationLists.size(); j++){
                        text += this.makeBusArrivalInfo(this.getBusArrival(busStationLists.get(j).getStationId()), null);
                    }
                }
            }else{
                //버스번호가 있다면!? 그 버스정류소에서 해당하는 숫자의 버스 정보만 제공하기
                busNumberList = userMapper.get(userKey).getEntityMap().get("숫자");
                for(int i=0; i<busStopList.size(); i++){
                    busStationLists = busStopList.get(i).getMsgBody().getBusStationList();
                    for(int j=0; j<busStationLists.size(); j++){
                        for(int k=0; k<busNumberList.size(); k++){
                            text += this.makeBusArrivalInfo(this.getBusArrival(busStationLists.get(j).getStationId()), busNumberList.get(k).getValue());
                        }
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
        return KakaoResponse.valueOf(message,null);

    }

    //정류소 id로 도착하는 버스 정보
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
    public String makeBusArrivalInfo(BusArrival busArrival, String busNumber){
        String text = "";

        text += this.getBusArrivalInfoText(busArrival, busNumber);

        return text;
    }


    //정류소 정보 텍스트 출력
    public String getBusArrivalInfoText(BusArrival busArrival, String busNumber){
        String text = "";
        ArrayList<ItemList> busArrivalList = busArrival.getMsgBody().getItemList();

        if(busNumber==null) {
            text += busArrivalList.get(STANDARD).getStNm() + " 정류소 정보\n";
            for (int i = 0; i < busArrivalList.size(); i++) {
                text += busArrivalList.get(i).getRtNm() + "버스\n" +
                        "첫번째 버스 " + busArrivalList.get(i).getArrmsg1() + "\n" +
                        "두번째 버스 " + busArrivalList.get(i).getArrmsg2() + "\n";
            }
        }else{
            for(int i=0; i<busArrivalList.size(); i++){
                if(busNumber.equals(busArrivalList.get(i).getRtNm())){
                    text += busArrivalList.get(STANDARD).getStNm() + " 정류소 정보\n";
                    text += busArrivalList.get(i).getRtNm() + "버스\n" +
                            "첫번째 버스 " + busArrivalList.get(i).getArrmsg1() + "\n" +
                            "두번째 버스 " + busArrivalList.get(i).getArrmsg2() + "\n";
                    break;
                }else{
                //    text += busArrivalList.get(i).getRtNm() + "버스 정보가 없습니다.";
                }
            }
        }
        return text;
    }

//body end
}


//    public KakaoResponse makeKakaoResponse(String userKey, LUIS luisResponse) {
//        Message message = new Message();
//        Keyboard keyboard = new Keyboard();
//
//        //TODO: 기능 요약
//        String text =
//                "버스 조회 기능은 추가될 예정입니다.\n";
//
//        message.setText(text);
//        userMapper.put(userKey, Conversation.valueOf("None",null,"None", false,0));
//        return KakaoResponse.valueOf(message, null);
//    }
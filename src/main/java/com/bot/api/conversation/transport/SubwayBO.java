package com.bot.api.conversation.transport;

import com.bot.api.core.Conversable;
import com.bot.api.core.Conversation;
import com.bot.api.core.UserMapper;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Keyboard;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.luis.LUIS;
import com.bot.api.model.luis.Value;
import com.bot.api.model.transportation.Subway.RealtimeArrivalList;
import com.bot.api.model.transportation.Subway.Subway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Service
public class SubwayBO implements Conversable {

    private static final int INFOSIZE = 4;

    @Autowired
    private UserMapper userMapper;

    public KakaoResponse makeKakaoResponse(String userKey, LUIS luisResponse) {
        String text = "";
        Message message = new Message();
        Keyboard keyboard = new Keyboard();
        Subway subwayMap;
        ArrayList<Value> station;
        ArrayList<Value> subwayId;

        ArrayList<String> station_default = new ArrayList<String>();
        station_default.add("건대입구");
        station_default.add("어린이대공원");



        //subwayInfo[0] = 호선, 1 = 방향, 2 = 도착예정시간

        //entityMap은 @@호선 언제와? // 상행인지 하행인지 ㄴㄴ?
        // 1) entityMap이 비어있으면 -> 전체 호선 데이터 제공.
        // 2) entityMap에서 숫자가 적혀있으면 숫자에 해당하는 호선만 제공
        // X호선 언제와? 이렇게 물어보면 X호선 - 차, 호선 호선 = 차

        //case1) 지하철 언제와?
        //     -> entity가 지하철만 있다면 -> 모든 목록 제공해주기 -> OK
        //case2) 7호선 지하철 언제와?
        //     -> entity가 지하철이 있고 숫자- 2호선 or 7호선 이 있다면 -> 그 열차에 해당하는 목록 제공 -> OK
        //case3) 지하철역 선택하기.
        //     -> URL 파싱을 두번 요청하는문제.흠. 일단 이건 아직. 고민
        //case4) @@@지하철 언제와?(건입, 어대역이 아닌경우 그냥 정보 다알려주자
        //     ->
        //텍스트 반환 : 호선,방향, @@ 호선 @@방향 도착시간 @@입니다. 현재열차위치 xx
        //호선, 방향, 도착시간, 열차위치

        //예시 2호선 장암 방향 시간정보 : 12분 37초 후 현재 위치 : 구의사거리
        // @@호선 @@방면 @@시간 후 도착. 현재위치 : @@@

        //지하철 언제와???  역에 대한정보가 없을경우 지하철 언제와 7호선?
        if(!userMapper.get(userKey).getEntityMap().containsKey("장소") && !userMapper.get(userKey).getEntityMap().containsKey("호선")){
            //default = 건입과, 어대역입구 정보 제공하기

                for(int i=0; i<station_default.size();  i++){
                    text += this.makeSubwayInfo(this.getData(station_default.get(i)).getRealtimeArrivalList(),null);
               }

        }else if(userMapper.get(userKey).getEntityMap().containsKey("장소")){

            //역 정보 저장
            station = userMapper.get(userKey).getEntityMap().get("장소");

            //호선 정보가 있을 경우
            if(!userMapper.get(userKey).getEntityMap().containsKey("호선")){
                subwayId = userMapper.get(userKey).getEntityMap().get("호선");
                for(int i=0; i<station.size(); i++){
                    //건입, 어입, 등등에 대해서 받고 text를 추가함. 이때는 호선 정보 X
                    text += this.makeSubwayInfo(this.getData(station.get(i).getValue()).getRealtimeArrivalList(),subwayId);
                    }
            }else{
                //호선정보가 없는 경우
                for(int i=0; i<station.size(); i++){
                    text += this.makeSubwayInfo(this.getData(station.get(i).getValue()).getRealtimeArrivalList(),null);
                }
            }

        }else if(userMapper.get(userKey).getEntityMap().containsKey("호선")){
            //호선 정보만 있는 경우
            text += "지하철역을 입력해주세요";

            //장소, 호선 정보 둘 다 없는 경우
        }else{
            //빼도 될듯.
            text += "정확한 정보를 입력해주세요";
        }


        //user conversation default
        userMapper.put(userKey, Conversation.valueOf("None",null,"None", false,0));

        message.setText(text);
        return KakaoResponse.valueOf(message, null);
    }


    //지하철 정보 파싱 Input = 역이름 output = Subway객체
    public Subway getData(String station){
        RestTemplate restTemplate=null;
        HttpHeaders httpHeaders=null;
        HttpEntity<String> httpEntity;

        String key = "526f4f714e786b613730566c676145";
        String url_subway= null;
//        http://swopenapi.seoul.go.kr/api/subway/526f4f714e786b613730566c676145/json/realtimeStationArrival/1/4/건대입구;
        url_subway = "http://swopenapi.seoul.go.kr/api/subway/"+key+"/json/realtimeStationArrival/1/4/"+station;


        restTemplate = new RestTemplate();
        Subway subway = restTemplate.getForObject(url_subway, Subway.class);

        return subway;
    }

    //지하철 초 -> 포맷 변경
    public String getTime(String time){

        int seconds = Integer.parseInt(time);

        return (seconds%3600/60)+"분 "+(seconds%3600%60)+"초 후 도착 예정";

        }

    //지하철 정보 반환
    public String makeSubwayInfo(List<RealtimeArrivalList> realtimeArrivalList, ArrayList<Value> subwayId){
            String text = "";
            //호선을 비교해야하면
        if(subwayId!=null){
            //subwayId에는 1,2,3,4,5,6,7과 같은 숫자가 있음.하지만 String
            StringTokenizer st = new StringTokenizer(realtimeArrivalList.get(0).getSubwayList(),", ");

            ArrayList<String> subwayIdList = new ArrayList<String>();
            //역의 호선 정보 저장
            while(st.hasMoreTokens()){
                subwayIdList.add(st.nextToken());
            }
            //요청한 호선과 실제 연결된 호선 정보 비교
            //subwayList 는 1002, 1003, 1004등을 가지고 있음
            for(int i = 0; i<subwayId.size(); i++){
                //요청한 호선의 개수 만큼
                for(int j = 0; j<subwayIdList.size(); j++){
                    //실제 연결된 호선 수 만큼 비교
                    if(subwayId.get(i).getValue().equals(subwayIdList.get(j).substring(3))){
                        //같은 경우
                        text += this.getSubwayInfoText(realtimeArrivalList,subwayIdList.get(j));
                        break;
                    }else{
                        if(j==subwayIdList.size()-1){
                            text += subwayId.get(i).getValue()+"호선의 정보는 존재하지 않습니다\n";
                        }
                    }
                }
            }
        }else {
            //호선을 비교할필요가 없으면
            text += this.getSubwayInfoText(realtimeArrivalList);
        }
        return text;

    }


    //지하철 정보를 텍스트로 변환 (호선이 있는 경우)
    public String getSubwayInfoText(List<RealtimeArrivalList> realtimeArrivalList, String subwayId){
        String text = "";
        for (int i = 0; i < realtimeArrivalList.size(); i++) {
            if(subwayId.equals(realtimeArrivalList.get(i).getSubwayId())){
                text += realtimeArrivalList.get(i).getSubwayId().substring(3) + "호선" + realtimeArrivalList.get(i).getTrainLineNm()+ " "
                        + this.getTime(realtimeArrivalList.get(i).getBarvlDt()) + " 현재위치 : " + realtimeArrivalList.get(i).getArvlMsg3() + "\n";
            }
        }
        return text;
    }

    //지하철 정보를 텍스트로 변환 (호선이 없는 경우)
    public String getSubwayInfoText(List<RealtimeArrivalList> realtimeArrivalList){
        String text = "";
        for (int i = 0; i < realtimeArrivalList.size(); i++) {
            text += realtimeArrivalList.get(i).getSubwayId().substring(3) + "호선 " + realtimeArrivalList.get(i).getTrainLineNm()+ " "
                    + this.getTime(realtimeArrivalList.get(i).getBarvlDt()) + " 현재위치 : " + realtimeArrivalList.get(i).getArvlMsg3() + "\n";
        }
        return text;
    }


//body end
    }
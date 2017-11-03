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
import com.bot.api.model.transportation.Subway.Train;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

@Service
public class SubwayBO extends Conversable {

    private final int STANDARD = 0;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SubwayInfo subwayIdInfo;

    @Autowired
    private Subwaydefault subwaydefault;

    public KakaoResponse makeKakaoResponse(String userKey, LUIS luisResponse) {
        String text = "";
        Message message = new Message();
        Keyboard keyboard = new Keyboard();
        Subway subwayMap;

        ArrayList<Value> station;
        ArrayList<Value> subwayId;

        //예시 2호선 장암 방향 시간정보 : 12분 37초 후 현재 위치 : 구의사거리

        //지하철 언제와???  역에 대한정보가 없을경우 지하철 언제와 7호선?
        if(!userMapper.get(userKey).getEntityMap().containsKey("장소") && !userMapper.get(userKey).getEntityMap().containsKey("호선")){
            //default = 건입과, 어대역입구 정보 제공하기
            for(int i=0; i<subwaydefault.size();  i++){
            text += this.makeSubwayInfo(this.getData(subwaydefault.get(i)),null);
            }

         }else if(userMapper.get(userKey).getEntityMap().containsKey("장소")){
            //역 정보 저장
            station = userMapper.get(userKey).getEntityMap().get("장소");

            //호선 정보가 있을 경우
            if(!userMapper.get(userKey).getEntityMap().containsKey("호선")){
                subwayId = userMapper.get(userKey).getEntityMap().get("호선");
                for(int i=0; i<station.size(); i++){
                    //건입, 어입, 등등에 대해서 받고 text를 추가함. 이때는 호선 정보 X
                    text += this.makeSubwayInfo(this.getData(station.get(i).getValue()),subwayId);
                }
            }else{
                //호선정보가 없는 경우
                for(int i=0; i<station.size(); i++){
                    text += this.makeSubwayInfo(this.getData(station.get(i).getValue()),null);
                }
            }

        }else if(userMapper.get(userKey).getEntityMap().containsKey("호선")){
            //호선 정보만 있는 경우
            HashMap<String, String> slots = new HashMap<String, String>();
            slots.put("장소", "역 이름을 입력해주세요");

            Message entityMessage;
            if((entityMessage = super.findNullEntity(userKey,slots)) != null)
                return KakaoResponse.valueOf(entityMessage, null);

            //장소, 호선 정보 둘 다 없는 경우
        }else{}

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
        url_subway = "http://swopenapi.seoul.go.kr/api/subway/"+key+"/json/realtimeStationArrival/1/8/"+station;


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
    public String makeSubwayInfo(Subway subway, ArrayList<Value> subwayId){
        String text = "";
        //호선을 비교해야하면
        if(subwayId!=null){
            //subwayId에는 1,2,3,4,5,6,7과 같은 숫자가 있음.하지만 String
            StringTokenizer st = new StringTokenizer(subway.getRealtimeArrivalList().get(STANDARD).getSubwayList(),", ");

            ArrayList<String> subwayIdList = new ArrayList<String>();
            //역의 호선 정보 저장
            while(st.hasMoreTokens()){
                subwayIdList.add(st.nextToken());
            }
            //요청한 호선과 실제 연결된 호선 정보 비교
            //subwayList 는 1002, 1003, 1004 현재 역의 실제 정보를 가지고 있음
            //subwayId 는 value즉 요청한 값들임 -> 2호선 ,7호선이런거 2호선이 들어왔으 subwayId만큼 비교하면됨
            for(int i=0; i<subwayId.size(); i++){
                for(int j=0; j< subwayIdList.size(); j++){
                    //요청한 값에 대해서 실제 값들과 비교 요청값= i 실제 가능한 값 = j 끝에는 1001 과 1001이런걸 비교하게됨
                    if(subwayIdList.get(j).equals(subwayIdInfo.get(subwayId.get(i).getValue()))){
                        //일치한다면
                        text += this.getSubwayInfoText(subway, subwayIdList.get(j));
                    }
                }
            }

        }else {
            //호선을 비교할필요가 없으면
            text += this.getSubwayInfoText(subway, null);
        }
        return text;

    }

    //지하철 정보를 텍스트로 변환 (호선이 없는 경우와 있는 경우)
    public String getSubwayInfoText(Subway subway, String subwayId){
        String text = "";

        //열차별로 방향별로 한개씩 정보를 저장해야함.
        ArrayList<Train> trains = new ArrayList<Train>();
        Train train = null;
        for(int i=0; i<subway.getRealtimeArrivalList().size(); i++){
            for(int j=0; j<trains.size(); j++){
                if(trains.size()==0){
                    //처음은 그냥입력시키자
                    train.valueOf(subway.getRealtimeArrivalList().get(i).getSubwayId(),
                            subway.getRealtimeArrivalList().get(i).getTrainLineNm(),
                            subway.getRealtimeArrivalList().get(i).getSubwayHeading(),
                            subway.getRealtimeArrivalList().get(i).getBarvlDt(),
                            subway.getRealtimeArrivalList().get(i).getArvlMsg3());

                    trains.add(train);
                }else{
                    // 호선과 방향이 일치한다면 가장적게 걸리는 시간만을 입력시키자.
                    if(trains.get(j).getSubwayId().equals(subway.getRealtimeArrivalList().get(i).getSubwayId()) &&
                        trains.get(j).getSubwayHeading().equals(subway.getRealtimeArrivalList().get(i).getSubwayHeading())){
                        //호선도 같고 방향도 같은것 중에서 더 짧게 걸리는 지하철을 추가시킨다. - 먼저 기존의 지하철을 빼고 1
                        if(Integer.parseInt(trains.get(j).getBarvlDt())>Integer.parseInt(subway.getRealtimeArrivalList().get(i).getBarvlDt())){
                            trains.remove(j);
                            train.valueOf(subway.getRealtimeArrivalList().get(i).getSubwayId(),
                                    subway.getRealtimeArrivalList().get(i).getTrainLineNm(),
                                    subway.getRealtimeArrivalList().get(i).getSubwayHeading(),
                                    subway.getRealtimeArrivalList().get(i).getBarvlDt(),
                                    subway.getRealtimeArrivalList().get(i).getArvlMsg3());
                            trains.add(j, train);
                        }
                    }else{
                        //호선과 방향이 일치하지 않으면 그냥 추가 시키면 됨
                        train.valueOf(subway.getRealtimeArrivalList().get(i).getSubwayId(),
                                subway.getRealtimeArrivalList().get(i).getTrainLineNm(),
                                subway.getRealtimeArrivalList().get(i).getSubwayHeading(),
                                subway.getRealtimeArrivalList().get(i).getBarvlDt(),
                                subway.getRealtimeArrivalList().get(i).getArvlMsg3());

                        trains.add(train);
                    }
                }
                //정보를 입력하고 id, 방면, 방향, 시간, 현재 위치를 모두저장
            }
        }
        if(subwayId==null) {
            for (int i = 0; i < trains.size(); i++) {
                text += subwayIdInfo.get(trains.get(i).getSubwayId()) + " " + trains.get(i).getTrainLineNm() + " "
                        + this.getTime(trains.get(i).getBarvlDt()) + " 현재위치 : " + trains.get(i).getArvlMsg3() + "\n";
            }
        }else{
            for (int i = 0; i < trains.size(); i++) {
                if(subwayId.equals(trains.get(i).getSubwayId())){
                    text += subwayIdInfo.get(trains.get(i).getSubwayId()) + " "  + trains.get(i).getTrainLineNm()+ " "
                            + this.getTime(trains.get(i).getBarvlDt()) + " 현재위치 : " + trains.get(i).getArvlMsg3() + "\n";
                }
            }
        }
        return text;
    }

//body end
}
//    public KakaoResponse makeKakaoResponse(String userKey, LUIS luisResponse) {
//        String text = "";
//        Message message = new Message();
//        Keyboard keyboard = new Keyboard();
//
//        if(userMapper.get(userKey).getEntityMap().containsKey("지하철역")) {
//            for(Value value : userMapper.get(userKey).getEntityMap().get("지하철역")) {
//                text += value.getValue() + " A호선 B방면: n분전\n";
//                text += value.getValue() + " A호선 C방면: n분전\n";
//            }
//        } else {
//            text += "건대입구 2호선 A방면: a분전\n";
//            text += "건대입구 2호선 B방면: b분전\n";
//            text += "건대입구 7호선 C방면: c분전\n";
//            text += "건대입구 7호선 D방면: d분전\n";
//            text += "어린이대공원 2호선 E방면: e분전\n";
//            text += "어린이대공원 7호선 F방면: f분전";
//        }
//
//        userMapper.put(userKey, Conversation.valueOf("None",null,"None", false,0));
//
//        message.setText(text);
//        return KakaoResponse.valueOf(message, null);
//    }
package com.bot.api.conversation.transport;

import com.bot.api.core.Conversable;
import com.bot.api.core.Conversation;
import com.bot.api.core.UserMapper;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.luis.LUIS;
import com.bot.api.model.luis.Value;
import com.bot.api.model.transportation.Subway.Subway;
import com.bot.api.model.transportation.Subway.Train;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.AsyncRestTemplate;
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

    @Autowired
    private SubwayTable subwayTable;

    public KakaoResponse makeKakaoResponse(String userKey, LUIS luisResponse) {
        String text = "";
        Message message = new Message();
        // Keyboard keyboard = new Keyboard();
        //   Subway subwayMap;

        ArrayList<Value> station;
        ArrayList<Value> subwayId;

        //실제 사용할 역명(맵핑)
        ArrayList<String> stationList;


        //지하철 언제와???  역에 대한 정보가 없을경우
        if (!userMapper.get(userKey).getEntityMap().containsKey("장소") && !userMapper.get(userKey).getEntityMap().containsKey("호선")) {
            //default = 건입과, 어대역입구 정보 제공하기
            System.out.println("Default Subway");
            for (int i = 0; i < subwaydefault.size(); i++) {
                if(i==0)
                    text += "건대입구역\n";
                else
                    text += "어린이대공원역\n";

                System.out.println(subwaydefault.get(i));
                text += this.makeSubwayInfo(this.getData(subwaydefault.get(i)), null);

            }

        } else if (userMapper.get(userKey).getEntityMap().containsKey("장소")) {
            //역 정보 저장
            station = userMapper.get(userKey).getEntityMap().get("장소");
         //   System.out.println("station name :"+station.get(0).getValue());
        //    System.out.println("size "+station.size());
            stationList = new ArrayList<String>();
            for (int i = 0; i < station.size(); i++) {

                if (station.get(i).getValue().equals("어린이대공원")) {
                    stationList.add(subwayTable.get("어린이대공원"));
                } else {
                    stationList.add(station.get(i).getValue());
                }
            }
            for(int i=0; i< stationList.size(); i++){
                System.out.println(stationList.get(i));
            }
            //station.get(0).getValue();
            //호선 정보가 있을 경우
            if (userMapper.get(userKey).getEntityMap().containsKey("호선")) {
                System.out.println("Line Info exist");
                subwayId = userMapper.get(userKey).getEntityMap().get("호선");
                for (int i = 0; i < station.size(); i++) {
                    //건입, 어입, 등등에 대해서 받고 text를 추가함. 이때는 호선 정보 X
                    text += this.makeSubwayInfo(this.getData(stationList.get(i)), subwayId);
                }
            } else {
                //호선정보가 없는 경우
                System.out.println("No Line Info");

                for (int i = 0; i < stationList.size(); i++) {
                    System.out.println(stationList.get(i));
                    text += this.makeSubwayInfo(this.getData(stationList.get(i)), null);
                }
            }

        } else if (userMapper.get(userKey).getEntityMap().containsKey("호선")) {
            //호선 정보만 있는 경우
            HashMap<String, String> slots = new HashMap<String, String>();
            slots.put("장소", "역 이름을 입력해주세요");

            Message entityMessage;
            if ((entityMessage = super.findNullEntity(userKey, slots)) != null)
                return KakaoResponse.valueOf(entityMessage, null);

            //장소, 호선 정보 둘 다 없는 경우
        } else {
        }

        //user conversation default
        // userMapper.put(userKey, Conversation.valueOf("None",null,"None", false,0));
       // station = null;
      //  subwayId = null;
       // stationList = null;

        message.setText(text);
        userMapper.put(userKey, Conversation.valueOf("None",null,"None", false,0));
        return KakaoResponse.valueOf(message, null);
    }


    //지하철 정보 파싱 Input = 역이름 output = Subway객체
    public Subway getData(String station) {
        Subway subway = null;
        RestTemplate restTemplate = null;
        AsyncRestTemplate asyncRestTemplate = null;
//        HttpHeaders httpHeaders=null;
//        HttpEntity<String> httpEntity;

        String key = "526f4f714e786b613730566c676145";
        String url_subway = null;
//        http://swopenapi.seoul.go.kr/api/subway/526f4f714e786b613730566c676145/json/realtimeStationArrival/1/4/건대입구;
        url_subway = "http://swopenapi.seoul.go.kr/api/subway/" + key + "/json/realtimeStationArrival/1/8/" + station;


     //   restTemplate = new RestTemplate();
        asyncRestTemplate = new AsyncRestTemplate();
//        Subway subway = restTemplate.getForObject(url_subway, Subway.class);
        subway = (Subway) asyncRestTemplate.getForEntity(url_subway, Subway.class);
//        ResponseEntity<Subway> subwayResponseEntity = (ResponseEntity<Subway>) asyncRestTemplate.getForEntity(url_subway, Subway.class);

        return subway;
    }

    //지하철 초 -> 포맷 변경
    public String getTime(String time) {

        int seconds = Integer.parseInt(time);

        return (seconds % 3600 / 60) + "분 " + (seconds % 3600 % 60) + "초 후 도착 예정";

    }

    //지하철 정보 반환
    public String makeSubwayInfo(Subway subway, ArrayList<Value> subwayId) {
        String text = "";

        //호선을 비교해야하면
        if (subwayId != null) {
            //subwayId에는 1,2,3,4,5,6,7과 같은 숫자가 있음.하지만 String
            StringTokenizer st = new StringTokenizer(subway.getRealtimeArrivalList().get(STANDARD).getSubwayList(), ", ");
            System.out.println("subwayId check");
            ArrayList<String> subwayIdList = new ArrayList<String>();
            //역의 호선 정보 저장
            while (st.hasMoreTokens()) {
                subwayIdList.add(st.nextToken());
            }
            for(int i=0; i<subwayIdList.size(); i++){
                System.out.println("SubwayIdList :"+subwayIdList.get(i));
            }
            //요청한 호선과 실제 연결된 호선 정보 비교
            //subwayList 는 1002, 1003, 1004 현재 역의 실제 정보를 가지고 있음
            //subwayId 는 value즉 요청한 값들임 -> 2호선 ,7호선이런거 2호선이 들어왔으 subwayId만큼 비교하면됨
            for (int i = 0; i < subwayId.size(); i++) {
                for (int j = 0; j < subwayIdList.size(); j++) {
                    //요청한 값에 대해서 실제 값들과 비교 요청값= i 실제 가능한 값 = j 끝에는 1001 과 1001이런걸 비교하게됨
                    if (subwayId.get(i).getValue().equals(subwayIdInfo.get(subwayIdList.get(j)))) {
                        //일치한다면
                        System.out.println("get Line Text");
                        text += this.getSubwayInfoText(subway, subwayIdList.get(j));
                        break;
                    }else{
                        //끝까지 탐색했는데 값이 없다면
                        if(j==subwayIdList.size()-1){
                            text += subwayId.get(i).getValue() + " 정보가 존재하지 않습니다.\n";
                        }
                    }
                }
            }

        } else {
            //호선을 비교할필요가 없으면
            text += this.getSubwayInfoText(subway, null);
        }

        return text;

    }

    //지하철 정보를 텍스트로 변환 (호선이 없는 경우와 있는 경우)
    public String getSubwayInfoText(Subway subway, String subwayId) {
        String text = "";
        // System.out.println("getSubwayInfoText");

        //열차별로 방향별로 한개씩 정보를 저장해야함.
        ArrayList<Train> trains = new ArrayList<Train>();
        Train train = null;
        System.out.println("사이즈 " + subway.getRealtimeArrivalList().size());
        // total size 만큼 반복 but subway가 없다면..
        if(subway.getRealtimeArrivalList().size()==0){
            text += "현재 운행중인 지하철이 없습니다.\n";
            return text;
        }
        for (int i = 0; i < subway.getRealtimeArrivalList().size(); i++) {
            //0일떄 add 1일때 add ,4,8,12,단위로 최단거리 1,2,
            if(i%4==0 || i%4==1){
                train = Train.valueOf(subway.getRealtimeArrivalList().get(i).getSubwayId(),
                        subway.getRealtimeArrivalList().get(i).getTrainLineNm(),
                        subway.getRealtimeArrivalList().get(i).getSubwayHeading(),
                        subway.getRealtimeArrivalList().get(i).getBarvlDt(),
                        subway.getRealtimeArrivalList().get(i).getArvlMsg3());

                trains.add(train);
            }
            //정보를 입력하고 id, 방면, 방향, 시간, 현재 위치를 모두저장
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
        //trains = null;
        return text;
    }

//body end
}

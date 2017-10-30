package com.bot.api.model.transportation.Subway;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor(staticName = "valueOf")
public class RealtimeArrivalList {

    //지하철 번호, 지하철 방면,subwayList는 현재 열차에 연결되는 열차들 확인, 걸리는 시간(초), 열차 위치
//    private String beginRow;
//    private String endRow;
//    private String curPage;
//    private String pageRow;
//    private int totalCount;
//    private int rowNum;
//    private String selectedCount;
    private String subwayId;
//    private String subwayNm;
//    private String updnLine;
    private String trainLineNm;
//    private String subwayHeading;
//    private String statnFid;
//    private String statnTid;
//    private String statnNm;
//    private String trainCo;
//    private String ordkey;
    private String subwayList;
//    private String statnList;
//    private String btrainSttus;
    private String barvlDt;
//    private String btrainNo;
//    private String bstatnId;
//    private String recptnDt;
//    private String arvlMsg2;
    private String arvlMsg3;
//    private String arvlCd;

}

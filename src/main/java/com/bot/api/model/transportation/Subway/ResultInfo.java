package com.bot.api.model.transportation.Subway;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor(staticName = "valueOf")
public class ResultInfo {
    String beginRow;
    String endRow;
    String curPage;
    String pageRow;
    String totalCount;
    String rowNum;
    String selectedCount;
    String subwayId;
    String subwayNm;
    String updnLine;
    String trainLineNm;
    String subwayHeading;
    String statnFid;
    String statnTid;
    String statnNm;
    String trainCo;
    String ordkey;
    String subwayList;
    String statnList;
    String btrainSttus;
    String barvlDt;
    String btrainNo;
    String bstatnId;
    String recptnDt;
    String arvlMsg2;
    String arvlMsg3;
    String arvlCd;


}

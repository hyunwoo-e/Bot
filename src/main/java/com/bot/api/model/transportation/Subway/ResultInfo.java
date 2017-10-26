package com.bot.api.model.transportation.Subway;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor(staticName = "valueOf")
public class ResultInfo {
    private String beginRow;
    private String endRow;
    private String curPage;
    private String pageRow;
    private String totalCount;
    private String rowNum;
    private String selectedCount;
    private String subwayId;
    private String subwayNm;
    private String updnLine;
    private String trainLineNm;
    private String subwayHeading;
    private String statnFid;
    private String statnTid;
    private String statnNm;
    private String trainCo;
    private String ordkey;
    private String subwayList;
    private String statnList;
    private String btrainSttus;
    private String barvlDt;
    private String btrainNo;
    private String bstatnId;
    private String recptnDt;
    private String arvlMsg2;
    private String arvlMsg3;
    private String arvlCd;


}

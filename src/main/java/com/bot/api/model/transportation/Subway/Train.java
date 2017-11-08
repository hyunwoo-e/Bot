package com.bot.api.model.transportation.Subway;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor(staticName = "valueOf")
public class Train implements Serializable {

    private String subwayId;
    private String trainLineNm;
    private String subwayHeading;
    private String barvlDt;
    private String arvlMsg3;

    public Train(){}

//    public Train(String subwayId, String trainLineNm, String subwayHeading, String barvlDt, String arvlMsg3){
//        this.subwayId = subwayId;
//        this.trainLineNm = trainLineNm;
//        this.subwayHeading = subwayHeading;
//        this.barvlDt = barvlDt;
//        this.arvlMsg3 = arvlMsg3;
//    }
}

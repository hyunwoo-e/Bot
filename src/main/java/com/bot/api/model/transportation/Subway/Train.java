package com.bot.api.model.transportation.Subway;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor(staticName = "valueOf")
public class Train {

    private String subwayId;
    private String trainLineNm;
    private String subwayHeading;
    private String barvlDt;
    private String arvlMsg3;

    public Train(){}
}

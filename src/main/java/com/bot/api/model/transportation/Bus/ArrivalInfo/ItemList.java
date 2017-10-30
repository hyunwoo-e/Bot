package com.bot.api.model.transportation.Bus.ArrivalInfo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.xml.bind.annotation.XmlElement;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor(staticName = "valueOf")
public class ItemList {

    //첫번째 버스 도착정보, 두번째 버스 도착정보, 버스번호
    private String arrmsg1;
    private String arrmsg2;
    private String rtNm;


    public ItemList(){}
}

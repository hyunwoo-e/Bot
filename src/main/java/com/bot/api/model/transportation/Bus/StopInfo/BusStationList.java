package com.bot.api.model.transportation.Bus.StopInfo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor(staticName = "valueOf")
@XmlRootElement(name = "")
public class BusStationList {

    // 정류소 id 9자리
    private String stationId;

    public BusStationList(){}
}

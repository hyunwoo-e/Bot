package com.bot.api.model.transportation.Bus.ArrivalInfo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor(staticName = "valueOf")
@XmlRootElement(name = "ServiceResult")
public class BusArrival {
    private MsgBody msgBody;

    public BusArrival(){}
}

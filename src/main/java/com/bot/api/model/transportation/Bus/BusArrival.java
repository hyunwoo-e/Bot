package com.bot.api.model.transportation.Bus;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "ServiceResult")
public class BusArrival {


    @XmlElement(name = "msgHeader")
    private MsgHeader msgHeader;

    @XmlElement(name = "msgBody")
    private MsgBody msgBody;


}

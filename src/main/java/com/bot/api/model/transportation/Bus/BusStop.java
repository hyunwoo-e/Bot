package com.bot.api.model.transportation.Bus;


import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


//버스정류소정보조회
@Data
@XmlRootElement(name = "bus")
public class BusStop {

    @XmlElement(name = "stId")
    private String stId;

    @XmlElement(name = "stNm")
    private String stNm;


}

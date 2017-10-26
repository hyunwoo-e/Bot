package com.bot.api.model.transportation.Bus;


import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "msgHeader")
@Data
public class MsgHeader {

    @XmlElement(name = "headerCd")
    private String headerCd;

    @XmlElement(name = "headerMsg")
    private String headerMsg;

    @XmlElement(name = "itemCount")
    private String itemCount;
}

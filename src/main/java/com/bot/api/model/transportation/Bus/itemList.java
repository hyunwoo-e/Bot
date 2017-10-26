package com.bot.api.model.transportation.Bus;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "itemList")
@Data
public class itemList {


    @XmlElement(name = "arrmsg1")
    private String arrmsg1;
}

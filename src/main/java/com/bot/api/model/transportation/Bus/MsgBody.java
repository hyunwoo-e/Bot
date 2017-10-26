package com.bot.api.model.transportation.Bus;

import lombok.Data;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "msgBody")
@Data
public class MsgBody {


    @XmlElementWrapper(name = "itemList")
    private List<itemList> itemListList;
}

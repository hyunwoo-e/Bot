package com.bot.api.model.transportation.Bus.ArrivalInfo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor(staticName = "valueOf")
public class MsgBody {
    private ArrayList<ItemList> itemList;

    public MsgBody(){}
}

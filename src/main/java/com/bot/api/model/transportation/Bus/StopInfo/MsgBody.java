package com.bot.api.model.transportation.Bus.StopInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor(staticName = "valueOf")
public class MsgBody {
    private ArrayList<BusStationList> busStationList;

    public MsgBody(){}
}

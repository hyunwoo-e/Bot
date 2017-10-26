package com.bot.api.model.transportation.Subway;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor(staticName = "valueOf")
public class RealtimeArrivalList {
    List<ResultInfo> resultInfos;

}

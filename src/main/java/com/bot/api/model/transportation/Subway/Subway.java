package com.bot.api.model.transportation.Subway;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor(staticName = "valueOf")
public class Subway {
    private ErrorMessage errorMessage;
    private RealtimeArrivalList realtimeArrivalList;
}

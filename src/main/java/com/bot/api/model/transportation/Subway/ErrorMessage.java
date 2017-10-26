package com.bot.api.model.transportation.Subway;

import com.fasterxml.jackson.annotation.JsonInclude;
import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor(staticName = "valueOf")
public class ErrorMessage {
    private String status;
    private String code;
    private String message;
    private String link;
    private String developerMessage;
    private String total;
}

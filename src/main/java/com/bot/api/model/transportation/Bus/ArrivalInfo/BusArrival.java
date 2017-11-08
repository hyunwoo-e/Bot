package com.bot.api.model.transportation.Bus.ArrivalInfo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor(staticName = "valueOf")
@XmlRootElement(name = "ServiceResult")
public class BusArrival extends ArrayList<ListenableFuture<ResponseEntity<BusArrival>>> {
    private MsgBody msgBody;

    public BusArrival(){}
}

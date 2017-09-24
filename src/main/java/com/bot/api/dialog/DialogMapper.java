package com.bot.api.dialog;

import com.bot.api.dialog.exchange.ExchangeBO;
import com.bot.api.dialog.none.NoneBO;
import com.bot.api.dialog.order.OrderBO;
import com.bot.api.model.dialog.Dialog;
import com.bot.api.model.dialog.dialog.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class DialogMapper {

    private Map<String, Dialogable> dialogMap;

    @Autowired
    private NoneBO noneBO;

    @Autowired
    private OrderBO orderBO;

    @Autowired
    private ExchangeBO exchangeBO;

    @PostConstruct
    public void init(){
        dialogMap = new HashMap<String, Dialogable>();
        dialogMap.put("None", noneBO);
        dialogMap.put("주문", orderBO);
        dialogMap.put("교환", exchangeBO);
    }

    public Dialogable getDialog(String key) {
        return dialogMap.get(key);
    }
}

package com.bot.api.model.dialog.dialog;

import lombok.Data;

@Data
public class Exchange {
    String exchangeType; //사이즈, 불량, 품목
    String exchangeProduct; //
    String exchangeCount;
    Boolean traceYn;
}

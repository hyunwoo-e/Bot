package com.bot.api.dialog.exchange;

import com.bot.api.model.dialog.dialog.Exchange;
import com.bot.api.model.dialog.order.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public interface ExchangeDAO {
    Exchange selectUserExchange(@Param("userId") String userId);

    int insertUserExchange(@Param("userId") String userId);

    int updateUserExchange(@Param("userId") String userId, @Param("entities") HashMap entities);

    int deleteUserExchange(@Param("userId") String userId);
}

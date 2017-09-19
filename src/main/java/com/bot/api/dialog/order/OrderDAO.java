package com.bot.api.dialog.order;

import com.bot.api.model.dialog.order.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDAO {
    Order selectOrderByUser(@Param("userId") String userId);
}

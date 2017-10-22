package com.bot.api.user;

import com.bot.api.model.common.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO {
     User selectUser(@Param("userKey") String userKey);
}

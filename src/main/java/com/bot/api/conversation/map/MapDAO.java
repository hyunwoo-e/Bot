package com.bot.api.conversation.map;

import com.bot.api.model.common.KonkukMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MapDAO {
    KonkukMap selectMap(@Param("mapName") String mapName);
}









package com.bot.api.conversation.profile;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public interface PartyDAO {
    HashMap<String, String> selectPartyByName(@Param("partyName") String partyName);
}

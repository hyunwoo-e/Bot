package com.bot.api.conversation.member;

import com.bot.api.model.common.Member;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberDAO {
    List<Member> selectMembers(@Param("memberNames") List<String> memberNames, @Param("memberDepartments") List<String> memberDepartments);
}









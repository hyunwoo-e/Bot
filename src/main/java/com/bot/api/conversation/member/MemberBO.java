package com.bot.api.conversation.member;

import com.bot.api.core.Conversable;
import com.bot.api.core.Conversation;
import com.bot.api.core.UserMapper;
import com.bot.api.model.common.Member;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Keyboard;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.luis.LUIS;
import com.bot.api.model.luis.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MemberBO extends Conversable {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MemberDAO memberDAO;

    public KakaoResponse makeKakaoResponse(String userKey, LUIS luis) {
        return makeDefaultResponse(userKey, luis);
    }

    public KakaoResponse makeDefaultResponse(String userKey, LUIS luis) {
        String text = "";
        Message message = new Message();
        Keyboard keyboard = new Keyboard();

        // SLOT 정의
        HashMap<String, String> slots = new HashMap<String, String>();
        slots.put("관계자", "조회하실 대상을 말씀해주세요.");

        // NULL 엔티티 요청
        if((message = super.findNullEntity(userKey,slots)) != null)
            return KakaoResponse.valueOf(message, null);

        // 관계자 조회
        List<Member> members = selectMembers(userKey);
        for (Member member : members) {
            text += member.getMemberDepartment() + " " + member.getMemberName() + member.getMemberType() + "님의 ";
            if (userMapper.get(userKey).getEntityMap().containsKey("정보")) {
                // 중복 제거
                HashSet<String> infoSet = new HashSet<String>();
                for(Value value : userMapper.get(userKey).getEntityMap().get("정보")) {
                    infoSet.add(value.getValue());
                }

                if(infoSet.contains("전화번호")) {
                    text += "전화번호는 " + member.getMemberRoomTel() + " ";
                }

                if(infoSet.contains("주소")) {
                    text += "주소는 " + member.getMemberAddress() + " ";
                }

                if(infoSet.contains("메일")) {
                    text += "메일은 " + member.getMemberEmail() + " ";
                }
            } else {
                text += "전화번호는 " + member.getMemberRoomTel() + " 주소는 " + member.getMemberAddress() + " 메일은 " + member.getMemberEmail() + " ";
            }
            text += "입니다.\n";
            message.setText(text);
        }

        userMapper.put(userKey, Conversation.valueOf("None",null,"None", false,0));
        return KakaoResponse.valueOf(message, null);
    }

    // 관계자 조회
    public List<Member> selectMembers(String userKey) {
        // 필터 조건 - 관계자
        List<String> memberNames = null;
        if (userMapper.get(userKey).getEntityMap().containsKey("관계자")) {
            memberNames = new ArrayList<String>();
            for (Value value : userMapper.get(userKey).getEntityMap().get("관계자")) {
                memberNames.add(value.getValue());
            }
        }

        // 필터 조건 - 학과
        List<String> memberDepartments = null;
        if (userMapper.get(userKey).getEntityMap().containsKey("학과")) {
            memberDepartments = new ArrayList<String>();
            for (Value value : userMapper.get(userKey).getEntityMap().get("학과")) {
                memberDepartments.add(value.getValue());
            }
        }

        return memberDAO.selectMembers(memberNames, memberDepartments);
    }
}
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

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberBO implements Conversable {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MemberDAO memberDAO;

    public KakaoResponse makeKakaoResponse(String userKey, LUIS luisResponse) {
        String text = "";
        Message message = new Message();
        Keyboard keyboard = new Keyboard();

        List<String> memberDepartments = new ArrayList<String>();
        if(userMapper.get(userKey).getEntityMap().containsKey("학과")) {
            for(Value value : userMapper.get(userKey).getEntityMap().get("학과")) {
                memberDepartments.add(value.getValue());
            }
        }

        List<String> memberNames = new ArrayList<String>();
        if(userMapper.get(userKey).getEntityMap().containsKey("관계자")) {
            for(Value value : userMapper.get(userKey).getEntityMap().get("관계자")) {
                memberNames.add(value.getValue());
            }
        }

        if(userMapper.get(userKey).getEntityMap().containsKey("관계자")) {
            List<Member> members;
            if(userMapper.get(userKey).getEntityMap().containsKey("학과")) {
                members = memberDAO.selectMembers(memberNames, memberDepartments);
            } else {
                members = memberDAO.selectMembers(memberNames, null);
            }

            for (Member member : members) {
                text += member.getMemberDepartment() + " " + member.getMemberName() + member.getMemberType() + "님의 ";
                if(userMapper.get(userKey).getEntityMap().containsKey("관계자정보")) {
                    for(Value value : userMapper.get(userKey).getEntityMap().get("관계자정보")) {
                        if (value.getValue().equals("전화번호")) {
                            text += "전화번호는 " + member.getMemberRoomTel() + " ";
                        }

                        if (value.getValue().equals("주소")) {
                            text += "주소는 " + member.getMemberAddress() + " ";
                        }

                        if (value.getValue().equals("메일")) {
                            text += "메일은 " + member.getMemberEmail() + " ";
                        }
                    }
                } else {
                    text += "전화번호는 " + member.getMemberRoomTel() + " 주소는 " + member.getMemberAddress() + " 메일은 " + member.getMemberEmail() + " ";
                }
                text += "입니다.\n";
            }

            userMapper.put(userKey, Conversation.valueOf("None",null,"None", false,0));
        } else {
           text = "조회하실 관계자를 말씀해주세요.";
        }

        message.setText(text);
        return KakaoResponse.valueOf(message, keyboard);
    }
}
package com.bot.api.conversation.etcData;

import com.bot.api.core.UserMapper;
import com.bot.api.model.Link.Link;
import com.bot.api.model.kakao.KakaoResponse;
import com.bot.api.model.kakao.Keyboard;
import com.bot.api.model.kakao.Message;
import com.bot.api.model.luis.LUIS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class LinkBO {

    @Autowired
    private UserMapper userMapper;


    private Link link;

    public KakaoResponse makeKakaoResponse(String userKey, LUIS luisResponse) throws IOException {
        String text = "";
        Message message = new Message();
        Keyboard keyboard = new Keyboard();

        text += "test";
        //링크 12가지
        //1)링크 조회
        //2) 셋팅 되어 있는 값은 한글. -> @@ 이면 1 -> 1번 링크 반환
        //2) entity는 한개 이상이니까 첫번쨰 entity의 링크 set text, 두번째 enttiy링크 셋....
        if(!userMapper.get(userKey).getEntityMap().isEmpty()){
            for(String s : userMapper.get(userKey).getEntityMap().keySet()){
                int num = link.getLinkMap().get(s);

                switch (num){
                    case 0:
                        System.out.println("졸업문의");
                        text += "졸업문의 주소\n";
                        break;
                    case 1:
                        System.out.println("취득학점포기");
                        text += "졸업문의 주소\n";
                        break;
                    case 2:
                        System.out.println("수강신청문의");
                        text += "졸업문의 주소\n";
                        break;
                    case 3:
                        System.out.println("장학금문의");
                        text += "졸업문의 주소\n";
                        break;
                    case 4:
                        System.out.println("편의시설문의");
                        text += "졸업문의 주소\n";
                        break;
                    case 5:
                        System.out.println("학생증발급문의");
                        text += "졸업문의 주소\n";
                        break;
                    case 6:
                        System.out.println("아직 입력 안됨.");
                        text += "졸업문의 주소\n";
                        break;
                    case 7:
                        System.out.println("아직 입력 안됨.");
                        text += "졸업문의 주소\n";
                        break;
                    case 8:
                        System.out.println("아직 입력 안됨.");
                        text += "졸업문의 주소\n";
                        break;
                    case 9:
                        System.out.println("아직 입력 안됨.");
                        text += "졸업문의 주소\n";
                        break;
                    case 10:
                        System.out.println("아직 입력 안됨.");
                        text += "졸업문의 주소\n";
                        break;
                    case 11:
                        System.out.println("아직 입력 안됨.");
                        text += "졸업문의 주소\n";
                        break;

                    default :
                        break;

                }

            }

        }

        message.setText(text);
        return KakaoResponse.valueOf(message,null);
    }


}

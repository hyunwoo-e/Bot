package com.bot.api.kakao;

import com.bot.api.dialog.DialogBO;
import com.bot.api.model.kakao.KakaoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Hyunwoo on 2017-07-31.
 */
@Controller
@RequestMapping("/kakao")
public class KakaoController {

    @Autowired
    private KakaoBO kakaoBO;

    @Autowired
    private DialogBO dialogBO;

    /*
    * 카카오 자동응답 API 채팅룸 입장
    * */
    @RequestMapping(value="/keyboard", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity enterRoom() {
        return new ResponseEntity(kakaoBO.enterRoom(), HttpStatus.OK);
    }

    /*
    * 카카오 자동응답 API 메시지 수신
    * */
    @RequestMapping(value="/message", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity recvMessage(@RequestBody KakaoRequest kakaoRequest) {
        return new ResponseEntity(dialogBO.recvMessage(kakaoRequest), HttpStatus.OK);
    }

    @RequestMapping(value="/chat_room/{userKey}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity exitRoom(@PathVariable String userKey) {
        return new ResponseEntity(HttpStatus.OK);
    }

    /*
    * 카카오 자동응답 API 친구 등록
    * */
    @RequestMapping(value="/friend", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity addFriend(KakaoRequest request) {
        return new ResponseEntity(HttpStatus.OK);
    }

    /*
    * 카카오 자동응답 API 친구 삭제
    * */
    @RequestMapping(value="/friend/{userKey}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity deleteFriend(@PathVariable String userKey) {
        return new ResponseEntity(HttpStatus.OK);
    }

    //TODO: GlobalExceptionHandler 적용
    @ExceptionHandler(Exception.class)
    public void globalExceptionHandler(Exception e){
        System.out.println("[카카오 요청 에러]");
        return;
    }
}

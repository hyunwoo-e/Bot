package com.bot.api.model.Link;


import lombok.Data;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Data
public class Link {

    //한글 -> 숫자 -> 주소
    HashMap<String,Integer> linkMap;

    @PostConstruct
    public void init(){
        linkMap = new HashMap<String, Integer>();

        linkMap.put("졸업문의",0);
        linkMap.put("취득학점포기",1);
        linkMap.put("수강신청문의",2);
        linkMap.put("장학금문의",3);
        linkMap.put("편의시설문의",4);
        linkMap.put("학생증발급문의",5);

    }

    /*
    졸업문의
취득학점포기
수강신청문의
장학금문의
편의시설문의
학생증발급문의

     */

//    public HashMap<Integer,String>[] makeNumtoLink(){
//        HashMap<Integer,String> numtolinkMap = new HashMap<Integer, String>();
//
//        numtolinkMap.put(0,"졸업문의");
//        numtolinkMap.put(1,"취득학점포기");
//        numtolinkMap.put(2,"수강신청문의");
//        numtolinkMap.put(3,"장학금문의");
//        numtolinkMap.put(4,"편의시설문의");
//        numtolinkMap.put(5,"학생증발급문의");
//
//
//    }







}

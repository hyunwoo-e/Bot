package com.bot.api.model.common;

import lombok.Data;

import java.util.Date;

@Data
public class Member {
    private Integer memberNo;
    private String memberDepartment;
    private String memberType;
    private String memberName;
    private String memberAddress;
    private String memberRoomTel;
    private String memberMobileTel;
    private String memberEmail;
    private Date registYmdt;
    private Date modifyYmdt;
}

package com.bot.api.model.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class User {
    private String userKey;
    private String userId;
    private String userPassword;
    private Integer userStudentNumber;
    private String userName;
    private String userTel;
    @JsonIgnore
    private Date registYmdt;
    private Date modifyYmdt;
}


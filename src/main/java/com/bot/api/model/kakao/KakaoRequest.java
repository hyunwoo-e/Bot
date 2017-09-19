package com.bot.api.model.kakao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by Hyunwoo on 2017-07-31.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class KakaoRequest implements Serializable  {
    private static final long serialVersionUID = 510750218701L;

    private String user_key;
    private String type;
    private String content;
}

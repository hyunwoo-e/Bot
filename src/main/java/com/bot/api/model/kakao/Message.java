package com.bot.api.model.kakao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by Hyunwoo on 2017-07-31.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Message implements Serializable {
    private static final long serialVersionUID = 18572157081L;

    private String text;
    private Photo photo;
    private MessageButton message_button;
}

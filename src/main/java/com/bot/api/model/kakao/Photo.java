package com.bot.api.model.kakao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Created by Hyunwoo on 2017-07-31.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Photo {
    private String url;
    private int width;
    private int height;
}

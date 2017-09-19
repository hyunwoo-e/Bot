package com.bot.api.model.luis;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Hyunwoo on 2017-07-31.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Entity implements Serializable {
    private static final long serialVersionUID = -117958171231591L;

    private String entity;
    private String type;
    private int startIndex;
    private int endIndex;
    private Resolution resolution;
}

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
@AllArgsConstructor(staticName = "valueOf")
public class LUIS implements Serializable {
    private static final long serialVersionUID = -11795817591L;
    private String query;
    private Intent intent;
    private List<Entity> entities;
}

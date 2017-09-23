package com.bot.api.dialog;

import com.bot.api.model.dialog.Dialog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DialogDAO {

    int insertUserDialog(@Param("userId") String userId, @Param("dialogId") String dialogId, @Param("dialogStatusCode") int dialogStatus);

    int updateUserDialog(@Param("userId") String userId, @Param("dialogId") String dialogId, @Param("dialogStatusCode") int dialogStatus);

    int deleteUserDialog(@Param("userId") String userId);

    Dialog selectUserDialog(@Param("userId") String userId);
}

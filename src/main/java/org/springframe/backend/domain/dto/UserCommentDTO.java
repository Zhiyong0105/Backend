package org.springframe.backend.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframe.backend.domain.BaseData;

@Data
public class UserCommentDTO implements BaseData {
    @NotNull
    private Integer type;
    private Integer typeId;
    private Integer parentId;
    private Integer replyId;
    private String commentContent;
    private Integer replyUserId;
}
